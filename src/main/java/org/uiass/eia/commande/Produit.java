package org.uiass.eia.commande;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="Produit")
@Table(name="Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idProd")
    private int idProd;

    @Column(name="nomProd")
    private String nomProd;

    @Column(name="ref_prod")
    private String refProd;

    @Column(name="libelle")
    private String libelle;

    @Column(name="quantiteStock")
    private int quantiteStock;
    @OneToOne
    @JoinColumn(name = "Marque")
    private Marque marque;
    @JoinColumn(name = "Categorie")
    private CategorieProduit categorieProduit;

    /*@OneToMany(mappedBy = "produit")
    private List<DetailleCommande> detailleCommandes = new ArrayList<>();

*/
    public Produit(String nomProd, String refProd, String libelle, int quantiteStock,Marque marque,CategorieProduit categorieProduit) {
        this.idProd = idProd;
        this.nomProd = nomProd;
        this.refProd = refProd;
        this.libelle = libelle;
        this.quantiteStock = quantiteStock;
        this.marque= marque;
        this.categorieProduit=categorieProduit;
    }

    public Produit() {

    }

    // Getters and Setters
    public int getIdProd() {
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
                "idProd='" + idProd + '\'' +
                ", nomProd='" + nomProd + '\'' +
                ", refProd='" + refProd + '\'' +
                ", libelle='" + libelle + '\'' +
                ", quantiteStock=" + quantiteStock +
                '}';
    }
}
