package org.uiass.eia.commande;

import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;
import org.uiass.eia.crm.Particulier;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {

   /* Adresse ad = new Adresse("123 Rue du Client", 123, "Quartier", 12345, "Ville", "Pays");

    Marque marque1 = new Marque("Marque A");
    Marque marque2 = new Marque("Marque B");

    CategorieProduit categorie1 = new CategorieProduit("Catégorie 1");
    CategorieProduit categorie2 = new CategorieProduit("Catégorie 2");

    Produit produit1 = new Produit("Produit 1", "REF1", "Libelle 1", 10, marque1, categorie1);
    Produit produit2 = new Produit("Produit 2", "REF2", "Libelle 2", 20, marque2, categorie2);
    Contact client = new Particulier("123456789", "client@example.com", "123456789", ad, "araoui", "khalil");


    // Créer une instance de Date représentant la date actuelle
    Date dateActuelle = new Date(System.currentTimeMillis());

    // Calculer la date 5 jours plus tard en millisecondes
    long cinqJoursEnMillisecondes = 5 * 24 * 60 * 60 * 1000;
    Date dateReglement = new Date(System.currentTimeMillis() + cinqJoursEnMillisecondes);

    double prix = 100.0;
    List<DetailleCommande> detailCommandes = new ArrayList<>();
    // Créer une instance de Commande avec les objets Date
    Commande commande = new Commande(client, dateActuelle, dateReglement, prix, EtatCmd.EN_COURS, detailCommandes);
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
*/

    // Ajout du client à la commande

        CommandeDAO cdao = CommandeDAO.getInstance();
        ContactDao contactDao = ContactDao.getInstance();
        Commande c = new Commande();
        try {
           /* Contact client =contactDao.findContactById(1);
            cdao.addCommande(client,Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now()),345.0,EtatCmd.EN_COURS,null);*/
            /*DetailleCommande detaille1 = new DetailleCommande(c, produit1, 2, 0.0, prixDetaille1);
            DetailleCommande detaille2 = new DetailleCommande(c, produit2, 3, 0.1, prixDetaille2);
            commande.setDetailsCommandes(Arrays.asList(detaille1, detaille2));*/

            Commande c2 =cdao.getCommandeByID(1);
            System.out.println(c2);
            //cdao.changeStatutAchat(1, "termine");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
