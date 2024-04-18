package org.uiass.eia.commande;

import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {

        CommandeDAO c = CommandeDAO.getInstance();
        for(Commande cmd : c.getAllCommande()){
            System.out.println(cmd);
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gie-backend");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Marque marque1 = new Marque("Marque A");
            Marque marque2 = new Marque("Marque B");

            CategorieProduit categorie1 = new CategorieProduit("Catégorie 1");
            CategorieProduit categorie2 = new CategorieProduit("Catégorie 2");

            Produit produit1 = new Produit("Produit 1", "REF1", "Libelle 1", 10, marque1, categorie1);
            Produit produit2 = new Produit("Produit 2", "REF2", "Libelle 2", 20, marque2, categorie2);

            DetailleCommande detaille1 = new DetailleCommande(2, 0.0);
            DetailleCommande detaille2 = new DetailleCommande(3, 0.1);

            Commande commande = new Commande(LocalDate.now(), LocalDate.now().plusDays(5), 100.0, EtatCmd.EN_COURS, Arrays.asList(detaille1, detaille2));

            detaille1.setCommande(commande);
            detaille1.setProduit(produit1);
            detaille2.setCommande(commande);
            detaille2.setProduit(produit2);

            // Ajout du client à la commande
           // Contact client = new Contact("123456789", "client@example.com", "123456789", new Adresse("123 Rue du Client", 123, "Quartier", 12345, "Ville", "Pays"));
            //commande.setContact(client);

            em.persist(marque1);
            em.persist(marque2);
            em.persist(categorie1);
            em.persist(categorie2);
            em.persist(produit1);
            em.persist(produit2);
          //  em.persist(client);
            em.persist(commande);
            em.persist(detaille1);
            em.persist(detaille2);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
