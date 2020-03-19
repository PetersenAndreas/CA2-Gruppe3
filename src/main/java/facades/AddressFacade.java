package facades;

import dto.AddressDTO;
import dto.AddressesDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.InvalidInputException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
    
    // Create an address
//    public AddressDTO addAddress(AddressDTO addressDTO) throws InvalidInputException {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            Address newAddress = new Address(addressDTO.getStreet());
//            em.persist(address);
//            em.getTransaction().commit();
//            AddressDTO result = new AddressDTO(address);
//            return result;
//        } finally {
//            em.close();
//        }
//    }

    public long getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            long addressCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
            return addressCount;
        } finally {
            em.close();
        }
    }
    
    //Get all addresses
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