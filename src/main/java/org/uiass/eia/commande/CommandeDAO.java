package org.uiass.eia.commande;


import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class CommandeDAO {
    private EntityManager em;
    private EntityTransaction tr;
    private static CommandeDAO commandeDAO;

    public static CommandeDAO getInstance(){
        if(commandeDAO ==null){
            commandeDAO=new CommandeDAO();
        }
        return commandeDAO;
    }

    private CommandeDAO() {
        this.em= HibernateUtility.getEntityManger();
        tr=em.getTransaction();
    }
    public List<Commande> getAllCommande(){
        Query query= em.createQuery("from Commande");
        return query.getResultList();
    }
    public void addCommande(LocalDate dateCommande, LocalDate dateReglement, double totalCommande, EtatCmd etatCommande, Collection<DetailleCommande> dtcm){
        try{
            tr.begin();
            em.persist(new Commande( dateCommande,  dateReglement,  totalCommande,  etatCommande,dtcm));
            tr.commit();
        }catch (Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }
    public void addCommande(Commande commande) {
        try {
            tr.begin();
            em.persist(commande);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public Commande getCommandeById(int Id){
        return em.find(Commande.class,Id);
    }
    public List<Commande> getCommandesByEtat(EtatCmd etat) {
        TypedQuery<Commande> query = em.createQuery("from Commande where etatCommande = :etat", Commande.class);
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    public List<Commande> getCommandesByTotal(double totalCommande){
        TypedQuery<Commande> query = em.createQuery("from Commande where totalCommande = :totalCommande",Commande.class);
        query.setParameter("totalCommande",totalCommande);
        return query.getResultList();
    }
    public List<Commande> getCommandesByDate(LocalDate date){
        TypedQuery<Commande> query= em.createQuery("from Commande where dateCommande =: dateCommande ",Commande.class);
        query.setParameter("dateCommande",date);
        return query.getResultList();
    }
    public void deleteCommandeById(int Id) {
        String hql = "delete from Commande" +" where numBonCommande = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", Id);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public void deleteCommandeByTotal(double totalCommande){
        String hql = " delete from Commande "+" where totalCommande = : totalCommande ";
        try{
            tr.begin();
            Query query= em.createQuery(hql);
            query.setParameter("totalCommande",totalCommande);
            query.executeUpdate();
            tr.commit();
        }catch (Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void updateCommandeTotal(int id, double newTotal) {
        String hql = "update Commande set totalCommande= :newTotal where numBonCommande = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("newTotal", newTotal);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }
    public void updateCommandeEtat(int id, EtatCmd etatCmd) {
        String hql = "update Commande set etatCommande= :etatCmd where numBonCommande = :id";
        try {
            tr.begin();
            Query query = em.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("etatCmd", etatCmd);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }


}
