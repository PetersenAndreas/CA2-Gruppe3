package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CitiesInfoDTO;
import dto.CityInfoDTO;
import dto.PersonsDTO;
import exceptions.InvalidInputException;
import facades.CityInfoFacade;
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

@Path("cities")
public class CitiesResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final CityInfoFacade FACADE = CityInfoFacade.getCityInfoFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllCities() {
        CitiesInfoDTO cities = FACADE.getAllCityInfo();
        return GSON.toJson(cities);
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCityCount() {
        long count = FACADE.getCityCount();
        return "{\"count\":" + count + "}";
    }
    
    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsFromCity(@PathParam("id") String zip) {
        PersonsDTO persons = FACADE.getPersonsFromCity(zip);
        return GSON.toJson(persons);
    }
    
    @Path("/city/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCityInfoByZipCode(@PathParam("zip") String zip) {
        try {
        CityInfoDTO city = FACADE.getCityInfoByZipCode(zip);
        return GSON.toJson(city);
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
    
    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addCity(String city) throws InvalidInputException {
        CityInfoDTO cityAdd = GSON.fromJson(city, CityInfoDTO.class);
        cityAdd = FACADE.addCityInfo(cityAdd);
        return GSON.toJson(cityAdd);
    }
}