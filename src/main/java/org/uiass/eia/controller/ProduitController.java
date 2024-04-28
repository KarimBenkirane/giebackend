package org.uiass.eia.controller;

import org.uiass.eia.achat.ProduitDao;
import static spark.Spark.*;
import com.google.gson.*;

public class ProduitController {

    private ProduitDao produitDao = ProduitDao.getInstance();


    public ProduitController() {

    }

    public static void main(String[] args) {

        Gson gson = new Gson();
        ProduitController produitController = new ProduitController();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");



        get("/api/produits/all", (req,res)-> {

            res.type("application/json");

            return produitController.produitDao.getAllProduits();

        },gson::toJson);



    }
}
