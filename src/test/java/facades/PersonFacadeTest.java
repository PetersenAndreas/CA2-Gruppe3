package facades;

import dto.PersonDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.InvalidInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person person1, person2, person3, person4;
    private static CityInfo city1;
    private static Address address1;
    private static Hobby hobby1, hobby2, hobby3;
    private static Person[] personArray;
    private static Long highestId;

    public PersonFacadeTest() {
    }

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
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
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            person1 = new Person("Michael", "Korsgaard", "MichaelKorsgaard@gmail.com");
            person2 = new Person("Andreas", "Petersen", "AndreasPetersen@gmail.com");
            person3 = new Person("Cahit", "Bakirci", "CahitBakirci@gmail.com");
            person4 = new Person("Marcus", "Johnsen", "MarcusJohnsen@gmail.com");
            city1 = new CityInfo("3030", "Capital");
            address1 = new Address("Home", city1);
            hobby1 = new Hobby("Chess", "Playing Chess");
            hobby2 = new Hobby("Sleeping", "The act of making no hobby into a hobby");
            hobby3 = new Hobby("Gaming", "Socialising by isolating");
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(person4);
            em.persist(city1);
            em.persist(hobby1);
            em.persist(hobby2);
            em.persist(hobby3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        personArray = new Person[]{person1, person2, person3, person4};

        highestId = 0L;
        for (Person person : personArray) {
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
    public void testPersonFacade() {
        long result = facade.getPersonCount();
        int expectedResult = personArray.length;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddPerson() throws InvalidInputException {
        Person testPerson = new Person("Muhammad", "Ali", "Champ@gmail.com");
        testPerson.addAddressToPerson(address1);
        Long expectedId = highestId + 1;
        PersonDTO testPersonDTO = new PersonDTO(testPerson);
        PersonDTO result = facade.addPerson(testPersonDTO);
        assertEquals(expectedId, result.getId());
        assertTrue(testPerson.getFirstName().equals(result.getFirstName()));
    }

    @Test
    public void editPerson() throws InvalidInputException {
        Person expectedPerson = person1;
        Address expectedAddress = address1;
        List<Hobby> expectedHobbies = new ArrayList(Arrays.asList(new Hobby[]{hobby1, hobby2}));
        PersonDTO expectedResult = new PersonDTO(expectedPerson);
        expectedResult.setFirstName("Nicolaj");
        expectedResult.setLastName("Jackson");
        expectedResult.setEmail("NJ@Gmail.com");
        expectedResult.getPhones().getPhones().clear();
        expectedResult.getPhones().getPhones().add(new PhoneDTO("11223344", "Mobile"));
        expectedResult.getPhones().getPhones().add(new PhoneDTO("12312312", "Home"));
        expectedResult.getPhones().getPhones().add(new PhoneDTO("19213231", "Work"));
        expectedResult.setStreet(expectedAddress.getStreet());
        expectedResult.getHobbies().clear();
        for (Hobby expectedHobby : expectedHobbies) {
            expectedResult.getHobbies().add(expectedHobby.getName());
        }

        facade.editPerson(expectedResult, expectedResult.getId());

        EntityManager em = emf.createEntityManager();
        try {
            Person dbResult = em.createQuery("Select p From Person p where p.id = :id", Person.class)
                    .setParameter("id", expectedResult.getId())
                    .getSingleResult();

            assertEquals(expectedResult.getFirstName(), dbResult.getFirstName());
            assertEquals(expectedResult.getLastName(), dbResult.getLastName());
            assertEquals(expectedResult.getEmail(), dbResult.getEmail());
            assertEquals(expectedResult.getPhones().getPhones().size(), dbResult.getPhones().size());
            
            //Assert on Phones
            for (Phone dbPhone : dbResult.getPhones()) {
                boolean matchFound = false;
                for (PhoneDTO phoneDTO : expectedResult.getPhones().getPhones()) {
                    if (Objects.equals(dbPhone.getNumber(), phoneDTO.getNumber())
                            && Objects.equals(dbPhone.getDescription(), phoneDTO.getDescription())) {
                        matchFound = true;
                        break;
                    }
                }
                assertTrue(matchFound);
            }
            
            //Assert on Address
            assertEquals(expectedResult.getStreet(), dbResult.getAddress().getStreet());
            assertEquals(expectedAddress, dbResult.getAddress());
            
            //Assert on Hobbies
            assertEquals(expectedHobbies.size(), dbResult.getHobbies().size());
            for (Hobby dbHobby : dbResult.getHobbies()) {
                boolean matchFound = false;
                for (Hobby expectedHobby : expectedHobbies) {
                    if(Objects.equals(dbHobby.getId(), expectedHobby.getId())){
                        assertEquals(expectedHobby.getName(), dbHobby.getName());
                        assertEquals(expectedHobby.getDescription(), dbHobby.getDescription());
                        matchFound = true;
                        break;
                    }
                }
                assertTrue(matchFound);
            }

        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }

    }
}
