package org.uiass.eia.achat;

import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.*;
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

    public List<Produit> getAllUnavailableProduits() {
        TypedQuery<Produit> query = em.createQuery("from Produit p where p.qteStock = 0", Produit.class);
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

    public Produit getProduitByID(long id){
        Produit produit =  em.find(Produit.class,id);
        if(produit == null)
            throw new EntityNotFoundException("Produit not found with this ID");
        return produit;
    }


    public boolean deleteProduit(Produit prod) {
        try {
            tr.begin();
            Produit entity = em.find(Produit.class, prod.getId());
            if (entity != null) {
                em.remove(entity);
            }
            tr.commit();
            return true;

        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
            return false;
        }
    }

    public boolean deleteProduitByID(long id){
        Produit produit = this.getProduitByID(id);
        return this.deleteProduit(produit);
    }


    public void addProduit(Produit produit) {
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

    public void changeMarqueProduit(long id, String marque) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(produit.getMarque() == null || !produit.getMarque().equals(marque)){
                produit.setMarque(marque);
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changePrixProduit(long id, double prix) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(!(produit.getPrix() == prix)){
                produit.setPrix(prix);
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeModeleProduit(long id, String modele) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(produit.getModele() == null || !produit.getModele().equals(modele)){
                produit.setModele(modele);
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeQteStockProduit(long id, int qte) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(!(produit.getQteStock() == qte)){
                produit.setQteStock(qte);
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeDescriptionProduit(long id, String description) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(produit.getDescription() == null || !produit.getDescription().equals(description)){
                produit.setDescription(description);

            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeCategorieProduit(long id, String categorie) {
        try{
            tr.begin();
            Produit produit = this.getProduitByID(id);
            if(produit.getCategorieProduit() == null || !produit.getCategorieProduit().equals(CategorieProduit.valueOf(categorie.toUpperCase()))){
                produit.setCategorieProduit(CategorieProduit.valueOf(categorie.toUpperCase()));
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public List getAllCategories(){
        Query query = em.createNativeQuery("select distinct categorie from produit");
        return query.getResultList();
    }



}
