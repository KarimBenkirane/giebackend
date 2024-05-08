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
    private long numBonCommande;

    @OneToOne
    @JoinColumn(name="contact_id")
    private Contact client;

    @Column(name = "dateCommande")
    private Date dateCommande;

    @Column(name = "dateReglement")
    private Date dateReglement;

    @Column(name = "totalCommande")
    private double totalCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatCommande")
    private EtatCmd etatCommande;

    @OneToMany(mappedBy = "commandeObjet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailleCommande> detailsCommandes;

    public Commande(){}


    public Commande(Contact contact,Date dateCommande, Date dateReglement, double totalCommande, EtatCmd etatCommande, List<DetailleCommande> detailleCommande) {
        this.client=contact;
        this.dateCommande = dateCommande;
        this.dateReglement = dateReglement;
        this.totalCommande = totalCommande;
        this.etatCommande = etatCommande;
        this.detailsCommandes = detailleCommande;
    }


    // Getters and Setters
    public long getNumBonCommande() {
        return this.numBonCommande;
    }

    public void setNumBonCommande(long numBonCommande) {
        this.numBonCommande = numBonCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public List<DetailleCommande> getDetailsCommandes() {return detailsCommandes;}

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

    public Contact getContact() {
        return client;
    }

    // toString method
    @Override
    public String toString() {
        return "Commande{" +
                "numBonCommande=" + numBonCommande +
                ",Client="+client+
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
        this.client = contact;
    }


}