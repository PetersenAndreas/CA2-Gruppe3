package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.InvalidInputException;
import exceptions.NoResultFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Disabled;

//@Disabled
@Path("persons")
public class PersonsResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    public String demo() {
//        return "{\"msg\":\"Hello World\"}";
//    }
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.getPersonCount();
        return "{\"count\":" + count + "}";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        PersonsDTO all = FACADE.getAllPersons();
        return GSON.toJson(all);
    }

    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByID(@PathParam("id") long id) throws NoResultFoundException {
        PersonDTO person = FACADE.getPersonById(id);
        return GSON.toJson(person);
    }

    @Path("/edit/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson(String person, @PathParam("id") long id) throws InvalidInputException {
        PersonDTO newPerson = GSON.fromJson(person, PersonDTO.class);
        FACADE.editPerson(newPerson, id);
        return GSON.toJson(newPerson);
    }

    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addPerson(String person) {
        PersonDTO personAdd = GSON.fromJson(person, PersonDTO.class);
        personAdd = FACADE.addPerson(personAdd);
        return GSON.toJson(personAdd);

    }

}
