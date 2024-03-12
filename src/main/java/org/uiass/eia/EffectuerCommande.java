package org.uiass.eia;

import javax.persistence.*;


@Entity
@Table(name = "Effectuer_commande")
public class EffectuerCommande {
    @Id
    @Column(name = "ID_Commande")
    private String id_commande;
    @Id
    @Column(name = "Numero_TVA")
    private String numero_tva;
    @Column(name = "Quantite")
    private int quantite;

    public String getId_commande() {
        return id_commande;
    }

    public void setId_commande(String id_commande) {
        this.id_commande = id_commande;
    }

    public String getNumero_tva() {
        return numero_tva;
    }

    public void setNumero_tva(String numero_tva) {
        this.numero_tva = numero_tva;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public EffectuerCommande(String id_commande, String numero_tva, int quantite) {
        this.id_commande = id_commande;
        this.numero_tva = numero_tva;
        this.quantite = quantite;
    }
}
