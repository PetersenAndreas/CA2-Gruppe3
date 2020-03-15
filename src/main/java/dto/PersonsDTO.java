/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruger
 */
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
