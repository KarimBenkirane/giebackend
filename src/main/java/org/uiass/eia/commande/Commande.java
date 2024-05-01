package org.uiass.eia.commande;

import org.uiass.eia.crm.Contact;

import javax.persistence.*;
import java.sql.Date;

import java.util.List;

@Entity
@Table(name="Commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numBonCommande")
    private int numBonCommande;

    @Column(name = "dateCommande")
    private Date dateCommande;

    @Column(name = "dateReglement")
    private Date dateReglement;

    @Column(name = "totalCommande")
    private double totalCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatCommande")
    private EtatCmd etatCommande;

    @OneToOne
    @JoinColumn(name="contact_id")
    private Contact contact;

    @OneToMany(mappedBy = "commandeObjet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailleCommande> detailsCommandes;

    public Commande() {
    }

    public Commande(Contact contact,Date dateCommande, Date dateReglement, double totalCommande, EtatCmd etatCommande, List<DetailleCommande> detailleCommande) {
        this.contact=contact;
        this.dateCommande = dateCommande;
        this.dateReglement = dateReglement;
        this.totalCommande = totalCommande;
        this.etatCommande = etatCommande;
        this.detailsCommandes = detailleCommande;
    }

    // Getters and Setters
    public int getNumBonCommande() {
        return numBonCommande;
    }

    public void setNumBonCommande(int numBonCommande) {
        this.numBonCommande = numBonCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
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
    public void setDateCommandeFormatted(String dateCommandeFormatted) {
        dateCommandeFormatted = String.valueOf(this.dateCommande);
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
    public double getPrixTotalCommande(){
        double montant = 0.0;
        for(DetailleCommande detailCommande : this.detailsCommandes){
            montant += detailCommande.getPrixCommannde();
        }
        return montant;
    }
    public void setDetailsCommandes(List<DetailleCommande> detailsCommandes) {
        this.detailsCommandes = detailsCommandes;
    }



    public void setContact(Contact contact) {
        this.contact = contact;
    }


}