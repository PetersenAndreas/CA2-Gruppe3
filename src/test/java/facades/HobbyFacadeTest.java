package facades;

import dto.HobbyDTO;
import entities.Hobby;
import exceptions.InvalidInputException;
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

public class HobbyFacadeTest {

    private static EntityManagerFactory emf;
    private static HobbyFacade facade;
    private static Hobby hobby1, hobby2, hobby3, hobby4;
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
        facade = HobbyFacade.getHobbyFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = HobbyFacade.getHobbyFacade(emf);
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
            em.persist(hobby1);
            em.persist(hobby2);
            em.persist(hobby3);
            em.persist(hobby4);
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
        long result = facade.getHobbyCount();
        int expectedResult = hobbyArray.length;
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testAddHobby() throws InvalidInputException {
        Hobby testHobby = new Hobby("Video games", "Spending time wisely");
        Long expectedId = highestID + 1;
        HobbyDTO testHobbyDTO = new HobbyDTO(testHobby);
        HobbyDTO result = facade.addHobby(testHobbyDTO);
//        assertEquals(expectedId, testHobby.getId());
//        assertTrue(result.getName().equals(testHobby.getName()));
    }
}