package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ParticulierDao {
    private EntityManager em;
    private EntityTransaction tr;


    public ParticulierDao() {

        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }


    public void addParticulier(Particulier particulier) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(particulier);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void deleteParticulier(Particulier particulier) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            Particulier entity = em.find(Particulier.class,particulier);
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

    public List<Particulier> getAllParticuliers() {
        // TODO Auto-generated method stub
        Query query= em.createQuery(" from Contact where contact_type='Particulier' ");
        return query.getResultList();
    }

    public Particulier findParticulierById(int id) {
        // TODO Auto-generated method stub
        return em.find(Particulier.class, id);
    }

    public Particulier findParticulierByNom(String nom) {
        TypedQuery<Particulier> query = em.createQuery("from Contact where contact_type='Particulier' and nom = :nom", Particulier.class);
        query.setParameter("nom", nom);
        try {
            return query.getSingleResult();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }
}
