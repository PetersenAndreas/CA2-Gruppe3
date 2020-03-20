package facades;

import dto.AddressDTO;
import dto.CityInfoDTO;
import entities.Address;
import entities.CityInfo;
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

public class AddressFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressFacade FACADE;
    private static CityInfo city1, city2, city3;
    private static Address a1, a2, a3;
    private static Address[] addressArray;

    public AddressFacadeTest() {
    }

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = AddressFacade.getAddressFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = AddressFacade.getAddressFacade(emf);
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
            city1 = new CityInfo("3030", "Capital");
            city2 = new CityInfo("3020", "West City");
            city3 = new CityInfo("3010", "East City");
            a1 = new Address("Home", city1);
            a2 = new Address("Work", city2);
            a3 = new Address("Vacation", city3);
            em.persist(city1);
            em.persist(city2);
            em.persist(city3);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        addressArray = new Address[]{a1, a2, a3};
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
    public void testAddressFacade() {
        long result = FACADE.getAddressCount();
        int expectedResult = addressArray.length;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddAddress() throws InvalidInputException {
        CityInfoDTO testCity = new CityInfoDTO (city1);
        AddressDTO testAddress = new AddressDTO("Missouri Street", testCity);
        AddressDTO result = FACADE.addAddress(testAddress);
        assertEquals(testCity.getZipCode(), result.getCityInfo().getZipCode());
        assertEquals(testCity.getCityName(), result.getCityInfo().getCityName());
        assertEquals(testAddress.getStreet(), result.getStreet());
        
        EntityManager em = emf.createEntityManager();
        try {
            List<Address> dbResult = em.createQuery("Select a FROM Address a", Address.class).getResultList();
            assertEquals(dbResult.size(), addressArray.length + 1);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            em.close();
        }
    }
}