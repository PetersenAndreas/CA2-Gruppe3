package dto;

import entities.Address;
import entities.CityInfo;

public class AddressDTO {
    
    private String street;
    private CityInfo cityInfo;
    
    public AddressDTO() {
    }
    
    public AddressDTO(String street, CityInfo cityInfo) {
        this.street = street;
        this.cityInfo = cityInfo;
    }
    
    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.cityInfo = address.getCityInfo();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }
    
}