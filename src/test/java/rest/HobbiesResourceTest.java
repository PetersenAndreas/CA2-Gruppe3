package rest;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Hobby;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class HobbiesResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Hobby h1,h2;
    private static Person p1,p2;
    private static List<Person> personList = new ArrayList();
    
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
    public static void closeTestServer(){
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        h1 = new Hobby("Hockey", "ice, stick and puck");
        h2 = new Hobby("Football", "the real European kind");
        p1 = new Person("Alexander", "Ovechkin", "Capitals@gmail.com");
        p2 = new Person("Bruno", "Fernandes", "United@gmail.com");
        p1.addHobbyToPerson(h1);
        p2.addHobbyToPerson(h2);
        personList.add(p1);
        personList.add(p2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(h1);
            em.persist(h2); 
            em.persist(p1); 
            em.persist(p2); 
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/hobbies").then().statusCode(200);
    }
   
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/hobbies/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Hello World"));   
    }
    
    @Test
    public void testHobbyCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/hobbies/count/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(2));   
    }
    
    @Test
    public void testGetPersonsCountFromHobby() throws Exception {
        given()
        .contentType("application/json")
        .get("/hobbies/countbyhobby/Hockey/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(1)); 
    }
    
    @Test
    public void testGetPersonsCountFromHobbyNoPersonMatch() throws Exception {
        given()
        .contentType("application/json")
        .get("/hobbies/countbyhobby/a").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(0)); 
    }
    
    @Test
    public void testGetPersonsFromHobby() throws Exception {
        Person expectedPerson = p2;
        PersonsDTO dbList = given()
        .contentType("application/json")
        .get("/hobbies/person/Football/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .extract().body().as(PersonsDTO.class);
        
        boolean matchingIdFound = false;
        for (PersonDTO dbPerson : dbList.getPersons()) {
            if (Objects.equals(expectedPerson.getId(), dbPerson.getId())) {
                assertTrue (dbPerson.getFirstName().equals(expectedPerson.getFirstName()));
                assertTrue (dbPerson.getLastName().equals(expectedPerson.getLastName()));
                matchingIdFound = true;
                break;
            }
        }
        assertTrue(matchingIdFound);
    }
    
    @Test
    public void testGetPersonsFromHobbyNoPerson() throws Exception {
        Person expectedPerson = p1;
        PersonsDTO dbList = given()
        .contentType("application/json")
        .get("/hobbies/person/Skipping/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .extract().body().as(PersonsDTO.class);
        
        boolean matchingIdFound = false;
        for (PersonDTO dbPerson : dbList.getPersons()) {
            if (Objects.equals(expectedPerson.getId(), dbPerson.getId())) {
                assertTrue (dbPerson.getFirstName().equals(expectedPerson.getFirstName()));
                assertTrue (dbPerson.getLastName().equals(expectedPerson.getLastName()));
                matchingIdFound = true;
                break;
            }
        }
        assertFalse(matchingIdFound);
    }
}