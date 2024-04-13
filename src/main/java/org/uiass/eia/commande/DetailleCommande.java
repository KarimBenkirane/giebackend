package org.uiass.eia.commande;

import javax.persistence.*;

@Entity
@Table(name="DetailleCommande")
public class DetailleCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailCommande_id;

    @Column(name="quantite_commander")
    private int quantiteCommander;

    @Column(name="remise")
    private double remise;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    public DetailleCommande() {
    }

    public DetailleCommande(int quantiteCommander, double remise) {
        this.quantiteCommander = quantiteCommander;
        this.remise = remise;
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

    // toString method
    @Override
    public String toString() {
        return "DetailleCommande{" +
                "quantiteCommander=" + quantiteCommander +
                ", remise=" + remise +
                '}';
    }
}
