package facades;

import dto.HobbiesDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Hobby;
import entities.Person;
import exceptions.InvalidInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

public class HobbyFacadeTest {

    private static EntityManagerFactory emf;
    private static HobbyFacade FACADE;
    private static Hobby hobby1, hobby2, hobby3, hobby4;
    private static Person person1, person2, person3, person4;
    private static Hobby[] hobbyArray;
    private static long highestID;

    public HobbyFacadeTest() {}

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = HobbyFacade.getHobbyFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       FACADE = HobbyFacade.getHobbyFacade(emf);
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
            hobby1 = new Hobby("Ishockey", "holdsport på is med en puck og en stav");
            hobby2 = new Hobby("Basketball", "holdsport med to kurve og en orange bold");
            hobby3 = new Hobby("Fodbold", "holdsport med to mål og en rigtig bold");
            hobby4 = new Hobby("MMA", "enmandssport der handler om at tæve modstanderen");
            person1 = new Person("Jimmy", "Nielsen", "Nielsen@gmail.com");
            person2 = new Person("Johnny", "Jensen", "Jensen@gmail.com");
            person3 = new Person("Jim", "Olsen", "Olsen@gmail.com");
            person4 = new Person("John", "Kristensen", "Kristensen@gmail.com");
            person1.addHobbyToPerson(hobby1);
            person1.addHobbyToPerson(hobby4);
            person2.addHobbyToPerson(hobby2);
            person3.addHobbyToPerson(hobby3);
            person3.addHobbyToPerson(hobby2);
            person4.addHobbyToPerson(hobby1);
            em.persist(hobby1);
            em.persist(hobby2);
            em.persist(hobby3);
            em.persist(hobby4);
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(person4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        hobbyArray = new Hobby[]{hobby1, hobby2, hobby3, hobby4};
        
        highestID = 0L;
        for (Hobby hobby : hobbyArray) {
            if (hobby.getId() > highestID) {
                highestID = hobby.getId();
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
    public void testHobbyFacade() {
        long result = FACADE.getHobbyCount();
        int expectedResult = hobbyArray.length;
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetAllHobbies() {
        HobbiesDTO result = FACADE.getAllHobbies();
        int expectedResult = hobbyArray.length;
        assertEquals(result.getHobbies().size(), expectedResult);
    }
    
    @Test
    public void testAddHobby() throws InvalidInputException {
        HobbyDTO testHobby = new HobbyDTO("Video games", "Spending time wisely");
        HobbyDTO result = FACADE.addHobby(testHobby);
        assertEquals(result.getName(), testHobby.getName());
        assertEquals(result.getDescription(), testHobby.getDescription());
        
        EntityManager em = emf.createEntityManager();
        try {
            List<Hobby> dbResult = em.createQuery("Select h FROM Hobby h", Hobby.class).getResultList();
            assertEquals(dbResult.size(), hobbyArray.length + 1);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testGetPersonCountByHobby() {
        Hobby expectedHobby = hobby1;
        String searchHobby = expectedHobby.getName();
        Long result = FACADE.getPersonCountByHobby(searchHobby);
        Long expectedPersonCount = (long)expectedHobby.getPersons().size();
        assertTrue(result.equals(expectedPersonCount));
    }
    
    @Test
    public void testGetPersonsByHobby() {
        List<Person> expectedPersons = new ArrayList(Arrays.asList(new Person[]{person2, person3}));
        Hobby expectedHobby = hobby2;
        
        String searchHobby = expectedHobby.getName();
        PersonsDTO result = FACADE.getPersonsByHobby(searchHobby);
        
        for (PersonDTO personDTO : result.getPersons()) {
            String fName = personDTO.getFirstName();
            String lName = personDTO.getLastName();
            String email = personDTO.getEmail();
            boolean matchFound = false;
            for (Person person : expectedPersons) {
                boolean matchingFirstName = fName.equals(person.getFirstName());
                boolean matchingLastName = lName.equals(person.getLastName());
                boolean matchingEmail = email.equals(person.getEmail());
                if (matchingFirstName && matchingLastName && matchingEmail) {
                    matchFound = true;
                    break;
                }
            }
            assertTrue(matchFound);
        }
    }
}