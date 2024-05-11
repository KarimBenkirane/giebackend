package org.uiass.eia.commande;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.uiass.eia.achat.CategorieProduit;
import org.uiass.eia.achat.Produit;
import org.uiass.eia.commande.Commande;
import org.uiass.eia.commande.DetailleCommande;
import org.uiass.eia.commande.EtatCmd;
import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.Particulier;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        // Création de la sessionFactory
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Créer un contact
        Adresse adresse = new Adresse("1 Rue de Test", 1,"Ville",60, "Rbat","Pays");
        Contact contact = new Particulier("0123456789", "test@test.com", "0123456789", adresse,"araoui","khalil");
        Produit produit = new Produit("samsung","nouveau",10,2083,"bon etat", CategorieProduit.LOGICIELS);

        // Créer une commande
        Date dateCommande = new Date(System.currentTimeMillis());
        Date dateReglement = new Date(System.currentTimeMillis());
        List<DetailleCommande> detailsCommandes = new ArrayList<>();
        Commande commande = new Commande(contact, dateCommande, dateReglement, 100.0, EtatCmd.EN_ATTENTE, detailsCommandes);

        // Créer un détail de commande
        DetailleCommande detailleCommande = new DetailleCommande(commande,produit, 5, 0.1, 10.0);
        commande.getDetailsCommandes().add(detailleCommande);

        // Sauvegarder la commande dans la base de données
        saveCommande(commande, sessionFactory);

        // Fermeture de la sessionFactory à la fin
        sessionFactory.close();
    }

    private static void saveCommande(Commande commande, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Enregistrer la commande et ses détails
            session.save(commande);
            for (DetailleCommande detailleCommande : commande.getDetailsCommandes()) {
                session.save(detailleCommande);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
