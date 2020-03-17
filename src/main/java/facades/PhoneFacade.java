package facades;

import dto.PhoneDTO;
import entities.Phone;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
    public PhoneDTO addPhone(Phone phone) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(phone);
            em.getTransaction().commit();
            PhoneDTO result = new PhoneDTO(phone);
            return result;
        } finally {
            em.close();
        }
    }
    
    public long getPhoneCount(){
        EntityManager em = getEntityManager();
        try{
            long phoneCount = (long)em.createQuery("SELECT COUNT(p) FROM Phone p").getSingleResult();
            return phoneCount;
        }finally{  
            em.close();
        }
    }
}