package org.uiass.eia.achat;

import jdk.jshell.spi.ExecutionControlProvider;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class AchatDao {

    private EntityManager em;
    private EntityTransaction tr;
    private static AchatDao achatDao;

    public static AchatDao getInstance(){
        if(achatDao == null){
            achatDao = new AchatDao();
        }
        return achatDao;
    }

    private AchatDao(){
        this.em = HibernateUtility.getEntityManger();
        this.tr = em.getTransaction();
    }

    public List<Achat> getAllAchats(){
        TypedQuery<Achat> query = em.createQuery("from Achat",Achat.class);
        return query.getResultList();
    }

    public List<Achat> getAllAchatsAnnules(){
        TypedQuery<Achat> query = em.createQuery("from Achat where statut_achat = 'annule'",Achat.class);
        return query.getResultList();
    }

    public List<Achat> getAllAchatsEffectues(){
        TypedQuery<Achat> query = em.createQuery("from Achat where statut_achat = 'effectue'",Achat.class);
        return query.getResultList();
    }

    public List<Achat> getAllAchatsEnCours(){
        TypedQuery<Achat> query = em.createQuery("from Achat where statut_achat = 'en_cours'",Achat.class);
        return query.getResultList();
    }

    public List<DetailAchat> getDetailsAchats(int achat_id){
        Achat achat = em.find(Achat.class, achat_id);
        if(achat == null){
            throw new EntityNotFoundException("Achat not found with this ID.");
        }
        return achat.getDetailsAchat();
    }

    public String getDateAchat(int achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getDateAchat().toString();
    }

    public Contact getFournisseurAchat(int achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getFournisseur();
    }

    public double getPrixAchat(int achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getPrixTotalAchat();
    }

    public void addAchat(Achat achat){
        try {
            tr.begin();
            em.persist(achat);
            tr.commit();

        }
        catch(Exception e) {
            tr.rollback();
            System.out.println(e);

        }
    }

    public void deleteAchat(Achat achat){
        try{
            tr.begin();
            Achat entity = em.find(Achat.class,achat.getId());
            if(entity != null){
                em.remove(entity);
            }
            em.flush();
            tr.commit();
        }catch (Exception e) {
            tr.rollback();
            System.out.println(e);
        }
    }

    public Achat getAchatByID(int id){
        Achat achat = em.find(Achat.class,id);
        if(achat == null){
            throw new EntityNotFoundException("Achat not found with this ID");
        }
        return achat;
    }

    public void deleteAchatByID(int id){
        Achat achat = this.getAchatByID(id);
        this.deleteAchat(achat);
    }

    public void changeFournisseurAchat(int achat_id, Contact fournisseur){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            if(achat.getFournisseur() == null || !achat.getFournisseur().equals(fournisseur)){
                achat.setFournisseur(fournisseur);
            }
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changePrixAchat(int achat_id,double prix){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            achat.setPrix(prix);
            em.flush();
            tr.commit();
        }catch (Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeDetailsAchat(int achat_id,List<DetailAchat> detailAchats){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            if(achat.getDetailsAchat() == null || !achat.getDetailsAchat().equals(detailAchats))
                achat.setDetailsAchat(detailAchats);
            em.flush();
            tr.commit();
        }catch (Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeDateAchat(int achat_id, LocalDate date){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            if(achat.getDateAchat() == null || achat.getDateAchat().equals(date))
                achat.setDateAchat(date);
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeStatutAchat(int achat_id,String statut){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            if(achat.getStatutAchat() == null || !achat.getStatutAchat().equals(statut))
                achat.setStatutAchat(StatutAchat.valueOf(statut.toUpperCase()));
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

}
