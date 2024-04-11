package org.uiass.eia.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.uiass.eia.commande.*;
import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Particulier;


import java.time.LocalDate;

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

        post("/api/particuliers/add", (req, res) -> {

            res.type("application/json");

            JsonObject commande = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos commande
            LocalDate dateC = null;
            LocalDate dateR = null;
            double total = -1;
            EtatCmd etatCmd = null;

            JsonObject detailComande = commande.get("")

            //Json Particulier
            JsonElement jsondateR = commande.get("dateR");
            JsonElement jsondateC = commande.get("dateC");
            JsonElement jsontotal = commande.get("total");
            JsonElement jsonetatCmd = commande.get("etatCmd");




            if (!(jsondateR == null)) {
                dateR = jsondateR.getAsDate();
            }
            if (!(jsondateC == null)) {
                dateC = jsondateC.getAsdate();
            }
            if (!(jsonTelephone == null)) {
                telephone = jsonTelephone.getAsString();
            }
            if (!(jsonNom == null)) {
                nom = jsonNom.getAsString();
            }
            if (!(jsonPrenom == null)) {
                prenom = jsonPrenom.getAsString();
            }


            if (!(jsonRue == null)) {
                rue = jsonRue.getAsString();
            }
            if (!(jsonNumeroRue == null)) {
                numeroRue = jsonNumeroRue.getAsInt();
            }
            if (!(jsonCodePostal == null)) {
                codePostal = jsonCodePostal.getAsInt();
            }
            if (!(jsonQuartier == null)) {
                quartier = jsonQuartier.getAsString();
            }
            if (!(jsonVille == null)) {
                ville = jsonVille.getAsString();
            }
            if (!(jsonPays == null)) {
                pays = jsonPays.getAsString();
            }


            Adresse adresseObjet = new Adresse(rue, numeroRue, quartier, codePostal, ville, pays);
            contactController.adresseDao.addAdresse(adresseObjet);
            contactController.contactDao.addParticulier(new Particulier(telephone, email, fax, adresseObjet, nom, prenom));

            return "Particulier ajouté avec succès!";

        }, gson::toJson);






    }

}
