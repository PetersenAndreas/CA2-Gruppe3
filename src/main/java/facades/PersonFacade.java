package facades;

import entities.Hobby;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import entities.Person;
import java.util.List;
import javax.persistence.TypedQuery;

public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    private PersonFacade() {}
    
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public long getPersonCount(){
        EntityManager em = getEntityManager();
        try{
            long personCount = (long)em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        }finally{  
            em.close();
        }
    }
    
    // "Get all persons with a given hobby"
    public PersonDTO getPersonByHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.hobbies = :hobbies", Person.class);
            tq.setParameter("hobbies", hobby);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }
    
    // "Get all persons living in a given city (i.e. 2800 Lyngby)"
    public PersonDTO getPersonByHobby(City city) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.city = :city", Person.class);
            tq.setParameter("city", city);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }
    
    public PersonDTO getPersonCountByHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try{
            long personCount = (long)em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        }finally{  
            em.close();
        }
    }
}