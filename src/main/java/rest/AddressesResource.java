package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.AddressesDTO;
import facades.AddressFacade;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("addresses")
public class AddressesResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final AddressFacade FACADE = AddressFacade.getAddressFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllAddresses() {
        AddressesDTO all = FACADE.getAllAddresses();
        return GSON.toJson(all);
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAddressCount() {
        long count = FACADE.getAddressCount();
        return "{\"count\":" + count + "}";
    }

//    @Path("/add")
//    @POST
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
//    public String addAddress(String address) throws InvalidInputException {
//        AddressDTO addressAdd = GSON.fromJson(address, AddressDTO.class);
//        addressAdd = FACADE.addAddress(addressAdd);
//        return GSON.toJson(addressAdd);
//    }
}