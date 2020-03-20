package rest;

import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import facades.CityInfoFacade;
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
import static rest.PersonResourceTest.startServer;
import utils.EMF_Creator;

public class CitiesResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static CityInfoFacade FACADE;
    private static CityInfo city1, city2, city3, city4;
    private static Address address1, address2, address3;
    private static Person person1, person2, person3, person4, person5, person6;
    private static List<Person> personList = new ArrayList();
    private static List<CityInfo> cityList = new ArrayList();

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
        FACADE = CityInfoFacade.getCityInfoFacade(emf);
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
            city1 = new CityInfo("2900", "Hellerup");
            city2 = new CityInfo("2800", "Lyngby");
            city3 = new CityInfo("2840", "Holte");
            city4 = new CityInfo("2730", "Herlev");
            address1 = new Address("Hidden Street", city2);
            address2 = new Address("Secret Street", city3);
            address3 = new Address("Stealth Street", city4);
            person1 = new Person("Jens", "Jensen", "Jens@Jensen.dk", address1);
            person2 = new Person("Mogens", "Mogensen", "Mogens@Mogensen.dk", address2);
            person3 = new Person("Palle", "Pallesen", "Palle@Pallesen.dk", address1);
            person4 = new Person("Svend", "Svendsen", "Svend@Svendsen.dk", address3);
            person5 = new Person("Kalle", "Kallesen", "Kalle@Kallesen.dk", address3);
            person6 = new Person("Jon", "Joensen", "Jon@Joensen.dk", address3);
            cityList.clear();
            cityList.add(city1);
            cityList.add(city2);
            cityList.add(city3);
            cityList.add(city4);
            personList.clear();
            personList.add(person1);
            personList.add(person2);
            personList.add(person3);
            personList.add(person4);
            personList.add(person5);
            personList.add(person6);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(city1);
            em.persist(city2);
            em.persist(city3);
            em.persist(city4);
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(person4);
            em.persist(person5);
            em.persist(person6);
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
        given().when().get("/cities").then().statusCode(200);
    }
    
    @Test
    public void testCityCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/cities/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(cityList.size()));
    }

    @Test
    public void testGetAllCities() {
        int listOfCitiesInfo = cityList.size();
        CitiesInfoDTO dbList = given().when()
                .contentType("application/json")
                .get("cities")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().as(CitiesInfoDTO.class);
        assertEquals(listOfCitiesInfo, dbList.getCitiesInfo().size());
        
        for (CityInfo city : cityList) {
            boolean matchingIdFound = false;
            for (CityInfoDTO dbCity : dbList.getCitiesInfo()) {
                if (Objects.equals(city.getCity(), dbCity.getCityName())) {
                    assertTrue(city.getZipCode().equals(dbCity.getZipCode()));
                    matchingIdFound = true;
                    break;
                }
            }
            assertTrue(matchingIdFound);
        }
    }
    
    @Test 
    public void testAddCity() throws Exception {
        CityInfo newCity = new CityInfo("2590", "Berlin");
        CityInfoDTO cityDTO = new CityInfoDTO(newCity);
        
        given()
                .contentType("application/json").body(cityDTO)
                .when().post("/cities/add/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
        EntityManager em = emf.createEntityManager();
        try {
            CityInfo dbResult = em.createQuery("Select c from CityInfo c WHERE c.zipCode = :zip", CityInfo.class)
                    .setParameter("zip", (cityDTO.getZipCode()))
                    .getSingleResult();

            assertEquals(cityDTO.getCityName(), dbResult.getCity());
            assertEquals(cityDTO.getZipCode(), dbResult.getZipCode());
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetPersonsFromCity() {
        CityInfo expectedCity = city4;
        person1.addAddressToPerson(address3);
        String expectedCityZip = expectedCity.getZipCode();
        PersonsDTO dbList = FACADE.getPersonsFromCity(expectedCityZip);

        PersonsDTO result = given().when()
                .contentType("application/json")
                .get("/cities/" + expectedCityZip)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().as(PersonsDTO.class);

        for (PersonDTO personDB : dbList.getPersons()) {
            boolean matchingIDFound = false;
            for (PersonDTO person : result.getPersons()) {
                if (Objects.equals(personDB.getId(), person.getId())) {
                    assertTrue(person.getFirstName().equals(personDB.getFirstName()));
                    assertTrue(person.getLastName().equals(personDB.getLastName()));
                    assertTrue(person.getEmail().equals(personDB.getEmail()));
                    matchingIDFound = true;
                    break;
                }
            }
            assertTrue(matchingIDFound);
        }
    }
    
    @Test
    public void testGetCityInfoByZipCode() {
        //TODO
    }
}