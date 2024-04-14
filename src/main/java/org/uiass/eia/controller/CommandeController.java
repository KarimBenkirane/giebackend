package org.uiass.eia.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.uiass.eia.commande.*;


import java.time.LocalDate;
import java.util.Collection;

import static spark.Spark.*;


public class CommandeController {

    private CommandeDAO commandeDAO = CommandeDAO.getInstance();
    private ProduitDao produitDao =ProduitDao.getInstance();
    private DetailleCommandeDao detailleCommandeDao =DetailleCommandeDao.getInstance();
    private MarqueDao marqueDao=MarqueDao.getInstance();
    private CategorieProduitDao categorieProduitDao=CategorieProduitDao.getInstance();

    public CommandeController(){}

    public static void main(String[] args) {
        CommandeController commandeController =new CommandeController();
        Gson gson= new Gson();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");


        get("/api/commande/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.commandeDAO.getAllCommande();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving commandes: " + e.getMessage();
            }
        }, gson::toJson);

        get("/api/produit/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.produitDao.getAllProduits();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving produits: " + e.getMessage();
            }
        }, gson::toJson);

        get("/api/detailcommande/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.detailleCommandeDao.getAllDetailleCommandes();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving detail commandes: " + e.getMessage();
            }
        }, gson::toJson);

        get("/api/marque/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.marqueDao.getAllMarques();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving marques: " + e.getMessage();
            }
        }, gson::toJson);

        get("/api/categorie/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.categorieProduitDao.getAllCategoriesProduit();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving categories: " + e.getMessage();
            }
        }, gson::toJson);



        //Exemple d'utilisation : http://localhost:4567/api/commande/get/1 → retourne le contact avec l'id 1

        get("api/commande/get/id/:id", (request, response) -> {
            String Id = request.params("id");
            int id = Integer.parseInt(Id);
            response.type("application/json");
            return commandeController.commandeDAO.getCommandeById(id);
        }, gson::toJson);

        get("api/commande/get/total/:total", (request, response) -> {
            double total = Double.parseDouble(request.params("total"));
            response.type("application/json");
            return commandeController.commandeDAO.getCommandesByTotal(total);
        }, gson::toJson);

        get("api/commande/get/date/:date", (request, response) -> {
            LocalDate date = LocalDate.parse(request.params("date"));
            response.type("application/json");
            return commandeController.commandeDAO.getCommandesByDate(date);
        }, gson::toJson);

        // Get product by ID
        get("/api/produit/get/id/:id", (request, response) -> {
            String id = request.params("id");
            int productId = Integer.parseInt(id);
            response.type("application/json");
            return commandeController.produitDao.findProduitById(productId);
        }, gson::toJson);

       // Get product by reference
        get("/api/produit/get/ref/:ref", (request, response) -> {
            String ref = request.params("ref");
            response.type("application/json");
            return commandeController.produitDao.findProduitByRef(ref);
        }, gson::toJson);

        // Get products by label (libelle)
        get("/api/produit/get/libelle/:libelle", (request, response) -> {
            String libelle = request.params("libelle");
            response.type("application/json");
            return commandeController.produitDao.findProduitsByLibelle(libelle);
        }, gson::toJson);

        get("/api/detailcommande/get/:id", (request, response) -> {
            String detailId = request.params("id");
            int id = Integer.parseInt(detailId);
            response.type("application/json");
            return commandeController.detailleCommandeDao.findDetailleCommandeById(id);
        }, gson::toJson);

        get("/api/marque/get/name/:name", (request, response) -> {
            String name = request.params("name");
            response.type("application/json");
            return commandeController.marqueDao.findMarqueByName(name);
        }, gson::toJson);

        get("/api/categorie/get/name/:name", (request, response) -> {
            String name = request.params("name");
            response.type("application/json");
            return commandeController.categorieProduitDao.findCategorieProduitByName(name);
        }, gson::toJson);


        post("/api/commande/add", (req, res) -> {
            res.type("application/json");

            JsonObject commande = new JsonParser().parse(req.body()).getAsJsonObject();

            // Récupérer les champs de la commande
            LocalDate dateCommande = LocalDate.now();
            LocalDate dateReglement = null;
            double totalCommande = -1;
            EtatCmd etatCommande = null;

            JsonObject detailCommande = commande.get("detailCommande").getAsJsonObject();

            // Créer une instance de DetailleCommande
            int quantiteCommande = -1;
            double remise = -1;

            JsonElement jsonDateCommande = commande.get("dateCommande");
            JsonElement jsonDateReglement = commande.get("dateReglement");
            JsonElement jsonTotalCommande = commande.get("totalCommande");
            JsonElement jsonEtatCommande = commande.get("etatCommande");

            // Récupérer les valeurs du JSON
            if (jsonDateReglement != null) {
                dateReglement = LocalDate.parse(jsonDateReglement.getAsString());
            }
            if (jsonDateCommande != null) {
                dateCommande = LocalDate.parse(jsonDateCommande.getAsString());
            }
            if (jsonTotalCommande != null) {
                totalCommande = jsonTotalCommande.getAsDouble();
            }
            if (jsonEtatCommande != null) {
                etatCommande = EtatCmd.valueOf(jsonEtatCommande.getAsString());
            }

            if (detailCommande != null) {
                JsonElement jsonQuantiteCommande = detailCommande.get("quantiteCommande");
                JsonElement jsonRemise = detailCommande.get("remise");

                if (jsonQuantiteCommande != null) {
                    quantiteCommande = jsonQuantiteCommande.getAsInt();
                }
                if (jsonRemise != null) {
                    remise = jsonRemise.getAsDouble();
                }
            }

            // Créer une instance de DetailleCommande et de Commande
            DetailleCommande detailCommandeObject = new DetailleCommande(quantiteCommande, remise);
            commandeController.detailleCommandeDao.addDetailleCommande(detailCommandeObject);
            commandeController.commandeDAO.addCommande(new Commande(dateCommande, dateReglement, totalCommande, etatCommande, (Collection<DetailleCommande>) detailCommandeObject));

            return "Commande créée avec succès!";
        }, gson::toJson);







    }
}
