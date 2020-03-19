package facades;

import dto.AddressDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import exceptions.InvalidInputException;
import java.util.ArrayList;
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

public class AddressFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressFacade addressFacade;
    private static CityInfoFacade cityInfoFacade;
    private static CityInfo city1, city2, city3;
    private static Address a1, a2, a3;
    private static List<CityInfo> cityInfoList = new ArrayList();
    private static List<Address> addressList = new ArrayList();

    public AddressFacadeTest() {
    }

    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        addressFacade = AddressFacade.getAddressFacade(emf);
        cityInfoFacade = CityInfoFacade.getCityInfoFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        addressFacade = AddressFacade.getAddressFacade(emf);
        cityInfoFacade = CityInfoFacade.getCityInfoFacade(emf);
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
            city2 = new CityInfo("3030", "Capital");
            city3 = new CityInfo("3030", "Capital");
            a1 = new Address("Home", city1);
            a2 = new Address("Home", city2);
            a3 = new Address("Home", city3);
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
        long result = addressFacade.getAddressCount();
        int expectedResult = addressList.size();
        assertEquals(3, result);
    }

    @Test
    public void testAddAddress() throws InvalidInputException {
        CityInfo testCity = new CityInfo ("20000", "Missouri");
        Address testAddress = new Address("Missouri Street", testCity);
        AddressDTO result = addressFacade.addAddress(testAddress);
        assertEquals(testCity, result.getCityInfo());
        assertTrue(testAddress.getStreet().equals(result.getStreet()));
    }
}