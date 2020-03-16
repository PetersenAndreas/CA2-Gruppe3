package dto;

import entities.Address;
import entities.CityInfo;
import java.util.ArrayList;
import java.util.List;

public class CityInfoDTO {

    private String zipCode;
    private String cityName;
//    private List<String> addresses = new ArrayList();

    public CityInfoDTO() {
    }

//    public CityInfoDTO(String zipCode, String city, List<Address> addresses) {
//        this.zipCode = zipCode;
//        this.cityName = city;
//        for (Address addresse : addresses) {
//            this.addresses.add(addresse.getStreet());
//        }
//    }
//    
//    public CityInfoDTO(CityInfo city) {
//        this.zipCode = city.getZipCode();
//        this.cityName = city.getCity();
//        for (Address addresse : city.getAddresses()) {
//            this.addresses.add(addresse.getStreet());
//        }
//    }
    public CityInfoDTO(String zipCode, String city) {
        this.zipCode = zipCode;
        this.cityName = city;
    }

    public CityInfoDTO(CityInfo city) {
        this.zipCode = city.getZipCode();
        this.cityName = city.getCity();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

//    public List<String> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<String> addresses) {
//        this.addresses = addresses;
//    }
}
