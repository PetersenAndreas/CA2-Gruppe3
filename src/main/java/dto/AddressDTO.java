package dto;

import entities.Address;

public class AddressDTO {
    
    private String street;
    private CityInfoDTO city;
    
    public AddressDTO() {
    }
    
    public AddressDTO(String street, CityInfoDTO cityInfo) {
        this.street = street;
        this.city = cityInfo;
    }
    
    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.city = new CityInfoDTO(address.getCityInfo());
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public CityInfoDTO getCityInfo() {
        return city;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.city = cityInfo;
    }
}