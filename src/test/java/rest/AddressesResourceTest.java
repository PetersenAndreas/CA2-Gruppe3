package rest;

import dto.AddressDTO;
import dto.AddressesDTO;
import entities.Address;
import entities.CityInfo;
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

public class AddressesResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Address a1, a2, a3;
    private static CityInfo city1, city2, city3;
    private static List<Address> addressList = new ArrayList();

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
        city1 = new CityInfo("2660", "Brondby Strand");
        city2 = new CityInfo("3030", "Capital");
        city3 = new CityInfo("8210", "Aarhus");
        a1 = new Address("Strandvejen", city1);
        a2 = new Address("Jensensgade", city2);
        a3 = new Address("Cahitsvej", city3);
        addressList.clear();
        addressList.add(a1);
        addressList.add(a2);
        addressList.add(a3);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.persist(city1);
            em.persist(city2);
            em.persist(city3);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
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
        given().when().get("/addresses").then().statusCode(200);
    }

    @Test
    public void testAddressCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/addresses/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(addressList.size()));
    }
    
    @Test
    public void testGetAllAddresses() throws Exception {
        AddressesDTO dbList = given()
                .contentType("application/json")
                .get("/addresses/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().as(AddressesDTO.class);

        for (Address address : addressList) {
            boolean matchingIdFound = false;
            for (AddressDTO dbAddress : dbList.getAddresses()) {
                if (Objects.equals(address.getStreet(), dbAddress.getStreet())) {
                    assertTrue(dbAddress.getCityInfo().getCityName().equals(address.getCityInfo().getCity()));
                    assertTrue(dbAddress.getCityInfo().getZipCode().equals(address.getCityInfo().getZipCode()));
                    matchingIdFound = true;
                    break;
                }
            }
            assertTrue(matchingIdFound);
        }
    }
    
    @Test 
    public void testAddAddress() throws Exception {
        CityInfo testCity = city2;
        Address newAddress = new Address("Michaelsvej", testCity);
        AddressDTO addressDTO = new AddressDTO(newAddress);
        
        given()
                .contentType("application/json").body(addressDTO)
                .when().post("/addresses/add/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        EntityManager em = emf.createEntityManager();
        try {
            Address dbResult = em.createQuery("Select a from Address a WHERE a.street = :street", Address.class)
                    .setParameter("street", (addressDTO.getStreet()))
                    .getSingleResult();

            assertEquals(addressDTO.getStreet(), dbResult.getStreet());
            assertEquals(addressDTO.getCityInfo().getCityName(), dbResult.getCityInfo().getCity());
            assertEquals(addressDTO.getCityInfo().getZipCode(), dbResult.getCityInfo().getZipCode());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }
}