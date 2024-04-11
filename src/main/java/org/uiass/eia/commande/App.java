package org.uiass.eia.commande;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class App {
    private EntityManager em;
    private EntityTransaction tr;

    public App() {
        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gie-backend");

        // Création de l'EntityManager
        em = emf.createEntityManager();

        // Début de la transaction
        tr = em.getTransaction();
    }

    public static void main(String[] args) {
        App app = new App();
        app.testEntities();
        app.close();
    }

    public void testEntities() {
        try {
            //Début de la transaction
            tr.begin();
// Création d'une instance de Commande
            Commande commande = new Commande(LocalDate.now(), LocalDate.now(), 100.0, EtatCmd.EN_ATTENTE);

// Création d'une instance de DetailleCommande
/*
            DetailleCommande detailleCommande = new DetailleCommande(5, 10.0);
*/

// Création d'une instance de Produit
           /* Marque m =new Marque("samsung");
            CategorieProduit cp = new CategorieProduit("tablette");
            Produit produit = new Produit("Produit 1", "REF001", "Libellé du Produit 1", 50,m,cp);
*/
// Création d'une instance de Marque
            Marque marque = new Marque("Marque 1");

// Création d'une instance de CategorieProd
            CategorieProduit categorieProd = new CategorieProduit("Catégorie 1");


            // Persistez les entités
            em.persist(commande);
           // em.persist(detailleCommande);
           // em.persist(produit);
            em.persist(marque);
            em.persist(categorieProd);

            // Validation de la transaction
            tr.commit();

            System.out.println("Entities persisted successfully.");
        } catch (Exception e) {
            if (tr.isActive()) {
                // Annulation de la transaction en cas d'erreur
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    public void close() {
        // Fermeture de l'EntityManager
        if (em != null) {
            em.close();
        }
    }
}
