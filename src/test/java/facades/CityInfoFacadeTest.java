package facades;

//@Disabled
import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.enterprise.inject.New;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

public class CityInfoFacadeTest {
    
    private static EntityManagerFactory emf;
    private static CityInfoFacade facade;
    private static PersonFacade facade2;
    private static CityInfo city1, city2, city3, city4;
    private static Address address1, address2, address3, address4;
    private static Person person1, person2, person3, person4, person5, person6;
    private static List<Person> personList = new ArrayList();
    private static CityInfo[] cityArray;
    
    public CityInfoFacadeTest() {
    }
    
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CityInfoFacade.getCityInfoFacade(emf);
        facade2 = PersonFacade.getPersonFacade(emf);
    }
    
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CityInfoFacade.getCityInfoFacade(emf);
        facade2 = PersonFacade.getPersonFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
        emptyDatabase();
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            city1 = new CityInfo("2900", "Hellerup");
            city2 = new CityInfo("2800", "Lyngby");
            city3 = new CityInfo("2840", "Holte");
            city4 = new CityInfo("2730", "Herlev");
            address1 = new Address("Hidden Street", city2);
            address2 = new Address("Secret Street", city3);
            address3 = new Address("Stealth Street", city4);
            address4 = new Address("Gone Street", city4);
            person1 = new Person("Jens", "Jensen", "Jens@Jensen.dk", address1);
            person2 = new Person("Mogens", "Mogensen", "Mogens@Mogensen.dk", address2);
            person3 = new Person("Palle", "Pallesen", "Palle@Pallesen.dk", address1);
            person4 = new Person("Svend", "Svendsen", "Svend@Svendsen.dk", address3);
            person5 = new Person("Kalle", "Kallesen", "Kalle@Kallesen.dk", address3);
            person6 = new Person("Jon", "Joensen", "Jon@Joensen.dk", address3);
            
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
        cityArray = new CityInfo[]{city1, city2, city3, city4};
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);
        personList.add(person5);
        personList.add(person6);
    }
    
    private static void emptyDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
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
    public void testCityInfoFacade() {
        long result = facade.getCityCount();
        int expectedResult = cityArray.length;
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetAllCities() {
        CitiesInfoDTO result = facade.getAllCityInfoes();
        
        int expectedResultSize = cityArray.length;
        assertEquals(expectedResultSize, result.getCitiesInfo().size());
        
        for (CityInfoDTO cityInfoDTO : result.getCitiesInfo()) {
            String zip = cityInfoDTO.getZipCode();
            String name = cityInfoDTO.getCityName();
            boolean matchFound = false;
            for (CityInfo cityInfo : cityArray) {
                boolean matchingZip = zip.equals(cityInfo.getZipCode());
                boolean matchingName = name.equals(cityInfo.getCity());
                if (matchingName && matchingZip) {
                    matchFound = true;
                    break;
                }
            }
            assertTrue(matchFound);
        }
    }
    
    @Test
    public void testGetPersonsFromCityWhereThereIsOnlyOneMatch() {
        
        Person expectedPerson = person2;
        CityInfo expectedCity = expectedPerson.getAddress().getCityInfo();
        Long searchCityID = expectedCity.getId();
        
        PersonsDTO result = facade.getPersonsFromCity(searchCityID);
        
        int expectedResultSize = 1;
        assertEquals(expectedResultSize, result.getPersons().size());
        
        PersonDTO resultPerson = result.getPersons().get(expectedResultSize - 1);
        assertEquals(expectedPerson.getId(), resultPerson.getId());
        
    }
    
    @Test
    public void testGetPersonsFromCityWithMultipleMatches() {
        
        CityInfo commonExpectedCity = person5.getAddress().getCityInfo();
        List<Person> expectedResult = new ArrayList(Arrays.asList(new Person[]{person4, person5, person6}));
        Long searchCityId = commonExpectedCity.getId();
        PersonsDTO result = facade.getPersonsFromCity(searchCityId);
        
        int expectedResultSize = expectedResult.size();
        assertEquals(expectedResultSize, result.getPersons().size());
        
        for (Person person : expectedResult) {
            
            boolean foundMatch = false;
            
            for (PersonDTO personDTO : result.getPersons()) {
                if (Objects.equals(person.getId(), personDTO.getId())) {
                    foundMatch = true;
                    assertTrue(person.getFirstName().equals(personDTO.getFirstName()));
                    assertTrue(person.getLastName().equals(personDTO.getLastName()));
                    assertTrue(person.getEmail().equals(personDTO.getEmail()));
                    break;
                }
            }
            assertTrue(foundMatch);
        }
        
    }
    
}
