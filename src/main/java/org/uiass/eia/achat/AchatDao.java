package org.uiass.eia.achat;

import org.uiass.eia.crm.Contact;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.Date;
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

    public List<DetailAchat> getDetailsAchats(long achat_id){
        Achat achat = em.find(Achat.class, achat_id);
        if(achat == null){
            throw new EntityNotFoundException("Achat not found with this ID.");
        }
        return achat.getDetailsAchat();
    }

    public String getDateAchat(long achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getDateAchat().toString();
    }

    public Contact getFournisseurAchat(long achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getFournisseur();
    }

    public double getPrixAchat(long achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getPrix();
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

    public List<Achat> getAchatsBetweenDates(Date dateBefore, Date dateAfter){
        TypedQuery<Achat> query = em.createQuery("SELECT a FROM Achat a WHERE a.dateAchat BETWEEN :dateBefore AND :dateAfter", Achat.class);
        query.setParameter("dateBefore", dateBefore);
        query.setParameter("dateAfter", dateAfter);
        return query.getResultList();
    }

    public List<Achat> getAchatsAfterDate(Date dateAfter){
        TypedQuery<Achat> query = em.createQuery("from Achat a where a.dateAchat >= :dateAfter", Achat.class);
        query.setParameter("dateAfter", dateAfter);
        return query.getResultList();
    }

    public List<Achat> getAchatsBeforeDate(Date dateBefore){
        TypedQuery<Achat> query = em.createQuery("from Achat a where a.dateAchat <= :dateBefore", Achat.class);
        query.setParameter("dateBefore", dateBefore);
        return query.getResultList();
    }

    public List<Achat> getAchatsByDate(Date date){
        TypedQuery<Achat> query = em.createQuery("from Achat a where a.dateAchat = :date", Achat.class);
        query.setParameter("date", date);
        return query.getResultList();
    }


    public Achat getAchatByID(long id){
        Achat achat = em.find(Achat.class,id);
        if(achat == null){
            throw new EntityNotFoundException("Achat not found with this ID");
        }
        return achat;
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

    public void deleteAchatByID(long id){
        Achat achat = this.getAchatByID(id);
        this.deleteAchat(achat);
    }

    public void changeFournisseurAchat(long achat_id, Contact fournisseur){
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

    public void changePrixAchat(long achat_id,double prix){
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

    public void changeDetailsAchat(long achat_id,List<DetailAchat> detailAchats){
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

    public void changeDateAchat(long achat_id, Date date){
        try{
            tr.begin();
            Achat achat = this.getAchatByID(achat_id);
            if(achat.getDateAchat() == null || !achat.getDateAchat().equals(date))
                achat.setDateAchat(date);
            em.flush();
            tr.commit();
        }catch(Exception e){
            tr.rollback();
            System.out.println(e);
        }
    }

    public void changeStatutAchat(long achat_id,String statut){
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
