package org.uiass.eia.commande;

import org.checkerframework.checker.units.qual.C;
import org.uiass.eia.achat.CategorieProduit;
import org.uiass.eia.achat.Produit;
import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;
import org.uiass.eia.crm.Particulier;
import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



public class Test {
    public static void main(String[] args) {
        Adresse ad = new Adresse("123 Rue du Client", 123, "Quartier", 12345, "Ville", "Pays");
        Produit produit1 = new Produit("marque1", "modle1", 11, 200, "Libelle 1", CategorieProduit.LOGICIELS);
        Produit produit2 = new Produit("marque2", "modle2", 110, 35, "Libelle 2", CategorieProduit.AUDIO_ET_VIDEO);
        Contact client = new Particulier("123456789", "client@example.com", "123456789", ad, "araoui", "khalil");

        Date dateActuelle = new Date(System.currentTimeMillis());
        long cinqJoursEnMillisecondes = 5 * 24 * 60 * 60 * 1000;
        Date dateReglement = new Date(System.currentTimeMillis() + cinqJoursEnMillisecondes);

        double prix = 100.0;
        Commande commande = new Commande(client, null, dateActuelle, dateReglement, prix, EtatCmd.INITIALISÉ);
        double prixDetaille1 = 2 * (1 - 0.0) * produit1.getPrix();
        double prixDetaille2 = 3 * (1 - 0.1) * produit2.getPrix();
        DetailleCommande detaille1 = new DetailleCommande(commande, produit1, 2, 0.0, prixDetaille1);
        DetailleCommande detaille2 = new DetailleCommande(commande, produit2, 3, 0.1, prixDetaille2);

        // Initialize the list
        List<DetailleCommande> detailsCommandes = new ArrayList<>();
        detailsCommandes.add(detaille1);
        detailsCommandes.add(detaille2);
        commande.setDetailsCommandes(detailsCommandes);
/*
        EntityManager entityManager = HibernateUtility.getEntityManger();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            // Persist the Particulier instance (client)
            entityManager.persist(client);

            // Persist the Produit instances
            entityManager.persist(produit1);
            entityManager.persist(produit2);

            // Set the client for the Commande instance
            commande.setClient(client);

            // Save the Commande object
            entityManager.persist(commande);

            // Save the DetailleCommande objects
            entityManager.persist(detaille1);
            entityManager.persist(detaille2);

            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            entityManager.close();
        }*/
        // Initialize the list

        try {
            //ContactDao c=ContactDao.getInstance();
            CommandeDAO cdao=CommandeDAO.getInstance();
            //Contact contact=c.findContactById(1);
            //cdao.addCommande(contact,detailsCommandes,dateActuelle,dateReglement,prix,EtatCmd.LIVRÉ);
            Commande c = cdao.getCommandeByID(6);
            System.out.println(c);
            // cdao.changeStatutAchat(4, String.valueOf(EtatCmd.ANNULÉ));

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*CommandeDAO commandeDAO = CommandeDAO.getInstance();

        // Provide test data
        String clientT = "araoui";
        String statut = "Tous les statuts";
        String dateApres = "2024-01-01";
        String dateAvant = "2024-12-31";
        Double prixMin = 100.0;
        Double prixMax = 500.0;

        // Call the method
        List<Commande> commandes = commandeDAO.getCommandesByCriteria(clientT, statut, dateApres, dateAvant, prixMin, prixMax);

        // Output the result
        for (Commande commandeT : commandes) {
            System.out.println(commandeT);
        }*/
    }
    }

