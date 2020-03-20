
package facades;

import dto.PhoneDTO;
import dto.PhonesDTO;
import entities.Phone;
import exceptions.InvalidInputException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class PhoneFacade {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;
    
    private PhoneFacade() {}
    
    public static PhoneFacade getPhoneFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    // Create a Phone
    public PhoneDTO addPhone(PhoneDTO phoneDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Phone newPhone = new Phone(phoneDTO.getNumber(), phoneDTO.getDescription());
            em.persist(newPhone);
            em.getTransaction().commit();
            PhoneDTO result = new PhoneDTO(newPhone);
            return result;
        } finally {
            em.close();
        }
    }
    
    // Get amount of phones in database
    public long getPhoneCount(){
        EntityManager em = getEntityManager();
        try{
            long phoneCount = (long)em.createQuery("SELECT COUNT(p) FROM Phone p").getSingleResult();
            return phoneCount;
        }finally{  
            em.close();
        }
    }
    
    // Get all phones in the database, with details
    public PhonesDTO getAllPhones() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p", Phone.class);
            List<Phone> dbList = query.getResultList();
            PhonesDTO result = new PhonesDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }
}