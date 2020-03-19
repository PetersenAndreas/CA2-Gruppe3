package dto;

import entities.Address;
import java.util.ArrayList;
import java.util.List;

public class AddressesDTO {

    private List<AddressDTO> addressList = new ArrayList();
    
    public AddressesDTO(){
    }

    public AddressesDTO(List<Address> addresses) {
        for (Address address : addresses) {
            this.addressList.add(new AddressDTO(address));
        }
    }

    public List<AddressDTO> getAddresses() {
        return addressList;
    }

    public void setAddresses(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }
}