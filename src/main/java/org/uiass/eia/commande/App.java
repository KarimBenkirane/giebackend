package org.uiass.eia.commande;

import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.Particulier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gie-backend");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Adresse ad = new Adresse("123 Rue du Client", 123, "Quartier", 12345, "Ville", "Pays");

            Marque marque1 = new Marque("Marque A");
            Marque marque2 = new Marque("Marque B");

            CategorieProduit categorie1 = new CategorieProduit("Catégorie 1");
            CategorieProduit categorie2 = new CategorieProduit("Catégorie 2");

            Produit produit1 = new Produit("Produit 1", "REF1", "Libelle 1", 10, marque1, categorie1);
            Produit produit2 = new Produit("Produit 2", "REF2", "Libelle 2", 20, marque2, categorie2);


            // Créer une instance de Date représentant la date actuelle
            Date dateActuelle = new Date(System.currentTimeMillis());

            // Calculer la date 5 jours plus tard en millisecondes
            long cinqJoursEnMillisecondes = 5 * 24 * 60 * 60 * 1000;
            Date dateReglement = new Date(System.currentTimeMillis() + cinqJoursEnMillisecondes);

            // Créer une instance de Commande avec les objets Date
            Commande commande = new Commande();
            commande.setDateCommande(dateActuelle);
            commande.setDateReglement(dateReglement);
            commande.setTotalCommande(100.0);
            commande.setEtatCommande(EtatCmd.EN_COURS);

            // Calculer le prix de la première DetailleCommande
            double prixDetaille1 = 2 * (1 - 0.0) * produit1.getPrix();

            // Calculer le prix de la deuxième DetailleCommande
            double prixDetaille2 = 3 * (1 - 0.1) * produit2.getPrix();

            DetailleCommande detaille1 = new DetailleCommande(commande, produit1, 2, 0.0, prixDetaille1);
            DetailleCommande detaille2 = new DetailleCommande(commande, produit2, 3, 0.1, prixDetaille2);

            commande.setDetailsCommandes(Arrays.asList(detaille1, detaille2));

            detaille1.setCommande(commande);
            detaille1.setProduit(produit1);
            detaille2.setCommande(commande);
            detaille2.setProduit(produit2);

            // Ajout du client à la commande
            Contact client = new Particulier("123456789", "client@example.com", "123456789", ad,"araoui","khalil");
            commande.setContact(client);

            em.persist(marque1);
            em.persist(marque2);
            em.persist(categorie1);
            em.persist(categorie2);
            em.persist(produit1);
            em.persist(produit2);
            em.persist(client);
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
