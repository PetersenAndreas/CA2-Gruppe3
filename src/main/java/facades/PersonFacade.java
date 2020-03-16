package facades;

import dto.PersonDTO;
import entities.Address;
import entities.Hobby;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import entities.Person;
import entities.Phone;
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
    public PersonDTO getPersonsByHobby(Hobby hobby) {
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
    public PersonDTO getPersonCountByHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Person> tq = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.hobbies = :hobbies", Person.class);
            tq.setParameter("hobbies", hobby);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        }finally{  
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
}