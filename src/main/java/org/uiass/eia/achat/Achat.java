package org.uiass.eia.achat;

import org.uiass.eia.crm.Contact;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity(name="Achat")
@Table(name="achat")
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="fournisseur_id")
    private Contact fournisseur;

    @OneToMany(mappedBy = "achatObjet", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<DetailAchat> detailsAchat;

    private Date dateAchat;

    private double prix;

    @Enumerated(EnumType.STRING)
    @Column(name="statut_achat")
    private StatutAchat statutAchat;


    public Achat(){

    }

    public Achat(Contact fournisseur, List<DetailAchat> detailsAchat, Date dateAchat, double prix, StatutAchat statutAchat) {
        this.fournisseur = fournisseur;
        this.detailsAchat = detailsAchat;
        this.dateAchat = dateAchat;
        this.prix = prix;
        this.statutAchat = statutAchat;
    }

    public Achat(Contact fournisseur, List<DetailAchat> detailsAchat, Date dateAchat, StatutAchat statutAchat) {
        this.fournisseur = fournisseur;
        this.detailsAchat = detailsAchat;
        this.dateAchat = dateAchat;
        this.statutAchat = statutAchat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contact getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Contact fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<DetailAchat> getDetailsAchat() {
        return detailsAchat;
    }

    public void setDetailsAchat(List<DetailAchat> detailsAchat) {
        this.detailsAchat = detailsAchat;
    }

    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public StatutAchat getStatutAchat() {
        return statutAchat;
    }

    public void setStatutAchat(StatutAchat statutAchat) {
        this.statutAchat = statutAchat;
    }

    public double getPrixTotalAchat(){
        double montant = 0.0;
        for(DetailAchat detailAchat : this.detailsAchat){
            montant += detailAchat.getPrixAchat();
        }
        return montant;
    }


    @Override
    public String toString() {
        return "Achat{" +
                "id=" + id +
                ", fournisseur=" + fournisseur +
                ", detailsAchat=" + detailsAchat +
                ", dateAchat=" + dateAchat +
                ", prix=" + prix +
                ", statutAchat=" + statutAchat +
                '}';
    }

}
