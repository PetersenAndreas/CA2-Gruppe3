package facades;

import dto.HobbyDTO;
import dto.PersonDTO;
import entities.Hobby;
import entities.Person;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade personFacade;
    private static HobbyFacade hobbyFacade;
    private static Person person1, person2, person3, person4;
    private static Person[] personArray;

    public PersonFacadeTest() {}

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        personFacade = PersonFacade.getPersonFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       personFacade = PersonFacade.getPersonFacade(emf);
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
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(person4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        personArray = new Person[]{person1, person2, person3, person4};
    }
    
    private static void emptyDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
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
        long result = personFacade.getPersonCount();
        int expectedResult = personArray.length;
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testAddPerson() {
        Person testPerson = new Person("Muhammad", "Ali", "Champ@gmail.com");
        PersonDTO result = personFacade.addPerson(testPerson);
        assertEquals(testPerson.getId(), result.getId());
        assertTrue(testPerson.getFirstName().equals(result.getFirstName()));
    }
}