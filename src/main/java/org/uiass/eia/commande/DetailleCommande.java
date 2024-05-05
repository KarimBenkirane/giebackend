package org.uiass.eia.commande;

import javax.persistence.*;

@Entity
@Table(name="DetailleCommande")
public class DetailleCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailCommande_id;

    @Column(name="quantite_commander")
    private int quantiteCommander;

    @Column(name="remise")
    private double remise;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commandeObjet;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produitObjet;

    private double prixCommannde;



    public DetailleCommande() {
    }

    public double getPrixCommannde() {
        return prixCommannde;
    }

    public DetailleCommande(Commande commandeObjet,Produit produitObjet,int quantiteCommander, double remise, double prixCommannde) {
        this.commandeObjet=commandeObjet;
        this.produitObjet=produitObjet;
        this.quantiteCommander = quantiteCommander;
        this.remise = remise;
        this.prixCommannde=prixCommannde;
    }
    public DetailleCommande(int quantiteCommander,double remise){
        this.quantiteCommander=quantiteCommander;
        this.remise=remise;
    }

    // Getters and Setters
    public int getQuantiteCommander() {
        return quantiteCommander;
    }

    public void setQuantiteCommander(int quantiteCommander) {
        this.quantiteCommander = quantiteCommander;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }
    public void setCommande(Commande commande) {
        this.commandeObjet = commande;
    }

    public void setProduit(Produit produit) {
        this.produitObjet = produit;
    }

    public double calculerPrix() {

        return quantiteCommander * (1 - remise) * prixCommannde;
    }

    public long getDetailCommande_id() {
        return detailCommande_id;
    }

    public void setDetailCommande_id(long detailCommande_id) {
        this.detailCommande_id = detailCommande_id;
    }

    // toString method
    @Override
    public String toString() {
        return "DetailleCommande{" +
                "quantiteCommander=" + quantiteCommander +
                ", remise=" + remise +
                '}';
    }

    public Produit getProduit() {
        return this.produitObjet;
    }
}
