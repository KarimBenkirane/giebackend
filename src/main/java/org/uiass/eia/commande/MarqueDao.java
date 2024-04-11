package org.uiass.eia.commande;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;

public class MarqueDao {
    private EntityManager em;
    private EntityTransaction tr;
    private static MarqueDao marqueDao;

    // Singleton pattern
    public static MarqueDao getInstance() {
        if (marqueDao == null)
            marqueDao = new MarqueDao();
        return marqueDao;
    }

    public List<Marque> getAllMarques() {
        Query query = em.createQuery("from Marque");
        return query.getResultList();
    }

    public void addMarque(String nomMarque) {
        try {
            tr.begin();
            em.persist(new Marque(nomMarque));
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void addMarque(Marque marque) {
        try {
            tr.begin();
            em.persist(marque);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public Marque findMarqueById(int id) {
        return em.find(Marque.class, id);
    }
    public Marque findMarqueByName(String name) {
        return em.find(Marque.class, name);
    }

    public void deleteMarqueById(int id) {
        String hql = "delete from Marque where id = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public void deleteMarqueByName(String name) {
        String hql = "delete from Marque where id = :name";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("name", name);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void updateMarqueNom(int id, String nomMarque) {
        String hql = "update Marque set nomMarque = :nomMarque where id = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("nomMarque", nomMarque);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
}
