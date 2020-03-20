package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PhoneDTO;
import dto.PhonesDTO;
import exceptions.InvalidInputException;
import facades.PhoneFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("phones")
public class PhonesResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final PhoneFacade FACADE = PhoneFacade.getPhoneFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPhones() {
        PhonesDTO allPhones = FACADE.getAllPhones();
        return GSON.toJson(allPhones);
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAddressCount() {
        long count = FACADE.getPhoneCount();
        return "{\"count\":" + count + "}";
    }

    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addAddress(String phone) throws InvalidInputException {
        PhoneDTO phoneAdd = GSON.fromJson(phone, PhoneDTO.class);
        phoneAdd = FACADE.addPhone(phoneAdd);
        return GSON.toJson(phoneAdd);
    }
}