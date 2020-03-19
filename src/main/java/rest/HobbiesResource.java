package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonsDTO;
import facades.HobbyFacade;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
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

    private static final PersonFacade personFacade = PersonFacade.getPersonFacade(EMF);
    private static final HobbyFacade hobbyFacade = HobbyFacade.getHobbyFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getHobbyCount() {
        long count = hobbyFacade.getHobbyCount();
        return "{\"count\":" + count + "}";
    }
    
    @Path("countbyhobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsCountFromHobby(@PathParam("hobby") String hobby) {
        try {
        long count = personFacade.getPersonCountByHobby(hobby);
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
        PersonsDTO list = personFacade.getPersonsByHobby(hobby);
        return GSON.toJson(list);
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
}
