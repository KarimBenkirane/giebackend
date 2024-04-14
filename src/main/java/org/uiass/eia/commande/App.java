package org.uiass.eia.commande;

import org.hibernate.mapping.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Collections;

public class App {

    public static void main(String[] args) {

        /*EntityManagerFactory factory = Persistence.createEntityManagerFactory("gie-backend");
        EntityManager em = factory.createEntityManager();

        try {
            em.getTransaction().begin();
            CategorieProduit categorie = new CategorieProduit("Electronique");
            Marque marque = new Marque("Samsung");
            Produit produit = new Produit("Téléphone", "S20", "Smartphone haut de gamme", 100, marque, categorie);
            DetailleCommande detailleCommande = new DetailleCommande(2, 0.0);
            Commande commande = new Commande(LocalDate.now(), LocalDate.now(), 2000.0, EtatCmd.EN_COURS, Collections.singletonList(detailleCommande));
            em.persist(categorie);
            em.persist(marque);
            em.persist(produit);
            em.persist(detailleCommande);
            em.persist(commande);

            em.getTransaction().commit();

            System.out.println("Commande persistée : " + commande);

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            factory.close();
        }*/
        ProduitDao c=  ProduitDao.getInstance();
        for( Produit cmd: c.getAllProduits()){
            System.out.println(cmd);
        }



    }
}
