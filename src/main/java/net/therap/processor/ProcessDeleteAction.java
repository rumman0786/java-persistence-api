package net.therap.processor;

import net.therap.domain.Developer;
import net.therap.domain.Feature;
import net.therap.domain.Student;
import net.therap.domain.University;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;

/**
 * @author ashrafhasan
 * @since 11/13/16
 */
public class ProcessDeleteAction {

    Logger logger = LoggerFactory.getLogger(ProcessDeleteAction.class);

    public void deleteManagedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");
        List<Feature> featureList = Arrays.asList(new Feature("Feature 1", developer), new Feature("Feature 2", developer));
        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        developer = em.find(Developer.class, developer.getId());
        em.remove(developer);

        em.close();
        em.getTransaction().commit();
    }

    public void deleteDetachedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");
        List<Feature> featureList = Arrays.asList(new Feature("Feature 1", developer), new Feature("Feature 2", developer));
        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        developer = em.getReference(Developer.class, developer.getId());
        em.remove(developer);

        em.close();
        em.getTransaction().commit();
    }

    /*Only a managed object can be removed . But in this case merge method should not be called to make
     a detached object managed. calling 'getReference' is better option */
    public void invalidDeleteForDetachedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");
        List<Feature> featureList = Arrays.asList(new Feature("Feature 1", developer), new Feature("Feature 2", developer));
        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        developer = em.merge(developer);
        em.remove(developer);

        em.close();
        em.getTransaction().commit();
    }

    /*Parent Entity can not be removed as long as the Child has the reference to it*/
    public void invalidChildDeleteWithParentExists(EntityManagerFactory emFactory) {
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

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        university = em.find(University.class, university.getId());
        em.remove(university);

        em.close();
        em.getTransaction().commit();
    }
}
