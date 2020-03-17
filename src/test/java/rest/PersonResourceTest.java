package rest;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
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
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person r1, r2;
    private static Address a1;
    private static CityInfo city1;
    private static List<Person> personList = new ArrayList();
    private static Long highestId;

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
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

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
        r1 = new Person("Hans Hansen", "hans@hansen.dk", "55555555");
        r2 = new Person("Kurt Kurtsen", "kurt@kurtsen.com", "44444444");
        city1 = new CityInfo("3030", "Capital");
        a1 = new Address("Home", city1);
        personList.clear();
        personList.add(r1);
        personList.add(r2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(city1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        highestId = 0L;
        for (Person person : personList) {
            if (person.getId() > highestId) {
                highestId = person.getId();
            }
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
        given().when().get("/persons").then().statusCode(200);
    }

    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/persons/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    @Test
    public void testPersonCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/persons/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }

    @Test
    public void testPersonsAll() throws Exception {
        PersonsDTO dbList = given()
                .contentType("application/json")
                .get("/persons/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().as(PersonsDTO.class);

        for (Person person : personList) {
            boolean matchingIdFound = false;
            for (PersonDTO dbPerson : dbList.getPersons()) {
                if (Objects.equals(person.getId(), dbPerson.getId())) {
                    assertTrue(dbPerson.getFirstName().equals(person.getFirstName()));
                    assertTrue(dbPerson.getLastName().equals(person.getLastName()));
                    assertTrue(dbPerson.getEmail().equals(person.getEmail()));
                    matchingIdFound = true;
                    break;
                }
            }
            assertTrue(matchingIdFound);
        }
    }

    @Test
    public void testPersonOnId() throws Exception {
        int id = Math.toIntExact(r1.getId());
        given()
                .contentType("application/json")
                .get("/persons/" + id).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(id));
    }

    @Test
    public void testPersonEdit() throws Exception {

        Person expectedPerson = r1;
        Address expectedAddress = a1;
        String id = Long.toString(expectedPerson.getId());
        PersonDTO expectedResult = new PersonDTO(r1);
        expectedResult.setFirstName("Nicolaj");
        expectedResult.setLastName("Jackson");
        expectedResult.setEmail("NJ@Gmail.com");
        expectedResult.setStreet(a1.getStreet());

        given()
                .contentType("application/json").body(expectedResult)
                .when().put("/persons/edit/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());

        EntityManager em = emf.createEntityManager();
        try {
            Person dbResult = em.createQuery("Select p from Person p WHERE p.id = :id", Person.class)
                    .setParameter("id", expectedPerson.getId())
                    .getSingleResult();

            assertEquals(expectedResult.getFirstName(), dbResult.getFirstName());
            assertEquals(expectedResult.getLastName(), dbResult.getLastName());
            assertEquals(expectedResult.getEmail(), dbResult.getEmail());
            assertEquals(expectedAddress, dbResult.getAddress());
            assertTrue(dbResult.getPhones().isEmpty());
            assertTrue(dbResult.getHobbies().isEmpty());

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }

    }
}
