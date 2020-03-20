package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import dto.PhoneDTO;
import entities.Address;
import entities.Hobby;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import entities.Person;
import entities.Phone;
import exceptions.InvalidInputException;
import exceptions.NoResultFoundException;
import java.util.ArrayList;
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
    public PersonDTO addPerson(PersonDTO personDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        validateInput(personDTO);
        try {
            em.getTransaction().begin();
            List<Hobby> allHobbies = em.createQuery("SELECT h From Hobby h", Hobby.class).getResultList();
            Person newPerson = new Person(personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail());
            em.persist(newPerson);
            setStreet(em, personDTO, newPerson);
            setHobby(personDTO, allHobbies, newPerson);
            for (PhoneDTO newPhone : personDTO.getPhones().getPhones()) {
                Phone phone = new Phone(newPhone.getNumber(), newPhone.getDescription(), newPerson);
                em.persist(phone);
            }
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

    public void editPerson(PersonDTO personDTO, Long id) throws InvalidInputException {
        validateInput(personDTO);
        editPersonAddress(id, personDTO);
        editPersonHobbies(id, personDTO);
        editPersonPhones(id, personDTO);
        editPersonInfo(id, personDTO);
    }

    private void editPersonPhones(Long id, PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult();

            removePersonPhones(person, personDTO, em);
            addPersonPhones(personDTO, person, em);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void addPersonPhones(PersonDTO personDTO, Person person, EntityManager em) {
        // Add new phones
        for (PhoneDTO newPhone : personDTO.getPhones().getPhones()) {
            boolean matchFound = false;
            for (Phone phone : person.getPhones()) {
                if (Objects.equals(phone.getNumber(), newPhone.getNumber())) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                Phone phone = new Phone(newPhone.getNumber(), newPhone.getDescription(), person);
                em.persist(phone);
            }
        }
    }

    private void removePersonPhones(Person person, PersonDTO personDTO, EntityManager em) {
        // Update Phones
        // Remove phones no longer connected
        List<Phone> toBeRemoved = new ArrayList();
        for (Phone phone : person.getPhones()) {
            boolean matchFound = false;
            for (PhoneDTO newPhone : personDTO.getPhones().getPhones()) {
                if (Objects.equals(phone.getNumber(), newPhone.getNumber())) {
                    matchFound = true;
                    phone.setDescription(newPhone.getDescription());
                    break;
                }
            }
            if (!matchFound) {
                toBeRemoved.add(phone);
            }
        }
        for (Phone phone : toBeRemoved) {
            person.getPhones().remove(phone);
            em.remove(phone);
        }
    }

    private void editPersonHobbies(Long id, PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // Update Hobbies
            List<Hobby> allHobbies = em.createQuery("SELECT h From Hobby h", Hobby.class).getResultList();
            for (Hobby hobby : person.getHobbies()) {
                hobby.getPersons().remove(person);
            }
            person.getHobbies().clear();
            setHobby(personDTO, allHobbies, person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void editPersonAddress(Long id, PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult();

            // Update Address
            Address address = em.createQuery("SELECT a From Address a WHERE a.street = :street", Address.class)
                    .setParameter("street", personDTO.getStreet())
                    .getSingleResult();
            if (!Objects.isNull(person.getAddress())) {
                person.getAddress().getPersons().remove(person);
            }

            person.addAddressToPerson(address);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void editPersonInfo(Long id, PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Pull all relevent data first
            Person person = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult();
            // Update firstName, lastName and Email
            person.setFirstName(personDTO.getFirstName());
            person.setLastName(personDTO.getLastName());
            person.setEmail(personDTO.getEmail());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void setHobby(PersonDTO personDTO, List<Hobby> allHobbies, Person person) {
        for (String hobby : personDTO.getHobbies()) {
            Hobby newHobby = null;
            for (Hobby dbHobby : allHobbies) {
                if (dbHobby.getName().toLowerCase().equals(hobby.toLowerCase())) {
                    newHobby = dbHobby;
                    break;
                }
            }
            person.addHobbyToPerson(newHobby);
        }
    }

    public void setStreet(EntityManager em, PersonDTO personDTO, Person person) {
        Address address = em.createQuery("SELECT a From Address a WHERE a.street = :street", Address.class)
                .setParameter("street", personDTO.getStreet())
                .getSingleResult();
        person.addAddressToPerson(address);
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

        //Variable street must NOT be null
        if (Objects.isNull(newPerson.getStreet())) {
            throw new InvalidInputException("Field '" + "street" + "' is required");
        }

        //Variable phones must NOT be null
        if (Objects.isNull(newPerson.getPhones())) {
            throw new InvalidInputException("Field '" + "phones" + "' is required");
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
