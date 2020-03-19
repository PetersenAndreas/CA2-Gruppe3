package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.HobbyDTO;
import dto.PersonsDTO;
import exceptions.InvalidInputException;
import facades.HobbyFacade;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("hobbies")
public class HobbiesResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final HobbyFacade FACADE = HobbyFacade.getHobbyFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllHobbies() {
        return GSON.toJson(FACADE.getAllHobbies());
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getHobbyCount() {
        long count = FACADE.getHobbyCount();
        return "{\"count\":" + count + "}";
    }
    
    @Path("countbyhobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsCountFromHobby(@PathParam("hobby") String hobby) {
        try {
        long count = FACADE.getPersonCountByHobby(hobby);
        return "{\"count\":" + count + "}";
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
    
    @Path("person/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsWithHobby(@PathParam("hobby") String hobby) {
        try {
        PersonsDTO list = FACADE.getPersonsByHobby(hobby);
        return GSON.toJson(list);
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
    
    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addHobby(String hobby) throws InvalidInputException {
        HobbyDTO hobbyAdd = GSON.fromJson(hobby, HobbyDTO.class);
        hobbyAdd = FACADE.addHobby(hobbyAdd);
        return GSON.toJson(hobbyAdd);
    }
}