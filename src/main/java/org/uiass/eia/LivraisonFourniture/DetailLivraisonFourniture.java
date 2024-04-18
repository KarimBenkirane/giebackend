package org.uiass.eia.LivraisonFourniture;

import org.uiass.eia.commande.Produit;

import javax.persistence.*;

@Entity
@Table(name = "DetailLivraisonFourniture")
public class DetailLivraisonFourniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailLivraisonFourniture_id;

    @Column(name="quantite_commander")
    private int quantiteCommander;

    @ManyToOne
    @JoinColumn(name = "Fournisseur_id")
    private Fournisseur fourniseur;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    public DetailLivraisonFourniture(){
    }
    public DetailLivraisonFourniture(int quttCommande,Fournisseur fourniseur,Produit produit){
        this.quantiteCommander=quttCommande;
        this.produit=produit;
        this.fourniseur=fourniseur;
    }

}
