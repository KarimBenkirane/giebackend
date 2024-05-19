package org.uiass.eia.achat;

import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.Entreprise;
import org.uiass.eia.crm.Particulier;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
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

    public Date getDateAchat(long achat_id){
        Achat achat = this.getAchatByID(achat_id);
        return achat.getDateAchat();
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
        TypedQuery<Achat> query = em.createQuery("SELECT a FROM Achat a WHERE a.dateAchat BETWEEN :dateAfter AND :dateBefore", Achat.class);
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

    public boolean deleteAchat(Achat achat){
        try{
            tr.begin();
            Achat entity = em.find(Achat.class,achat.getId());
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

    public boolean deleteAchatByID(long id){
        Achat achat = this.getAchatByID(id);
        return this.deleteAchat(achat);
    }

    public List<Achat> getAchatsByFournisseur(Contact contact){
        int fournisseur_id = contact.getId();
        String hql = "from Achat where fournisseur_id = :fournisseur_id";
        TypedQuery<Achat> query = em.createQuery(hql, Achat.class);
        query.setParameter("fournisseur_id", fournisseur_id);
        return query.getResultList();
    }

    public List<Achat> getAchatsByFournisseur(int fournisseur_id){
        String hql = "from Achat where fournisseur_id = :fournisseur_id";
        TypedQuery<Achat> query = em.createQuery(hql, Achat.class);
        query.setParameter("fournisseur_id", fournisseur_id);
        return query.getResultList();
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


    public void deleteAllDetailAchats(long id) {
        tr.begin();
        Query query = em.createQuery("DELETE FROM DetailAchat d WHERE d.achatObjet.id = :achatId");
        query.setParameter("achatId", id);
        query.executeUpdate();
        em.flush();
        tr.commit();
    }

    public List<String> getAllFournisseurs() {
        List<String> fournisseurs = new ArrayList<>();
        TypedQuery<Contact> query = em.createQuery( "SELECT DISTINCT c FROM Achat a JOIN a.fournisseur c", Contact.class);
        List<Contact> contacts = query.getResultList();
        for(Contact c : contacts){
            if(c instanceof Particulier){
                Particulier p = (Particulier)c;
                fournisseurs.add(p.getNom());
            }
            else{
                Entreprise e = (Entreprise) c;
                fournisseurs.add(e.getRaisonSociale());
            }
        }

        return fournisseurs;

    }


    public List<Achat> getAchatsByCriteria(String fournisseur, String statut, String dateApres, String dateAvant, Double prixMin, Double prixMax) {
        StringBuilder queryString = new StringBuilder("SELECT a FROM Achat a JOIN a.fournisseur f WHERE 1=1");

        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        if (fournisseur != null && !fournisseur.isEmpty()) {
            queryString.append(" AND (");

            // Condition for Particulier
            queryString.append(" (TYPE(f) = Particulier AND f.nom LIKE ?").append(paramIndex).append(")");
            parameters.add("%" + fournisseur + "%");
            paramIndex++;

            queryString.append(" OR ");

            // Condition for Entreprise
            queryString.append(" (TYPE(f) = Entreprise AND f.raisonSociale LIKE ?").append(paramIndex).append(")");
            parameters.add("%" + fournisseur + "%");
            paramIndex++;

            queryString.append(")");
        }

        if (statut != null && !statut.isEmpty()) {
            queryString.append(" AND a.statutAchat = ?").append(paramIndex);
            parameters.add(StatutAchat.valueOf(statut));
            paramIndex++;
        }

        if (dateApres != null && !dateApres.isEmpty()) {
            queryString.append(" AND a.dateAchat >= ?").append(paramIndex);
            parameters.add(java.sql.Date.valueOf(dateApres));
            paramIndex++;
        }

        if (dateAvant != null && !dateAvant.isEmpty()) {
            queryString.append(" AND a.dateAchat <= ?").append(paramIndex);
            parameters.add(java.sql.Date.valueOf(dateAvant));
            paramIndex++;
        }

        if (prixMin != null) {
            queryString.append(" AND a.prix >= ?").append(paramIndex);
            parameters.add(prixMin);
            paramIndex++;
        }

        if (prixMax != null) {
            queryString.append(" AND a.prix <= ?").append(paramIndex);
            parameters.add(prixMax);
            paramIndex++;
        }

        TypedQuery<Achat> query = em.createQuery(queryString.toString(), Achat.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        return query.getResultList();
    }

    public static class ProduitDao {
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
            List<String> categoriesProduits = new ArrayList<>();
            CategorieProduit[] categories = CategorieProduit.values();
            for(CategorieProduit cp : categories){
                categoriesProduits.add(String.valueOf(cp));
            }
            return categoriesProduits;
        }

        public List getAllMarques(){
            Query query = em.createNativeQuery("select distinct marque from produit");
            return query.getResultList();
        }

        public List<Produit> getProduitsByCriteria(String marque, String modele, Integer qteStock, Double prixMin, Double prixMax, String description, String disponibilite, List<String> categoriesList) {
            try{
                System.out.println("début de la méthode DAO -> "+categoriesList);
                String jpql = "SELECT p FROM Produit p WHERE 1=1";
                if (marque != null) {
                    jpql += " AND p.marque = :marque";
                }
                if (modele != null) {
                    jpql += " AND p.modele = :modele";
                }
                if ("disponible".equals(disponibilite)) {
                    jpql += " AND p.qteStock > 0";
                } else if ("indisponible".equals(disponibilite)) {
                    jpql += " AND p.qteStock = 0";
                }
                if (qteStock != null) {
                    jpql += " AND p.qteStock > :qteStock";
                }
                if (prixMin != null) {
                    jpql += " AND p.prix >= :prixMin";
                }
                if (prixMax != null) {
                    jpql += " AND p.prix <= :prixMax";
                }
                if (description != null) {
                    jpql += " AND p.description LIKE :description";
                }
                List<CategorieProduit> categorieProduits = null; // Declare outside of the if block

                if (categoriesList != null && !categoriesList.isEmpty()) {
                    categorieProduits = new ArrayList<>();
                    for (String category : categoriesList) {
                        try {
                            CategorieProduit categorieProduit = CategorieProduit.valueOf(category);
                            categorieProduits.add(categorieProduit);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid enum values
                            System.out.println("Invalid category: " + category);
                        }
                    }
                    jpql += " AND p.categorieProduit IN :categories";
                }

                TypedQuery<Produit> query = em.createQuery(jpql, Produit.class);
                if (marque != null) {
                    query.setParameter("marque", marque);
                }
                if (modele != null) {
                    query.setParameter("modele", modele);
                }
                if (qteStock != null) {
                    query.setParameter("qteStock", qteStock);
                }
                if (prixMin != null) {
                    query.setParameter("prixMin", prixMin);
                }
                if (prixMax != null) {
                    query.setParameter("prixMax", prixMax);
                }
                if (description != null) {
                    query.setParameter("description", "%" + description + "%");
                }
                if (categorieProduits != null && !categorieProduits.isEmpty()) {
                    query.setParameter("categories", categorieProduits);
                }

                System.out.println("fin côté DAO ->"+categoriesList);

                return query.getResultList();
            }catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }



    }
}
