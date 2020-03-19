package facades;

import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
import dto.PersonsDTO;
import entities.CityInfo;
import entities.Person;
import exceptions.InvalidInputException;
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
    
    // Create a city
    public CityInfoDTO addCityInfo(CityInfoDTO cityDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CityInfo newCity = new CityInfo(cityDTO.getCityName(), cityDTO.getZipCode());
            em.persist(newCity);
            em.getTransaction().commit();
            CityInfoDTO result = new CityInfoDTO(newCity);
            return result;
        } finally {
            em.close();
        }
    }

    // Get amount of cities in database
    public long getCityCount() {
        EntityManager em = getEntityManager();
        try {
            long cityCount = (long) em.createQuery("SELECT COUNT(c) FROM CityInfo c").getSingleResult();
            return cityCount;
        } finally {
            em.close();
        }
    }

    // Get all the cities in the database, with details
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

    // Get a city by it's zip code
    public CityInfoDTO getCityInfoByZipCode(String zip) {
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

    // Get all persons living in a given city
    public PersonsDTO getPersonsFromCity(String zip) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN p.address a WHERE a.cityInfo.zipCode = :zipCode", Person.class);
            tq.setParameter("zipCode", zip);
            List<Person> persons = tq.getResultList();
            PersonsDTO result = new PersonsDTO(persons);
            return result;
        } finally {
            em.close();
        }
    }
}