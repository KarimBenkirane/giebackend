package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EntrepriseDao {
    private EntityManager em;
    private EntityTransaction tr;


    public EntrepriseDao() {

        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }


    public void addEntreprise(Entreprise entreprise) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(entreprise);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void deleteEntreprise(Entreprise entreprise) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            Entreprise entity = em.find(Entreprise.class,entreprise);
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

    public List<Entreprise> getAllEntreprises() {
        // TODO Auto-generated method stub
        Query query= em.createQuery(" from Contact where contact_type='Entreprise' ");
        return query.getResultList();
    }

    public Entreprise findEntrepriseById(int id) {
        // TODO Auto-generated method stub
        return em.find(Entreprise.class, id);
    }

    public Entreprise findEntrepriseByRaisonSociale(String raisonSociale) {
        TypedQuery<Entreprise> query = em.createQuery("from Contact where contact_type='Entreprise' and raisonSociale = :raisonSociale", Entreprise.class);
        query.setParameter("raisonSociale", raisonSociale);
        try {
            return query.getSingleResult();
        } catch (NonUniqueResultException e) {
            return null;
        }
    }
}
