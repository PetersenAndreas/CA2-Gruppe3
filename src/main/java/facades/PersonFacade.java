package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Hobby;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import entities.Person;
import entities.Phone;
import exceptions.InvalidInputException;
import exceptions.NoResultFoundException;
import java.util.List;
import java.util.Objects;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

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

    public long getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
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
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT COUNT(p) FROM Person p WHERE p.hobbies = :hobbies", Person.class);
            tq.setParameter("hobbies", hobby);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }

    // Get information about a person (address, hobbies etc) given a phone number
    public PersonDTO getPersonInformationByPhone(Phone phone) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.phones = :phones", Person.class);
            tq.setParameter("phones", phone);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }

    // Create a Person (with hobbies, phone, address etc.)
    public PersonDTO addPerson(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        try {
            Person newPerson = new Person(person.getFirstName(), person.getLastName(), person.getEmail());
            
            em.getTransaction().begin();
            em.persist(newPerson);
            em.getTransaction().commit();
            PersonDTO result = new PersonDTO(newPerson);
            return result;
        } finally {
            em.close();
        }
    }

    //Get all persons
    public PersonsDTO getAllPersons() {
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
    public PersonDTO getPersonById(Long id) throws NoResultFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class);
            tq.setParameter("id", id);
            Person person = tq.getSingleResult();
            PersonDTO result = new PersonDTO(person);
            return result;
        } catch (NoResultException e) {
            throw new NoResultFoundException("No person with given ID");
        } finally {
            em.close();
        }
    }

    public void editPerson(PersonDTO p, Long id) throws InvalidInputException {
        validateInput(p);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Hobby> allHobbies = em.createQuery("SELECT h From Hobby h", Hobby.class).getResultList();
            List<Address> allAddresses = em.createQuery("SELECT a From Address a", Address.class).getResultList();
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class).setParameter("id", id);
            Person person = tq.getSingleResult();
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setEmail(p.getEmail());
            em.getTransaction().commit();
        } catch (NoResultException e) {
            throw new InvalidInputException("");
        } finally {
            em.close();
        }
    }

    private void validateInput(PersonDTO newPerson) throws InvalidInputException {
        //Variable firstName must NOT be null
        if (Objects.isNull(newPerson.getFirstName())) {
            throw new InvalidInputException("Field '" + "firstName" + "' is required");
        }

        //Variable lastName must NOT be null
        if (Objects.isNull(newPerson.getLastName())) {
            throw new InvalidInputException("Field '" + "lastName" + "' is required");
        }

        EntityManager em = emf.createEntityManager();
        try {
            List<Hobby> allHobbies = em.createQuery("SELECT h From Hobby h", Hobby.class).getResultList();
            List<Address> allAddresses = em.createQuery("SELECT a From Address a", Address.class).getResultList();

            //Variable hobbyTitle name MUST MATCH existing hobby
            for (String hobby : newPerson.getHobbies()) {
                boolean matchFound = false;
                for (Hobby dbHobby : allHobbies) {
                    if (dbHobby.getName().toLowerCase().equals(hobby.toLowerCase())) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    throw new InvalidInputException("Field 'hobbyTitle' value ‘" + hobby + "’ is invalid");
                }
            }

            //Variable street name MUST MATCH existing address
            boolean matchFound = false;
            for (Address dbAddress : allAddresses) {
                if (dbAddress.getStreet().toLowerCase().equals(newPerson.getStreet().toLowerCase())) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                throw new InvalidInputException("Field 'street' value ‘" + newPerson.getStreet() + "’ is invalid");
            }

        } finally {
            em.close();
        }
    }

}
