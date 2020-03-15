/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;
import entities.Hobby;

/**
 *
 * @author Bruger
 */
public class HobbiesDTO {
    
    private List<HobbyDTO> hobbies = new ArrayList();

    public HobbiesDTO() {
    }
    
    public HobbiesDTO(List<Hobby> hobbies) {
        for (Hobby hobby : hobbies) {
            this.hobbies.add(new HobbyDTO(hobby));
        }
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }
    
}
