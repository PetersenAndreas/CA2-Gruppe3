/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CitiesInfoDTO;
import dto.PersonDTO;
import dto.PersonsDTO;
import facades.CityInfoFacade;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Bruger
 */
@Path("cities")
public class CitiesResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/startcode",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final CityInfoFacade FACADE_CITY = CityInfoFacade.getCityInfoFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllCities() {
        CitiesInfoDTO cities = FACADE_CITY.getAllCityInfoes();
        return GSON.toJson(cities);
    }

    
    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonsFromCity(@PathParam("id") Long id) {
    PersonsDTO persons = FACADE_CITY.getPersonsFromCity(id);
    return GSON.toJson(persons);
    }
    
}
