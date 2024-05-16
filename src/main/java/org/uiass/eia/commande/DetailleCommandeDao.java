package org.uiass.eia.commande;

import org.uiass.eia.achat.Produit;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.*;
import java.util.List;

public class DetailleCommandeDao {
    private EntityManager em;
    private EntityTransaction tr;
    private static DetailleCommandeDao detailleCommandeDao;

    // Singleton pattern
    public static DetailleCommandeDao getInstance() {
        if (detailleCommandeDao == null)
            detailleCommandeDao = new DetailleCommandeDao();
        return detailleCommandeDao;
    }
    public DetailleCommandeDao(){
        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }

    public List<DetailleCommande> getAllDetailleCommandes() {
        Query query = em.createQuery("from DetailleCommande");
        return query.getResultList();
    }

    public void addDetailleCommande(Commande c, Produit p, int quantiteCommander, double remise, double prix) {
        try {
            tr.begin();
            em.persist(new DetailleCommande(c,p,quantiteCommander, remise,prix));
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public void addDetailleCommande(DetailleCommande detailleCommande) {
        try {
            tr.begin();
            em.persist(detailleCommande);
            tr.commit();

        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public DetailleCommande findDetailleCommandeById(int id) {
        return em.find(DetailleCommande.class, id);
    }

    public void deleteDetailleCommandeById(int id) {
        String hql = "delete from DetailleCommande where id = :id";
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

    public void updateDetailleCommandeQuantiteAndRemise(int id, int quantiteCommander, double remise) {
        String hql = "update DetailleCommande set quantiteCommander = :quantiteCommander, remise = :remise where id = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("quantiteCommander", quantiteCommander);
            query.setParameter("remise", remise);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
}
