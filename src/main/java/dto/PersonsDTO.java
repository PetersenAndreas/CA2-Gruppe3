package dto;

import java.util.ArrayList;
import java.util.List;

public class PersonsDTO {

    private List<PersonDTO> persons = new ArrayList();

    public PersonsDTO() {
    }

    public PersonsDTO(List<PersonDTO> persons) {
        for (PersonDTO person : persons) {
            this.persons.add(person);
        }
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

}
