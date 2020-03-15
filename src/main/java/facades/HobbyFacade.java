package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;
    
    private HobbyFacade() {}
    
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public long getHobbyCount(){
        EntityManager em = getEntityManager();
        try{
            long hobbyCount = (long)em.createQuery("SELECT COUNT(h) FROM Hobby h").getSingleResult();
            return hobbyCount;
        }finally{  
            em.close();
        }
    }
}