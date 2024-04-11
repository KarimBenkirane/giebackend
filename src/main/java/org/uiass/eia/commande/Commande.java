package org.uiass.eia.commande;

import org.uiass.eia.crm.Adresse;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Commande")
@Table(name="Commande")
public class Commande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numBonCommande")
    private int numBonCommande;
    @Column(name = "dateCommande")
    private LocalDate dateCommande;
    @Column(name ="dateReglement")
    private LocalDate dateReglement;
    @Column(name = "totalCommande")
    private double totalCommande;
    @Column(name = "etatCommande")
    private EtatCmd etatCommande;

    @OneToOne
    @JoinColumn(name = "detailCommande_id")
    protected DetailleCommande detailleCommande;

    public Commande(LocalDate dateCommande, LocalDate dateReglement, double totalCommande, EtatCmd etatCommande,DetailleCommande detailleCommande) {
        this.dateCommande = dateCommande;
        this.dateReglement = dateReglement;
        this.totalCommande = totalCommande;
        this.etatCommande = etatCommande;
        this.detailleCommande=detailleCommande;
    }

    // Getters and Setters
    public int getNumBonCommande() {
        return numBonCommande;
    }

    public void setNumBonCommande(int numBonCommande) {
        this.numBonCommande = numBonCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
    }

    public double getTotalCommande() {
        return totalCommande;
    }

    public void setTotalCommande(double totalCommande) {
        this.totalCommande = totalCommande;
    }

    public EtatCmd getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(EtatCmd etatCommande) {
        this.etatCommande = etatCommande;
    }

    // toString method
    @Override
    public String toString() {
        return "Commande{" +
                "numBonCommande='" + numBonCommande + '\'' +
                ", dateCommande=" + dateCommande +
                ", dateReglement=" + dateReglement +
                ", totalCommande=" + totalCommande +
                ", etatCommande=" + etatCommande +
                '}';
    }
}
