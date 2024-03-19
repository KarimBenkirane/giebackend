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
    private static AdresseDao adresseDao;

    public static AdresseDao getAdresseDao(){
        if(adresseDao == null)
            adresseDao = new AdresseDao();
        return adresseDao;
    }


    private AdresseDao() {

        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }


    public void addAdresse(Adresse adresse) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(adresse);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }
    public void addAdresseByAttribues(String rue, int numeroRue, String quartier, int codePostal, String ville, String pays) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(new Adresse(rue,numeroRue,quartier,codePostal,ville,pays));
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }



    public void deleteAdresse(int id) {
        String hql = "delete from Adresse where adresse_id =:id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id",id);
            query.executeUpdate();
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
