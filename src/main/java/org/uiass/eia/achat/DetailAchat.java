package org.uiass.eia.achat;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity(name="DetailAchat")
@Table(name="detailAchat")
public class DetailAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "achat_id")
    private Achat achatObjet;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produitObjet;

    private int qteAchetee;
    private double prixAchat;
    private double reduction;

    public DetailAchat(Achat achatObjet, Produit produitObjet, int qteAchetee, double prixAchat, double reduction) {
        this.achatObjet = achatObjet;
        this.produitObjet = produitObjet;
        this.qteAchetee = qteAchetee;
        this.prixAchat = prixAchat;
        this.reduction = reduction;
    }

    public DetailAchat(Produit produitObjet, int qteAchetee, double prixAchat, double reduction) {
        this.produitObjet = produitObjet;
        this.qteAchetee = qteAchetee;
        this.prixAchat = prixAchat;
        this.reduction = reduction;
    }

    public DetailAchat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Achat getAchatObjet() {
        return achatObjet;
    }

    public void setAchatObjet(Achat achatObjet) {
        this.achatObjet = achatObjet;
    }

    public Produit getProduitObjet() {
        return produitObjet;
    }

    public void setProduitObjet(Produit produitObjet) {
        this.produitObjet = produitObjet;
    }

    public int getQteAchetee() {
        return qteAchetee;
    }

    public void setQteAchetee(int qteAchetee) {
        this.qteAchetee = qteAchetee;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }
}
