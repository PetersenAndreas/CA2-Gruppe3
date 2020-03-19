package dto;

import entities.Address;
import java.util.ArrayList;
import java.util.List;

public class AddressesDTO {

    List<AddressDTO> addressList = new ArrayList();

    public AddressesDTO(List<Address> addresses) {
        for (Address address : addresses) {
            addressList.add(new AddressDTO(address));
        }
    }

    public List<AddressDTO> getAddresses() {
        return addressList;
    }

    public void setAddresses(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }
}