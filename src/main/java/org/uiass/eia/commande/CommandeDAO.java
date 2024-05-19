package org.uiass.eia.commande;


import org.uiass.eia.achat.Achat;
import org.uiass.eia.achat.CategorieProduit;
import org.uiass.eia.achat.Produit;
import org.uiass.eia.achat.StatutAchat;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.*;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.ArrayList;
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
    public List<Commande> getAllCommande() {
        TypedQuery<Commande> query = em.createQuery("from Commande", Commande.class);
        return query.getResultList();
    }
    public List<Commande> getAllCommandeAnnules(){
        TypedQuery<Commande> query = em.createQuery("from Commande where etatCommande = 'Annuler'",Commande.class);
        return query.getResultList();
    }
    public List<Commande> getAllCommandeEffectues(){
        TypedQuery<Commande> query = em.createQuery("from Commande where etatCommande = 'TERMINE'",Commande.class);
        return query.getResultList();
    }
    public List<Commande> getAllCommandeEnCours(){
        TypedQuery<Commande> query = em.createQuery("from Commande where etatCommande = 'EN_COURS'",Commande.class);
        return query.getResultList();
    }
    public List<DetailleCommande> getDetailsCommande(long commande_id){
        Commande commande = em.find(Commande.class, commande_id);
        if(commande == null){
            throw new EntityNotFoundException("Commande not found with this ID.");
        }
        return commande.getDetailsCommandes();
    }
    public Date getDateCommande(long num_bon_commande){
        Commande commande = this.getCommandeByID(num_bon_commande);
        return commande.getDateCommande();
    }
    public Date getDateReglement(long num_bon_commande){
        Commande commande = this.getCommandeByID(num_bon_commande);
        return commande.getDateReglement();
    }
    public Contact getClientCommande(long num_bon_commande){
        Commande commande = this.getCommandeByID(num_bon_commande);
        return commande.getContact();
    }

    public double getPrixCommande(long num_bon_commande){
        Commande commande = this.getCommandeByID(num_bon_commande);
        return commande.getTotalCommande();
    }

    public void addCommande(Contact contact, List<DetailleCommande> detailleCommande, Date dateCommande, Date dateReglement, double totalCommande, EtatCmd etatCommande) {
        try {
            tr.begin();
            Commande commande = new Commande(contact, detailleCommande, dateCommande, dateReglement, totalCommande, etatCommande);
            em.persist(commande);
            tr.commit();
        } catch (Exception e) {
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
    public Commande getCommandeByID(long id){
        Commande commande = em.find(Commande.class,id);
        if(commande == null){
            throw new EntityNotFoundException("Commande not found with this ID");
        }
        return commande;
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
    public List<Commande> getCommandesByDate(Date date){
        TypedQuery<Commande> query= em.createQuery("from Commande where dateCommande = date ",Commande.class);
        query.setParameter("dateCommande",date);
        return query.getResultList();
    }
    public boolean deleteCommande(Commande commande){
        try{
            tr.begin();
            Commande entity = em.find(Commande.class,commande.getNumBonCommande());
            if(entity != null){
                em.remove(entity);
            }
            em.flush();
            tr.commit();
            return true;
        }catch (Exception e) {
            tr.rollback();
            System.out.println(e);
            return false;
        }
    }
    public boolean deleteCommandeByID(long id){
        Commande commande = this.getCommandeByID(id);
        return this.deleteCommande(commande);
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
    public List<Commande> getCommandesByClient(Contact client){
        int contact_id = client.getId();
        String hql = "from Commande where contact_id = :contact_id";
        TypedQuery<Commande> query = em.createQuery(hql, Commande.class);
        query.setParameter("contact_id", contact_id);
        return query.getResultList();
    }

    public List<Commande> getCommandesByClient(int contact_id){
        String hql = "from Commande where contact_id = :contact_id";
        TypedQuery<Commande> query = em.createQuery(hql, Commande.class);
        query.setParameter("contact_id", contact_id);
        return query.getResultList();
    }

    public void updateCommandeTotal(long id, double newTotal) {
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
    public void changeStatutAchat(long commande_id,String etat){
        try{
            tr.begin();
            Commande commande = this.getCommandeByID(commande_id);
            if(commande.getEtatCommande() == null || !commande.getEtatCommande().equals(etat))
                commande.setEtatCommande(EtatCmd.valueOf(etat.toUpperCase()));
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }
    public void deleteAllDetailCommande(long id) {
        tr.begin();
        Query query = em.createQuery("DELETE FROM DetailleCommande d WHERE d.commandeObjet.id = :commandeId");
        query.setParameter("commandeId", id);
        query.executeUpdate();
        em.flush();
        tr.commit();
    }

    public List<Commande> getCommandesByCriteria(String client, String statut, String dateApres, String dateAvant, Double prixMin, Double prixMax) {
        StringBuilder queryString = new StringBuilder("SELECT a FROM Commande a JOIN a.client f WHERE 1=1");

        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        if (client != null && !client.isEmpty()) {
            queryString.append(" AND (");

            // Condition for Particulier
            queryString.append(" (TYPE(f) = Particulier AND f.nom LIKE ?").append(paramIndex).append(")");
            parameters.add("%" + client + "%");
            paramIndex++;

            queryString.append(" OR ");

            // Condition for Entreprise
            queryString.append(" (TYPE(f) = Entreprise AND f.raisonSociale LIKE ?").append(paramIndex).append(")");
            parameters.add("%" + client + "%");
            paramIndex++;

            queryString.append(")");
        }

        if (statut != null && !statut.isEmpty()) {
            queryString.append(" AND a.etatCommande = ?").append(paramIndex);
            parameters.add(EtatCmd.valueOf(statut));
            paramIndex++;
        }

        if (dateApres != null && !dateApres.isEmpty()) {
            queryString.append(" AND a.dateCommande >= ?").append(paramIndex);
            parameters.add(java.sql.Date.valueOf(dateApres));
            paramIndex++;
        }

        if (dateAvant != null && !dateAvant.isEmpty()) {
            queryString.append(" AND a.dateCommande <= ?").append(paramIndex);
            parameters.add(java.sql.Date.valueOf(dateAvant));
            paramIndex++;
        }

        if (prixMin != null) {
            queryString.append(" AND a.totalCommande >= ?").append(paramIndex);
            parameters.add(prixMin);
            paramIndex++;
        }

        if (prixMax != null) {
            queryString.append(" AND a.totalCommande <= ?").append(paramIndex);
            parameters.add(prixMax);
            paramIndex++;
        }

        TypedQuery<Commande> query = em.createQuery(queryString.toString(), Commande.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        return query.getResultList();
    }



}


