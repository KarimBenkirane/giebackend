package org.uiass.eia.commande;

import javax.persistence.*;


@Entity
@Table(name="Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProd")
    private long idProd;

    @Column(name = "nomProd")
    private String nomProd;

    @Column(name = "ref_prod")
    private String refProd;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "quantiteStock")
    private int quantiteStock;
    @Column(name="prix")
    private double prix;

    @OneToOne
    @JoinColumn(name = "marque_id")
    private Marque marque;

    @OneToOne
    @JoinColumn(name = "categorie_id")
    private CategorieProduit categorieProduit;


    public Produit() {
    }

    public Produit(String nomProd, String refProd, String libelle, int quantiteStock, Marque marque, CategorieProduit categorieProduit) {
        this.nomProd = nomProd;
        this.refProd = refProd;
        this.libelle = libelle;
        this.quantiteStock = quantiteStock;
        this.marque = marque;
        this.categorieProduit = categorieProduit;
    }

    // Getters and Setters
    public long getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public String getRefProd() {
        return refProd;
    }

    public void setRefProd(String refProd) {
        this.refProd = refProd;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    // toString method
    @Override
    public String toString() {
        return "Produit{" +
                "idProd=" + idProd +
                ", nomProd='" + nomProd + '\'' +
                ", refProd='" + refProd + '\'' +
                ", libelle='" + libelle + '\'' +
                ", quantiteStock=" + quantiteStock +
                '}';
    }

    public double getPrix() {
        return this.prix;
    }
}