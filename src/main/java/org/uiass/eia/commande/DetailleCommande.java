package org.uiass.eia.commande;

import javax.persistence.*;
import org.uiass.eia.achat.Produit;

@Entity
@Table(name="DetailleCommande")
public class DetailleCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailCommande_id;

    @Column(name="quantite_commander")
    private int qteCommander;

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

    public DetailleCommande(Commande commandeObjet,Produit produitObjet,int qteCommander, double remise, double prixCommannde) {
        this.commandeObjet=commandeObjet;
        this.produitObjet=produitObjet;
        this.qteCommander = qteCommander;
        this.remise = remise;
        this.prixCommannde=prixCommannde;
    }
    public DetailleCommande( Produit produitObjet,int qteCommander, double remise, double prixCommannde) {
        this.produitObjet=produitObjet;
        this.qteCommander = qteCommander;
        this.remise = remise;
        this.prixCommannde=prixCommannde;
    }
    public DetailleCommande(int qteCommander,double remise){
        this.qteCommander=qteCommander;
        this.remise=remise;
    }

    // Getters and Setters
    public int getQuantiteCommander() {
        return qteCommander;
    }

    public void setQuantiteCommander(int quantiteCommander) {
        this.qteCommander = quantiteCommander;
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

        return qteCommander * (1 - remise) * prixCommannde;
    }

    public long getDetailCommande_id() {
        return detailCommande_id;
    }

    public void setDetailCommande_id(int detailCommande_id) {
        this.detailCommande_id = detailCommande_id;
    }
    public Commande getCommandeObjet() {
        return commandeObjet;
    }

    public void setCommandeObjet(Commande commandeObjet) {
        this.commandeObjet = commandeObjet;
    }

    // toString method
    @Override
    public String toString() {
        return "DetailleCommande{" +
                "qteCommander=" + qteCommander +
                ", remise=" + remise +
                '}';
    }

    public Produit getProduit() {
        return this.produitObjet;
    }
}
