package dto;

import entities.Person;
import entities.Address;
import entities.Hobby;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String cityName;
    private String zip;
    private List<String> hobbies = new ArrayList();
    private PhonesDTO phones = new PhonesDTO();

    public PersonDTO(Long id, String firstName, String lastName, String email,
            String street, String cityName, String zip, List<String> hobbies,
            PhonesDTO phones) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.cityName = cityName;
        this.zip = zip;
        this.hobbies = hobbies;
        this.phones = phones;
    }

    public PersonDTO() {
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        if (person.getAddress() != null) {
            this.street = person.getAddress().getStreet();
            this.cityName = person.getAddress().getCityInfo().getCity();
            this.zip = person.getAddress().getCityInfo().getZipCode();
        }
        this.phones = new PhonesDTO(person.getPhones());
        for (Hobby hobby : person.getHobbies()) {
            this.hobbies.add(hobby.getName());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public PhonesDTO getPhones() {
        return phones;
    }

    public void setPhones(PhonesDTO phones) {
        this.phones = phones;
    }

}
