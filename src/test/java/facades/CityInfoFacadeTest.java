package facades;

//@Disabled

import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
import entities.CityInfo;
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
    private static CityInfo city1, city2, city3, city4;
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
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CityInfoFacade.getCityInfoFacade(emf);
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
            em.persist(city1);
            em.persist(city2);
            em.persist(city3);
            em.persist(city4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        cityArray = new CityInfo[]{city1, city2, city3, city4};
    }

    private static void emptyDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
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
    public void testGetAllCities(){
        CitiesInfoDTO result = facade.getAllCitiInfoes();
        
        int expectedResultSize = cityArray.length;
        assertEquals(expectedResultSize, result.getCitiesInfo().size());
        
        for (CityInfoDTO cityInfoDTO : result.getCitiesInfo()) {
            String zip = cityInfoDTO.getZipCode();
            String name = cityInfoDTO.getCityName();
            boolean matchFound = false;
            for (CityInfo cityInfo : cityArray) {
                boolean matchingZip = zip.equals(cityInfo.getZipCode());
                boolean matchingName = name.equals(cityInfo.getCity());
                if(matchingName && matchingZip){
                    matchFound = true;
                    break;
                }
            }
            assertTrue(matchFound);
        }
    }

}
