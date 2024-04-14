package org.uiass.eia.commande;

import javax.persistence.*;
import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

public class CategorieProduitDao {
    private EntityManager em;
    private EntityTransaction tr;
    private static CategorieProduitDao categorieProduitDao;

    // Singleton pattern
    public static CategorieProduitDao getInstance() {
        if (categorieProduitDao == null)
            categorieProduitDao = new CategorieProduitDao();
        return categorieProduitDao;
    }
    private CategorieProduitDao() {
        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }

    public List<CategorieProduit> getAllCategoriesProduit() {
        Query query = em.createQuery("from CategorieProduit");
        return query.getResultList();
    }

    public void addCategorieProduit(String nomCategorie) {
        try {
            tr.begin();
            em.persist(new CategorieProduit(nomCategorie));
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void addCategorieProduit(CategorieProduit categorieProduit) {
        try {
            tr.begin();
            em.persist(categorieProduit);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public CategorieProduit findCategorieProduitById(int id) {
        return em.find(CategorieProduit.class, id);
    }
    public CategorieProduit findCategorieProduitByName(String name) {
        return em.find(CategorieProduit.class, name);
    }

    public void deleteCategorieProduitById(int id) {
        String hql = "delete from CategorieProduit where id_Cat_Prod = :id";
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
    public void deleteCategorieProduitById(String name) {
        String hql = "delete from CategorieProduit where nom_categorie = :name";
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

    public void updateCategorieProduitNom(int id, String nomCategorie) {
        String hql = "update CategorieProduit set nom_Categorie = :nomCategorie where id_Cat_Prod = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("nomCategorie", nomCategorie);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
}
