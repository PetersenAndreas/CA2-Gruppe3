package facades;

import dto.PhoneDTO;
import dto.PhonesDTO;
import entities.Phone;
import exceptions.InvalidInputException;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class PhoneFacadeTest {

    private static EntityManagerFactory emf;
    private static PhoneFacade FACADE;
    private static Phone phone1, phone2, phone3, phone4;
    private static Phone[] phoneArray;

    public PhoneFacadeTest() {}

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = PhoneFacade.getPhoneFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       FACADE = PhoneFacade.getPhoneFacade(emf);
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
        long result = FACADE.getPhoneCount();
        int expectedResult = phoneArray.length;
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetAllPhones() {
        PhonesDTO result = FACADE.getAllPhones();
        int expectedResult = phoneArray.length;
        assertEquals(result.getPhones().size(), expectedResult);
    }
    
    @Test
    public void testAddPhone() throws InvalidInputException {
        PhoneDTO testPhone = new PhoneDTO("12598125", "home");
        PhoneDTO result = FACADE.addPhone(testPhone);
        assertEquals(result.getNumber(), testPhone.getNumber());
        assertEquals(result.getDescription(), testPhone.getDescription());
        
        EntityManager em = emf.createEntityManager();
        try {
            List<Phone> dbResult = em.createQuery("Select p FROM Phone p", Phone.class).getResultList();
            assertEquals(dbResult.size(), phoneArray.length + 1);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }
}