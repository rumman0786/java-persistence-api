package net.therap.processor;

import net.therap.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;

/**
 * @author ashrafhasan
 * @since 11/13/16
 */
public class ProcessInsertAction {

    public Developer simpleInsert(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");
        List<Feature> featureList = Arrays.asList(new Feature("Feature 1", developer), new Feature("Feature 2", developer));
        developer.setFeatureList(featureList);
        em.persist(developer);
        developer.setName("Mary Active");

        em.close();
        em.getTransaction().commit();

        return developer;
    }

    /*
        Owner entity of the relationship can take detached object as the associated Entity. The corresponding table
        only needs the primary key of the detached object to insert is as the foreign key.
    */
    public void insertWithAssociatedDetachedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        University university = new University("DU");
        em.persist(university);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Student student = new Student("John Doe", university);
        em.persist(student);

        em.close();
        em.getTransaction().commit();
    }

    /*
        In the case of Application Managed Persistence Context, it get closed with the transaction commit
        or em.close (that comes later). So changes made after the Persistence Context is closed or Transaction is committed
        will not be flushed in database.
    */
    public Developer invalidPropertySettingAfterEmClosing(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");
        List<Feature> featureList = Arrays.asList(new Feature("Feature 1", developer), new Feature("Feature 2", developer));
        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        developer.setName("Marry Active");

        return developer;
    }

    /*
        Inverse side of the relationship can not take detached object as the associated Entity. As it depends on the
        the owner side to manage the relationship so owner object needs to be managed.
    */
    public void invalidInsertWithAssociatedDetachedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        ViceChancellor viceChancellor = new ViceChancellor("John Doe", null);
        em.persist(viceChancellor);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        University student = new University("DU");
        student.setViceChancellor(viceChancellor);
        em.persist(student);

        em.close();
        em.getTransaction().commit();
    }
}
