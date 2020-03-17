package facades;

import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
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

    public CitiesInfoDTO getAllCityInfoes() {

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

    public CityInfoDTO getCitiesInfoByZipCode(CityInfo zip) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<CityInfo> tq = em.createQuery("SELECT c FROM CityInfo c WHERE c.zipCode = :zipCode", CityInfo.class);
            tq.setParameter("zipCode", zip);
            CityInfo info = tq.getSingleResult();
            CityInfoDTO result = new CityInfoDTO(info);
            return result;
        } finally {
            em.close();
        }
    }

    public PersonsDTO getPersonsFromCity(Long id) {

        EntityManager em = emf.createEntityManager();

        try {

            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN p.address a WHERE a.cityInfo.id = :id",
                     Person.class);
            tq.setParameter("id", id);
            List<Person> persons = tq.getResultList();
            PersonsDTO result = new PersonsDTO(persons);
            return result;

        } finally {
            em.close();
        }

    }

}
