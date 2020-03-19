package facades;

import dto.HobbiesDTO;
import dto.HobbyDTO;
import entities.Hobby;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
    
    // Create a hobby
    public HobbyDTO addHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
            HobbyDTO result = new HobbyDTO(hobby);
            return result;
        } finally {
            em.close();
        }
    }
    
    public HobbiesDTO getAllHobbies(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> tq = em.createQuery("Select h from Hobby h", Hobby.class);
            List<Hobby> dbList = tq.getResultList();
            HobbiesDTO result = new HobbiesDTO(dbList);
            return result;
        } finally {
            em.close();
        }
    }
}