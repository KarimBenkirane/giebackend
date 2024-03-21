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

    public AdresseDao() {

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

    public Adresse getAdresseById(int id){
        return em.find(Adresse.class, id);
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


    public void changeCodePostal(int adresse_id,int codePostal){
        String hql = "update Adresse set codePostal= :codePostal where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("codePostal",codePostal);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void changeNumeroRue(int adresse_id,int numeroRue){
        String hql = "update Adresse set numeroRue= :numeroRue where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("numeroRue",numeroRue);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void changeRue(int adresse_id,String rue){
        String hql = "update Adresse set rue= :rue where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("rue",rue);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void changePays(int adresse_id,String pays){
        String hql = "update Adresse set pays= :pays where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("pays",pays);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void changeQuartier(int adresse_id,String quartier){
        String hql = "update Adresse set quartier= :quartier where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("quartier",quartier);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void changeVille(int adresse_id,String ville){
        String hql = "update Adresse set ville= :ville where adresse_id = :adresse_id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("adresse_id",adresse_id);
            query.setParameter("ville",ville);
            query.executeUpdate();
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }
    }



