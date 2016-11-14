package net.therap.processor;

import net.therap.domain.Developer;
import net.therap.domain.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ashrafhasan
 * @since 11/13/16
 */
public class ProcessUpdateAction {

    Logger logger = LoggerFactory.getLogger(ProcessUpdateAction.class);

    public void updateManagedEntity(EntityManagerFactory emFactory) {
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
        developer.setName("Mary Active");
        developer.getFeatureList().remove(0);
        developer.getFeatureList().add(new Feature("Feature 3", developer));
        logger.debug("Developer : {}", developer);

        em.close();
        em.getTransaction().commit();
    }

    public void updateDetachedEntity(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");

        List<Feature> featureList = new ArrayList<>();
        featureList.add(new Feature("Feature 1", developer));
        featureList.add(new Feature("Feature 2", developer));

        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        developer.setName("Mary Active");
        developer.getFeatureList().remove(0);
        developer.getFeatureList().add(new Feature("Feature 3", developer));

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        developer = em.merge(developer);
        logger.debug("Developer : {}", developer);

        em.close();
        em.getTransaction().commit();
    }

    public void invalidMerge(EntityManagerFactory emFactory) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Developer developer = new Developer("John Doe");

        List<Feature> featureList = new ArrayList<>();
        featureList.add(new Feature("Feature 1", developer));
        featureList.add(new Feature("Feature 2", developer));

        developer.setFeatureList(featureList);
        em.persist(developer);

        em.close();
        em.getTransaction().commit();

        em = emFactory.createEntityManager();
        em.getTransaction().begin();

        em.merge(developer);
        developer.setName("Mary Active");

        em.close();
        em.getTransaction().commit();
    }
}
