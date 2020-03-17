package entities;

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
    private static final PhoneFacade PHONE_FACADE = PhoneFacade.getPhoneFacade(EMF);

    public static void main(String[] args) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        addPersons();
        addHobbies();
        addCityInfo();
        addPhones();
    }

    private static void addPersons() {
        Person person1 = new Person("Khabib", "Nurmagomedov", "LwChamp@gmail.com");
        Person person2 = new Person("Tony", "Ferguson", "PplChamp@gmail.com");
        Person person3 = new Person("Mohamed", "Salah", "Pharaoh@gmail.com");
        Person person4 = new Person("Virgil", "van Dijk", "TopDefender@gmail.com");
        Person person5 = new Person("Keanu", "Reeves", "RealNeo@gmail.com");
        PERSON_FACADE.addPerson(person1);
        PERSON_FACADE.addPerson(person2);
        PERSON_FACADE.addPerson(person3);
        PERSON_FACADE.addPerson(person4);
        PERSON_FACADE.addPerson(person5);
    }

    private static void addHobbies() {
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
    }

    private static void addCityInfo() {
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
    }
    
    private static void addPhones() {
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
    }
}