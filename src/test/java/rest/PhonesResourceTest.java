package rest;

import dto.PhoneDTO;
import dto.PhonesDTO;
import entities.Phone;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PhonesResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Phone p1, p2, p3;
    private static List<Phone> phoneList = new ArrayList();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Phone("58719251", "Home");
        p2 = new Phone("01579125", "Work");
        p3 = new Phone("78519251", "Cell");
        phoneList.clear();
        phoneList.add(p1);
        phoneList.add(p2);
        phoneList.add(p3);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

     private static void emptyDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        emptyDatabase();
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/phones").then().statusCode(200);
    }

    @Test
    public void testAddressCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/phones/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(phoneList.size()));
    }
    
    @Test
    public void testGetAllPhones() throws Exception {
        PhonesDTO dbList = given()
                .contentType("application/json")
                .get("/phones/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().as(PhonesDTO.class);

        for (Phone phone : phoneList) {
            boolean matchingIdFound = false;
            for (PhoneDTO dbPhone : dbList.getPhones()) {
                if (Objects.equals(phone.getNumber(), dbPhone.getNumber())) {
                    assertTrue(dbPhone.getDescription().equals(phone.getDescription()));
                    assertTrue(dbPhone.getNumber().equals(phone.getNumber()));
                    matchingIdFound = true;
                    break;
                }
            }
            assertTrue(matchingIdFound);
        }
    }
    
    @Test 
    public void testAddPhone() throws Exception {
        Phone newPhone = new Phone("12562162", "Home");
        PhoneDTO phoneDTO = new PhoneDTO(newPhone);
        
        given()
                .contentType("application/json").body(phoneDTO)
                .when().post("/phones/add/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        EntityManager em = emf.createEntityManager();
        try {
            Phone dbResult = em.createQuery("Select p from Phone p WHERE p.number = :number", Phone.class)
                    .setParameter("number", (phoneDTO.getNumber()))
                    .getSingleResult();
            assertEquals(phoneDTO.getNumber(), dbResult.getNumber());
            assertEquals(phoneDTO.getDescription(), dbResult.getDescription());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }
}