package org.uiass.eia;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String numero_tva; // Primary key

    private String id_commande;
    private String note_commande;
    private String adresse_livraison;
    private LocalDate date_commande;

    // Default constructor for JPA
    public Commande() {
    }

    // Constructor with parameters
    public Commande(String numero_tva, String id_commande, String note_commande, String adresse_livraison, LocalDate date_commande) {
        this.numero_tva = numero_tva;
        this.id_commande = id_commande;
        this.note_commande = note_commande;
        this.adresse_livraison = adresse_livraison;
        this.date_commande = date_commande;
    }

    // Getters and setters

    public String getNumero_tva() {
        return numero_tva;
    }

    public void setNumero_tva(String numero_tva) {
        this.numero_tva = numero_tva;
    }

    public String getId_commande() {
        return id_commande;
    }

    public void setId_commande(String id_commande) {
        this.id_commande = id_commande;
    }

    public String getNote_commande() {
        return note_commande;
    }

    public void setNote_commande(String note_commande) {
        this.note_commande = note_commande;
    }

    public String getAdresse_livraison() {
        return adresse_livraison;
    }

    public void setAdresse_livraison(String adresse_livraison) {
        this.adresse_livraison = adresse_livraison;
    }

    public LocalDate getDate_commande() {
        return date_commande;
    }

    public void setDate_commande(LocalDate date_commande) {
        this.date_commande = date_commande;
    }
}
