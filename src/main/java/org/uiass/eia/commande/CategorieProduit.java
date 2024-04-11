package org.uiass.eia.commande;

import jakarta.persistence.*;

@Entity(name="CategorieProd")
@Table(name="CategorieProd")
public class CategorieProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cat_prod")
    private int idCatProd;

    @Column(name="nom_categorie")
    private String nomCategorie;


    public CategorieProduit(String nomCategorie) {
            this.idCatProd = idCatProd;
            this.nomCategorie = nomCategorie;
        }

    public CategorieProduit() {

    }

    // Getters and Setters
        public int getIdCatProd() {
            return idCatProd;
        }

        public void setIdCatProd(int idCatProd) {
            this.idCatProd = idCatProd;
        }

        public String getNomCategorie() {
            return nomCategorie;
        }

        public void setNomCategorie(String nomCategorie) {
            this.nomCategorie = nomCategorie;
        }

        // toString method
        @Override
        public String toString() {
            return "CategorieProd{" +
                    "idCatProd='" + idCatProd + '\'' +
                    ", nomCategorie='" + nomCategorie + '\'' +
                    '}';
        }
    }

