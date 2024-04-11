package org.uiass.eia.commande;

import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProduitDao {
    private EntityManager em;
    private EntityTransaction tr;
    private static ProduitDao produitDao;

    // Singleton pattern
    public static ProduitDao getInstance() {
        if (produitDao == null)
            produitDao = new ProduitDao();
        return produitDao;
    }

    private ProduitDao() {
        this.em = HibernateUtility.getEntityManger();
        tr = em.getTransaction();
    }

    public List<Produit> getAllProduits() {
        Query query = em.createQuery("from Produit");
        return query.getResultList();
    }

    public Produit findProduitById(int id) {
        return em.find(Produit.class, id);
    }
    public Produit findProduitByRef(String ref) {
        return em.find(Produit.class, ref);
    }

    public void addProduit(String nomProd, String refProd, String libelle, int quantiteStock, Marque marque,CategorieProduit categorieProduit) {
        try {
            tr.begin();
            em.persist(new Produit(nomProd,refProd,libelle,quantiteStock,marque,categorieProduit));
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void addProduit(Produit produit) {
        try {
            tr.begin();
            em.persist(produit);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void deleteProduitById(int id) {
        String hql = "delete from Produit where idProd = :id";
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
    public void deleteProduitByRef(String Ref) {
        String hql = "delete from Produit where refProd = :Ref";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("Ref", Ref);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void updateProduitStock(int id, int newStock) {
        String hql = "update Produit set quantiteStock = :newStock where idProd = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("newStock", newStock);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public void updateProduitRef(int id, String ref) {
        String hql = "update Produit set refProd = :ref where idProd = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("ref", ref);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public void updateProduitLibelle(int id, String libelle) {
        String hql = "update Produit set libelle = :libelle where idProd = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("libelle", libelle);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public List<Produit> findProduitsById(int id) {
        TypedQuery<Produit> query = em.createQuery("from Produit where idProd = :id", Produit.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
    public List<Produit> findProduitsByRef(String ref) {
        TypedQuery<Produit> query = em.createQuery("from Produit where refProd = :ref", Produit.class);
        query.setParameter("ref", ref);
        return query.getResultList();
    }

    public List<Produit> findProduitsByLibelle(String libelle) {
        TypedQuery<Produit> query = em.createQuery("from Produit where libelle = :libelle", Produit.class);
        query.setParameter("libelle", libelle);
        return query.getResultList();
    }
}
