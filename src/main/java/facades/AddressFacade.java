package facades;

import dto.AddressDTO;
import dto.AddressesDTO;
import entities.Address;
import entities.CityInfo;
import exceptions.InvalidInputException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    private AddressFacade() {
    }

    public static AddressFacade getAddressFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    // Create an address with a related city
    public AddressDTO addAddress(AddressDTO addressDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CityInfo city;
            try {
            city = em.createQuery("SELECT c FROM CityInfo c WHERE c.zipCode = :zipCode", CityInfo.class)
                    .setParameter("zipCode", addressDTO.getCityInfo().getZipCode())
                    .getSingleResult();
            }catch(NoResultException e) {
                throw new InvalidInputException("Field ‘zipCode’ value ‘"+addressDTO.getCityInfo().getZipCode()+"’ is invalid");
            }
            Address newAddress = new Address(addressDTO.getStreet(), city);
            em.persist(newAddress);
            em.getTransaction().commit();
            AddressDTO result = new AddressDTO(newAddress);
            return result;
        } finally {
            em.close();
        }
    }

    // get amount of addresses in database
    public long getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            long addressCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
            return addressCount;
        } finally {
            em.close();
        }
    }
    
    //Get all addresses in the database, with details
    public AddressesDTO getAllAddresses() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a", Address.class);
            List<Address> dbList = query.getResultList();
            AddressesDTO result = new AddressesDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }
}