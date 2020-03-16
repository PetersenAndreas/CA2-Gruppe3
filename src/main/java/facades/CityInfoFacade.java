package facades;

import dto.CitiesInfoDTO;
import dto.PersonsDTO;
import entities.CityInfo;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class CityInfoFacade {

    private static CityInfoFacade instance;
    private static EntityManagerFactory emf;

    private CityInfoFacade() {
    }

    public static CityInfoFacade getCityInfoFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public long getCityCount() {
        EntityManager em = getEntityManager();
        try {
            long cityCount = (long) em.createQuery("SELECT COUNT(c) FROM CityInfo c").getSingleResult();
            return cityCount;
        } finally {
            em.close();
        }
    }

    public CitiesInfoDTO getAllCitiInfoes() {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<CityInfo> query = em.createQuery("select c from CityInfo c", CityInfo.class);
            List<CityInfo> dbList = query.getResultList();
            CitiesInfoDTO result = new CitiesInfoDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }

}
