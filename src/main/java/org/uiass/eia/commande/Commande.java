package org.uiass.eia.commande;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name="Commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numBonCommande")
    private int numBonCommande;

    @Column(name = "dateCommande")
    private LocalDate dateCommande;

    @Column(name = "dateReglement")
    private LocalDate dateReglement;

    @Column(name = "totalCommande")
    private double totalCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatCommande")
    private EtatCmd etatCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private Collection<DetailleCommande> detaillecommandes;

    public Commande() {
    }

    public Commande(LocalDate dateCommande, LocalDate dateReglement, double totalCommande, EtatCmd etatCommande, Collection<DetailleCommande> detailleCommande) {
        this.dateCommande = dateCommande;
        this.dateReglement = dateReglement;
        this.totalCommande = totalCommande;
        this.etatCommande = etatCommande;
        this.detaillecommandes = detailleCommande;
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
                "numBonCommande=" + numBonCommande +
                ", dateCommande=" + dateCommande +
                ", dateReglement=" + dateReglement +
                ", totalCommande=" + totalCommande +
                ", etatCommande=" + etatCommande +
                '}';
    }
}