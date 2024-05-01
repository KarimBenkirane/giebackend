package org.uiass.eia.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.uiass.eia.achat.AchatDao;
import org.uiass.eia.achat.CategorieProduit;
import org.uiass.eia.achat.Produit;
import org.uiass.eia.achat.ProduitDao;

import static spark.Spark.*;

public class AchatController {

    private ProduitDao produitDao = ProduitDao.getInstance();
    private AchatDao achatDao = AchatDao.getInstance();


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


        get("/api/produits/get/available", (req,res)-> {

            res.type("application/json");

            return achatController.produitDao.getAllAvailableProduits();

        },gson::toJson);


        get("/api/produits/get/unavailable", (req,res)-> {

            res.type("application/json");

            return achatController.produitDao.getAllUnavailableProduits();

        },gson::toJson);


        get("/api/produits/get/marque/:marque", (req,res)-> {

            res.type("application/json");
            String marque = req.params("marque");

            return achatController.produitDao.getProduitsByMarque(marque);

        },gson::toJson);


        get("/api/produits/get/:id", (req,res)-> {

            res.type("application/json");
            int id = Integer.parseInt(req.params("id"));

            return achatController.produitDao.getProduitByID(id);

        },gson::toJson);


        get("/api/produits/prix", (req,res)-> {

            res.type("application/json");

            double prixMin = Double.parseDouble(req.queryParams("prixMin"));
            double prixMax = Double.parseDouble(req.queryParams("prixMax"));

           return achatController.produitDao.getProduitsByPriceRange(prixMin, prixMax);


        },gson::toJson);


        delete("/api/produits/delete/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));

            res.type("application/json");

            boolean status = achatController.produitDao.deleteProduitByID(id);

            if(status)
                return "Suppression effectuée avec succès !";

            res.status(500);
            return "Erreur lors de la suppression";
        }, gson::toJson);


        post("/api/produits/add", (req, res) -> {
            JsonObject produitJson = JsonParser.parseString(req.body()).getAsJsonObject();

            String marque = produitJson.has("marque") ?
                    produitJson.get("marque").getAsString() :
                    null;

            String modele = produitJson.has("modele") ?
                    produitJson.get("modele").getAsString() :
                    null;

            int qteStock = produitJson.has("qteStock") ?
                    produitJson.get("qteStock").getAsInt() :
                    0;

            double prix = produitJson.has("prix") ?
                    produitJson.get("prix").getAsDouble() :
                    0.0;

            String description = produitJson.has("description") ?
                    produitJson.get("description").getAsString() :
                    null;

            String stCategorieProduit = produitJson.has("categorie") ?
                    produitJson.get("categorie").getAsString() :
                    null;

            achatController.produitDao.addProduit(new Produit(marque, modele, qteStock, prix, description, CategorieProduit.valueOf(stCategorieProduit.toUpperCase())));

            return "Produit ajouté avec succès !";

        }, gson::toJson);


        put("/api/produits/update/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            res.type("application/json");

            Produit produit = achatController.produitDao.getProduitByID(id);
            if (produit == null) {
                res.status(404);
                return "Produit non trouvé";
            }

            JsonObject produitJson = JsonParser.parseString(req.body()).getAsJsonObject();

            if (produitJson.has("marque")) {
                achatController.produitDao.changeMarqueProduit(id, produitJson.get("marque").getAsString());
            }

            if (produitJson.has("modele")) {
                achatController.produitDao.changeModeleProduit(id, produitJson.get("modele").getAsString());
            }

            if (produitJson.has("qteStock")) {
                achatController.produitDao.changeQteStockProduit(id, produitJson.get("qteStock").getAsInt());
            }

            if (produitJson.has("prix")) {
                achatController.produitDao.changePrixProduit(id, produitJson.get("prix").getAsDouble());
            }

            if (produitJson.has("description")) {
                achatController.produitDao.changeDescriptionProduit(id, produitJson.get("description").getAsString());
            }

            if (produitJson.has("categorie")) {
                achatController.produitDao.changeCategorieProduit(id, produitJson.get("categorie").getAsString());
            }

            return "Changements effectués avec succès !";
        }, gson::toJson);



    }
}
