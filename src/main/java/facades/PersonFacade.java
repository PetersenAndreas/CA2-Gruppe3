package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import entities.Person;
import entities.Phone;
import exceptions.NoResultFoundException;
import java.util.List;
import javax.persistence.NoResultException;
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
    
    // "Get all persons living in a given city (i.e. 2800 Lyngby)"
    public PersonDTO getPersonsByCity(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.address = :address", Person.class);
            tq.setParameter("address", address);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
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
    
    // "Get all persons with a given hobby"
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
    
    // Get information about a person (address, hobbies etc) given a phone number
    public PersonDTO getPersonInformationByPhone(Phone phone) {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.phones = :phones", Person.class);
            tq.setParameter("phones", phone);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        }finally{  
            em.close();
        }    
    }
    
    // Create a Person (with hobbies, phone, address etc.)
    public PersonDTO addPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }
    
    //Get all persons
    public PersonsDTO getAllPersons(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> dbList = query.getResultList();
            PersonsDTO result = new PersonsDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }
    
    // Get person with a given ID
    public PersonDTO getPersonById(Long id) throws NoResultFoundException{
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class);
            tq.setParameter("id", id);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } catch(NoResultException e) {
            throw new NoResultFoundException("No person with given ID");
        }finally{  
            em.close();
        }    
    }
}