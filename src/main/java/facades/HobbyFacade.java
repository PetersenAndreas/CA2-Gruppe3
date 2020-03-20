package facades;

import dto.HobbiesDTO;
import dto.HobbyDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.InvalidInputException;
import entities.Hobby;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;
    
    private HobbyFacade() {}
    
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    // Create a hobby
    public HobbyDTO addHobby(HobbyDTO hobbyDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Hobby newHobby = new Hobby(hobbyDTO.getName(), hobbyDTO.getDescription());
            em.persist(newHobby);
            em.getTransaction().commit();
            HobbyDTO result = new HobbyDTO(newHobby);
            return result;
        } finally {
            em.close();
        }
    }
    
    // Get amount of hobbies in database
    public long getHobbyCount(){
        EntityManager em = getEntityManager();
        try{
            long hobbyCount = (long)em.createQuery("SELECT COUNT(h) FROM Hobby h").getSingleResult();
            return hobbyCount;
        }finally{  
            em.close();
        }
    }
    
    // Get all hobbies in database, with details
    public HobbiesDTO getAllHobbies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
            List<Hobby> dbList = query.getResultList();
            HobbiesDTO result = new HobbiesDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }
    
    // Get the count of people with a given hobby
    public long getPersonCountByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try{
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p Join p.hobbies h WHERE h.name = :hobbies").setParameter("hobbies", hobby).getSingleResult();
            return personCount;
        }finally{  
            em.close();
        }
    }
    
    // Get all persons with a given hobby
    public PersonsDTO getPersonsByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :hobbies", Person.class);
            tq.setParameter("hobbies", hobby);
            List<Person> resultList = tq.getResultList();
            PersonsDTO result = new PersonsDTO(resultList);
            return result;
        } finally {
            em.close();
        }
    }
}