package dto;

import java.util.ArrayList;
import java.util.List;
import entities.Hobby;

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
