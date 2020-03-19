package entities;

import exceptions.InvalidInputException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

public class SetupDummies {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.DROP_AND_CREATE);

    public static void main(String[] args) throws InvalidInputException {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
          addEntities();
    }

    private static void addEntities() throws InvalidInputException {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            Hobby hobby1 = new Hobby("MMA", "Beating people up");
            Hobby hobby2 = new Hobby("Football", "Most popular sport");
            Hobby hobby3 = new Hobby("Hockey", "Canadians love it");
            Hobby hobby4 = new Hobby("Acting", "Pretending you're cool");
            Hobby hobby5 = new Hobby("Knitting", "Popular with babushkas");
        
            CityInfo city1 = new CityInfo("2605", "Brøndby");
            CityInfo city2 = new CityInfo("2660", "Brøndby Strand");
            CityInfo city3 = new CityInfo("2650", "Hvidovre");
            CityInfo city4 = new CityInfo("2600", "Glostrup");
            CityInfo city5 = new CityInfo("2620", "Albertslund");
            
            Address address1 = new Address("Jensensgade", city1);
            Address address2 = new Address("Jørgensesgade", city2);
            Address address3 = new Address("Johnsensgade", city3);
            Address address4 = new Address("Johansensgade", city4);
            Address address5 = new Address("Jimmysgade", city5);
            
            Phone phone1 = new Phone("91827512", "Mobile");
            Phone phone2 = new Phone("17295712", "Mobile");
            Phone phone3 = new Phone("68749216", "Work");
            Phone phone4 = new Phone("82717642", "Home");
            Phone phone5 = new Phone("55821924", "Mobile");
            
            Person person1 = new Person("Khabib", "Nurmagomedov", "LwChamp@gmail.com");
            Person person2 = new Person("Tony", "Ferguson", "PplChamp@gmail.com");
            Person person3 = new Person("Mohamed", "Salah", "Pharaoh@gmail.com");
            Person person4 = new Person("Virgil", "van Dijk", "TopDefender@gmail.com");
            Person person5 = new Person("Keanu", "Reeves", "RealNeo@gmail.com");
            
            person1.addHobbyToPerson(hobby1);
            person2.addHobbyToPerson(hobby1);
            person3.addHobbyToPerson(hobby2);
            person4.addHobbyToPerson(hobby2);
            person5.addHobbyToPerson(hobby4);
            
            person1.addAddressToPerson(address2);
            person2.addAddressToPerson(address5);
            person3.addAddressToPerson(address1);
            person4.addAddressToPerson(address3);
            person5.addAddressToPerson(address4);
            
            phone1.addPhoneToPerson(person3);
            phone2.addPhoneToPerson(person5);
            phone3.addPhoneToPerson(person1);
            phone4.addPhoneToPerson(person4);
            phone5.addPhoneToPerson(person2);
            
            em.persist(hobby1);
            em.persist(hobby2);
            em.persist(hobby3);
            em.persist(hobby4);
            em.persist(hobby5);
            
            em.persist(city1);
            em.persist(city2);
            em.persist(city3);
            em.persist(city4);
            em.persist(city5);
            
            em.persist(address1);
            em.persist(address2);
            em.persist(address3);
            em.persist(address4);
            em.persist(address5);
            
            em.persist(phone1);
            em.persist(phone2);
            em.persist(phone3);
            em.persist(phone4);
            em.persist(phone5);
            
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(person4);
            em.persist(person5);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}