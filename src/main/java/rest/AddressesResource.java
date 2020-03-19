
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.AddressDTO;
import dto.PersonsDTO;
import entities.Address;
import facades.AddressFacade;
import facades.CityInfoFacade;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

/**
 *
 * @author cahit
 */

 @Path("address")
public class AddressesResource {


    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final AddressFacade FACADE = AddressFacade.getAddressFacade(EMF);
    private static final CityInfoFacade FACADE_CITY = CityInfoFacade.getCityInfoFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


//    
//    @Path("add")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addAddress(String per) {
//        
//    AddressDTO p = GSON.fromJson(per, AddressDTO.class);
//    AddressDTO address = FACADE.addAddress(p.getStreet(),p.getCityInfo());
//    return Response.ok(address).build();
//    }
    
    
    }


