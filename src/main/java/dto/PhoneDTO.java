package dto;

import entities.Phone;

public class PhoneDTO {
    
    private Long id;
    private String phoneNumber;
    private String description;

    public PhoneDTO(Long id, String phoneNumber, String description) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }

    public PhoneDTO(Phone phone) {
        this.id = phone.getId();
//        this.phoneNumber = phone.getNumber();
        this.description = phone.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

}
