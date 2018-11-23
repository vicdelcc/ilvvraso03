package de.berlin.htw.usws.config;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerDbProducer {

//    @PersistenceContext(unitName = "ingrEatDBPg")
//    private EntityManager entityManager;
//
//    @Produces
//    @Default
//    @RequestScoped
//    private EntityManager provide() {
//        return this.entityManager;
//    }
//
//    public void close(@Disposes @Default EntityManager em) {
//        if (em.isOpen()) {
//            em.close();
//        }
//    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @Default
    @RequestScoped
    public EntityManager create()
    {
        return this.entityManagerFactory.createEntityManager();
    }

    public void dispose(@Disposes @Default EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

}