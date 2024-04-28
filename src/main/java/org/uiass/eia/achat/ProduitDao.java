package org.uiass.eia.achat;

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

    //Singleton pattern
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
        TypedQuery<Produit> query = em.createQuery("from Produit", Produit.class);
        return query.getResultList();
    }


    public List<Produit> getAllAvailableProduits() {
        TypedQuery<Produit> query = em.createQuery("from Produit p where p.qteStock > 0", Produit.class);
        return query.getResultList();
    }


    public List<Produit> getProduitsByMarque(String marque) {
        TypedQuery<Produit> query = em.createQuery("from Produit p where p.marque = :marque", Produit.class);
        query.setParameter("marque", marque);
        return query.getResultList();
    }


    public List<Produit> getProduitsByPriceRange(double prixMin, double prixMax) {
        TypedQuery<Produit> query = em.createQuery("from Produit p where p.prix between :prixMin and :prixMax", Produit.class);
        query.setParameter("prixMin", prixMin);
        query.setParameter("prixMax", prixMax);
        return query.getResultList();
    }

    public Produit getProduitByID(int id){
        return em.find(Produit.class,id);
    }


    public void deleteProduit(Produit prod) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            Produit entity = em.find(Produit.class, prod.getId());
            if (entity != null) {
                em.remove(entity);
            }
            tr.commit();

        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void deleteProduitByID(int id){
        Produit produit = this.getProduitByID(id);
        this.deleteProduit(produit);
    }


    public void addProduit(Produit produit) {
        // TODO Auto-generated method stub
        try {
            tr.begin();
            em.persist(produit);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }


}
