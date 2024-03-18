package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class AdresseDao {
    private EntityManager em;
    private EntityTransaction tr;


    public AdresseDao() {

        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }


    public void addAdresse(Adresse Adresse) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(Adresse);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void deleteAdresse(Adresse Adresse) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            Adresse entity = em.find(Adresse.class,Adresse);
            if (entity != null) { // vérifier que l'objet existe
                em.remove(entity);
            }
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);
        }

    }

    public List<Adresse> getAllAdresses() {
        // TODO Auto-generated method stub
        Query query= em.createQuery(" from Adresse ");
        return query.getResultList();
    }

    public Adresse findAdresseById(int id) {
        // TODO Auto-generated method stub
        return em.find(Adresse.class, id);
    }

}
