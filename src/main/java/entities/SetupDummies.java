package entities;

import dto.PersonDTO;
import exceptions.InvalidInputException;
import facades.AddressFacade;
import facades.CityInfoFacade;
import facades.HobbyFacade;
import facades.PersonFacade;
import facades.PhoneFacade;
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

    private static final PersonFacade PERSON_FACADE = PersonFacade.getPersonFacade(EMF);
    private static final HobbyFacade HOBBY_FACADE = HobbyFacade.getHobbyFacade(EMF);
    private static final CityInfoFacade CITYINFO_FACADE = CityInfoFacade.getCityInfoFacade(EMF);
    private static final AddressFacade ADDRESS_FACADE = AddressFacade.getAddressFacade(EMF);
    private static final PhoneFacade PHONE_FACADE = PhoneFacade.getPhoneFacade(EMF);

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
//        addPersons();
//        addHobbies();
//        addCityInfoAndAddresses();
//        addPhones();
          addEntities();
    }

    private static void addEntities() throws InvalidInputException {
        Hobby hobby1 = new Hobby("MMA", "Beating people up");
        Hobby hobby2 = new Hobby("Football", "Most popular sport");
        Hobby hobby3 = new Hobby("Hockey", "Canadians love it");
        Hobby hobby4 = new Hobby("Acting", "Pretending you're cool");
        Hobby hobby5 = new Hobby("Knitting", "Popular with babushkas");
        HOBBY_FACADE.addHobby(hobby1);
        HOBBY_FACADE.addHobby(hobby2);
        HOBBY_FACADE.addHobby(hobby3);
        HOBBY_FACADE.addHobby(hobby4);
        HOBBY_FACADE.addHobby(hobby5);
        
        CityInfo city1 = new CityInfo("2605", "Brøndby");
        CityInfo city2 = new CityInfo("2660", "Brøndby Strand");
        CityInfo city3 = new CityInfo("2650", "Hvidovre");
        CityInfo city4 = new CityInfo("2600", "Glostrup");
        CityInfo city5 = new CityInfo("2620", "Albertslund");
        CITYINFO_FACADE.addCityInfo(city1);
        CITYINFO_FACADE.addCityInfo(city2);
        CITYINFO_FACADE.addCityInfo(city3);
        CITYINFO_FACADE.addCityInfo(city4);
        CITYINFO_FACADE.addCityInfo(city5);
        
        Address address1 = new Address("Jensensgade", city1);
        Address address2 = new Address("Jørgensesgade", city2);
        Address address3 = new Address("Johnsensgade", city3);
        Address address4 = new Address("Johansensgade", city4);
        Address address5 = new Address("Jimmysgade", city5);
        ADDRESS_FACADE.addAddress(address1);
        ADDRESS_FACADE.addAddress(address2);
        ADDRESS_FACADE.addAddress(address3);
        ADDRESS_FACADE.addAddress(address4);
        ADDRESS_FACADE.addAddress(address5);
        
        Phone phone1 = new Phone("91827512", "Mobile");
        Phone phone2 = new Phone("17295712", "Mobile");
        Phone phone3 = new Phone("68749216", "Work");
        Phone phone4 = new Phone("82717642", "Home");
        Phone phone5 = new Phone("55821924", "Mobile");
        PHONE_FACADE.addPhone(phone1);
        PHONE_FACADE.addPhone(phone2);
        PHONE_FACADE.addPhone(phone3);
        PHONE_FACADE.addPhone(phone4);
        PHONE_FACADE.addPhone(phone5);
        
        Person person1 = new Person("Khabib", "Nurmagomedov", "LwChamp@gmail.com");
        Person person2 = new Person("Tony", "Ferguson", "PplChamp@gmail.com");
        Person person3 = new Person("Mohamed", "Salah", "Pharaoh@gmail.com");
        Person person4 = new Person("Virgil", "van Dijk", "TopDefender@gmail.com");
        Person person5 = new Person("Keanu", "Reeves", "RealNeo@gmail.com");
        PersonDTO person1DTO = new PersonDTO(person1);
        PersonDTO person2DTO = new PersonDTO(person2);
        PersonDTO person3DTO = new PersonDTO(person3);
        PersonDTO person4DTO = new PersonDTO(person4);
        PersonDTO person5DTO = new PersonDTO(person5);
        PERSON_FACADE.addPerson(person1DTO);
        PERSON_FACADE.addPerson(person2DTO);
        PERSON_FACADE.addPerson(person3DTO);
        PERSON_FACADE.addPerson(person4DTO);
        PERSON_FACADE.addPerson(person5DTO);
        
        
        person1.addHobbyToPerson(hobby1);
        person2.addHobbyToPerson(hobby1);
        person3.addHobbyToPerson(hobby2);
        person4.addHobbyToPerson(hobby2);
        person5.addHobbyToPerson(hobby4);
        person1.addPhoneToPerson(phone5);
        person2.addPhoneToPerson(phone4);
        person3.addPhoneToPerson(phone2);
        person4.addPhoneToPerson(phone4);
        person5.addPhoneToPerson(phone1);
        person1.addAddressToPerson(address3);
        person2.addAddressToPerson(address1);
        person3.addAddressToPerson(address2);
        person4.addAddressToPerson(address5);
        person5.addAddressToPerson(address4);
        
        hobby1.addPersonToHobby(person1);
        hobby1.addPersonToHobby(person2);
        hobby2.addPersonToHobby(person3);
        hobby2.addPersonToHobby(person4);
        hobby4.addPersonToHobby(person5);
        phone1.addPhoneToPerson(person5);
        phone2.addPhoneToPerson(person3);
        phone4.addPhoneToPerson(person2);
        phone4.addPhoneToPerson(person4);
        phone5.addPhoneToPerson(person1);
        address1.addPersonToAddress(person2);
        address2.addPersonToAddress(person3);
        address3.addPersonToAddress(person1);
        address4.addPersonToAddress(person5);
        address5.addPersonToAddress(person4);
    }
//    
//    private static void addPersons() {
//        Person person1 = new Person("Khabib", "Nurmagomedov", "LwChamp@gmail.com");
//        Person person2 = new Person("Tony", "Ferguson", "PplChamp@gmail.com");
//        Person person3 = new Person("Mohamed", "Salah", "Pharaoh@gmail.com");
//        Person person4 = new Person("Virgil", "van Dijk", "TopDefender@gmail.com");
//        Person person5 = new Person("Keanu", "Reeves", "RealNeo@gmail.com");
//        PERSON_FACADE.addPerson(person1);
//        PERSON_FACADE.addPerson(person2);
//        PERSON_FACADE.addPerson(person3);
//        PERSON_FACADE.addPerson(person4);
//        PERSON_FACADE.addPerson(person5);
//    }
//
//    private static void addHobbies() {
//        Hobby hobby1 = new Hobby("MMA", "Beating people up");
//        Hobby hobby2 = new Hobby("Football", "Most popular sport");
//        Hobby hobby3 = new Hobby("Hockey", "Canadians love it");
//        Hobby hobby4 = new Hobby("Acting", "Pretending you're cool");
//        Hobby hobby5 = new Hobby("Knitting", "Popular with babushkas");
//        HOBBY_FACADE.addHobby(hobby1);
//        HOBBY_FACADE.addHobby(hobby2);
//        HOBBY_FACADE.addHobby(hobby3);
//        HOBBY_FACADE.addHobby(hobby4);
//        HOBBY_FACADE.addHobby(hobby5);
//    }
//
//    private static void addCityInfoAndAddresses() {
//        CityInfo city1 = new CityInfo("2605", "Brøndby");
//        CityInfo city2 = new CityInfo("2660", "Brøndby Strand");
//        CityInfo city3 = new CityInfo("2650", "Hvidovre");
//        CityInfo city4 = new CityInfo("2600", "Glostrup");
//        CityInfo city5 = new CityInfo("2620", "Albertslund");
//        CITYINFO_FACADE.addCityInfo(city1);
//        CITYINFO_FACADE.addCityInfo(city2);
//        CITYINFO_FACADE.addCityInfo(city3);
//        CITYINFO_FACADE.addCityInfo(city4);
//        CITYINFO_FACADE.addCityInfo(city5);
//        Address address1 = new Address("91827512", city1);
//        Address address2 = new Address("91827512", city2);
//        Address address3 = new Address("91827512    ", city3);
//        Address address4 = new Address("91827512", city4);
//        Address address5 = new Address("91827512", city5);
//    }
//    
//    private static void addPhones() {
//        Phone phone1 = new Phone("91827512", "Mobile");
//        Phone phone2 = new Phone("17295712", "Mobile");
//        Phone phone3 = new Phone("68749216", "Work");
//        Phone phone4 = new Phone("82717642", "Home");
//        Phone phone5 = new Phone("55821924", "Mobile");
//        PHONE_FACADE.addPhone(phone1);
//        PHONE_FACADE.addPhone(phone2);
//        PHONE_FACADE.addPhone(phone3);
//        PHONE_FACADE.addPhone(phone4);
//        PHONE_FACADE.addPhone(phone5);
//    }
}