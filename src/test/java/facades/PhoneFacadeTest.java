package facades;

import entities.Phone;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class PhoneFacadeTest {

    private static EntityManagerFactory emf;
    private static PhoneFacade facade;
    private static Phone phone1, phone2, phone3, phone4;
    private static Phone[] phoneArray;

    public PhoneFacadeTest() {}

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = PhoneFacade.getPhoneFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = PhoneFacade.getPhoneFacade(emf);
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
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            phone1 = new Phone("68170285", "Michaels telefonnummer");
            phone2 = new Phone("32719822", "Andreas' telefonnummer");
            phone3 = new Phone("11785381", "Cahits telefonnummer");
            phone4 = new Phone("28774631", "Marcus' telefonnummer");
            em.persist(phone1);
            em.persist(phone2);
            em.persist(phone3);
            em.persist(phone4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        phoneArray = new Phone[]{phone1, phone2, phone3, phone4};
    }
    
    private static void emptyDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
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
    public void testPhoneFacade() {
        long result = facade.getPhoneCount();
        int expectedResult = phoneArray.length;
        assertEquals(expectedResult, result);
    }
}