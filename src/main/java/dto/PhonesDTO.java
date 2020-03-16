package dto;

import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PhonesDTO {
    
    private List<PhoneDTO> phones = new ArrayList();

    public PhonesDTO() {
    }
    
    public PhonesDTO(List<Phone> phones) {
        for (Phone phone : phones) {
            this.phones.add(new PhoneDTO(phone));
        }
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }
    
    

}
