package org.uiass.eia.achat;

import javax.persistence.*;

@Entity(name="Produit")
@Table(name="produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="marque")
    private String marque;
    @Column(name="modele")
    private String modele;
    @Column(name="qteStock")
    private int qteStock;
    @Column(name="prix")
    private double prix;
    @Column(name="description",length = 3000)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name="categorie")
    private CategorieProduit categorieProduit;

    public Produit(String marque, String modele, int qteStock, double prix, String description, CategorieProduit categorieProduit) {
        this.marque = marque;
        this.modele = modele;
        this.qteStock = qteStock;
        this.prix = prix;
        this.description = description;
        this.categorieProduit = categorieProduit;
    }

    public Produit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getQteStock() {
        return qteStock;
    }

    public void setQteStock(int qteStock) {
        this.qteStock = qteStock;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategorieProduit getCategorieProduit() {
        return categorieProduit;
    }

    public void setCategorieProduit(CategorieProduit categorieProduit) {
        this.categorieProduit = categorieProduit;
    }
}
