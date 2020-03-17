package facades;

import dto.AddressDTO;
import entities.Address;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
    public AddressDTO addAddress(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            AddressDTO result = new AddressDTO(address);
            return result;
        } finally {
            em.close();
        }
    }

    public long getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            long addressCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
            return addressCount;
        } finally {
            em.close();
        }
    }
}