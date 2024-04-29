package org.uiass.eia.controller;

import org.uiass.eia.achat.ProduitDao;
import static spark.Spark.*;
import com.google.gson.*;

public class AchatController {

    private ProduitDao produitDao = ProduitDao.getInstance();


    public AchatController() {

    }

    public static void main(String[] args) {

        Gson gson = new Gson();
        AchatController achatController = new AchatController();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");



        get("/api/produits/all", (req,res)-> {

            res.type("application/json");

            return achatController.produitDao.getAllProduits();

        },gson::toJson);



    }
}
