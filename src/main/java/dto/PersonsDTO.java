package dto;

import entities.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonsDTO {

    private List<PersonDTO> persons = new ArrayList();

    public PersonsDTO() {
    }

    public PersonsDTO(List<Person> persons) {
        for (Person person : persons) {
            this.persons.add(new PersonDTO(person));
        }
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

}
