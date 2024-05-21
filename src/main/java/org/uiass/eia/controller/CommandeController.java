package org.uiass.eia.controller;

import com.google.gson.*;
import org.uiass.eia.achat.AchatDao;
import org.uiass.eia.achat.Produit;
import org.uiass.eia.commande.*;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;


import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;


public class CommandeController {

    CommandeDAO commandeDAO = CommandeDAO.getInstance();
    AchatDao.ProduitDao produitDao = AchatDao.ProduitDao.getInstance();
    private DetailleCommandeDao detailleCommandeDao =DetailleCommandeDao.getInstance();
    ContactDao contactDao = ContactDao.getInstance();

    public CommandeController(){}
    private static DetailleCommande parseDetailCommande(JsonObject detailCommandeJson) {

        CommandeController commandeController = new CommandeController();

        Produit produit = detailCommandeJson.has("produitObjet") ?
                commandeController.produitDao.getProduitByID(detailCommandeJson.get("produitObjet").getAsJsonObject().get("id").getAsLong()):
                null;

        int qteAchetee = detailCommandeJson.has("qteAchetee") ?
                detailCommandeJson.get("qteAchetee").getAsInt():
                -1;

        double prixAchat = detailCommandeJson.has("prixAchat") ?
                detailCommandeJson.get("prixAchat").getAsDouble():
                0.0;

        double reduction =  detailCommandeJson.has("reduction") ?
                detailCommandeJson.get("reduction").getAsDouble():
                0.0;

        return new DetailleCommande(produit,qteAchetee,prixAchat,reduction);


    }

    public static void main(String[] args) {
        CommandeController commandeController =new CommandeController();
        Gson gson= new Gson();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");


        Gson gsonWithSerializer = new GsonBuilder()
                .registerTypeAdapter(DetailleCommande.class, (JsonSerializer<DetailleCommande>) (detailCommande, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", detailCommande.getDetailCommande_id());
                    jsonObject.add("produitObjet", context.serialize(detailCommande.getProduit()));
                    jsonObject.addProperty("qteCommande", detailCommande.getQuantiteCommander());
                    jsonObject.addProperty("prixCommande", detailCommande.getPrixCommannde());
                    jsonObject.addProperty("reduction", detailCommande.getRemise());
                    return jsonObject;
                })
                .create();

        get("/api/produit/get/all", (request, response) -> {
            response.type("application/json");
            try {
                return commandeController.produitDao.getAllProduits();
            } catch (Exception e) {
                response.status(500);
                return "Error retrieving produits: " + e.getMessage();
            }
        }, gson::toJson);


        get("/api/commande/get/all", (request, response) -> {
                    response.type("application/json");
                    try {
                        return  commandeController.commandeDAO.getAllCommande();
                    } catch (Exception e) {
                        response.status(500);
                        return "Error retrieving produits: commande not found " + e.getMessage();
                    }
        },gsonWithSerializer::toJson);


        get("/api/commande/get/cancelled", (req,res)-> {

            res.type("application/json");

            return commandeController.commandeDAO.getAllCommandeAnnules();

        },gsonWithSerializer::toJson);


        get("/api/commande/get/inprogress", (req,res)-> {

            res.type("application/json");

            return commandeController.commandeDAO.getAllCommandeEnCours();

        },gsonWithSerializer::toJson);


        get("/api/commande/get/done", (req,res)-> {

            res.type("application/json");

            return commandeController.commandeDAO.getAllCommandeEffectues();

        },gsonWithSerializer::toJson);

        get("/api/commande/get/detailscommande/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));
             List<DetailleCommande> dcs = commandeController.commandeDAO.getDetailsCommande(id);
             for(DetailleCommande d :dcs) {System.out.println(d);}
             return dcs;

        },gsonWithSerializer::toJson);

        get("/api/commande/get/byClient/:client_id", (req,res)-> {

            res.type("application/json");
            int contact_id = Integer.parseInt(req.params("client_id"));


            return commandeController.commandeDAO.getCommandesByClient(contact_id);

        },gsonWithSerializer::toJson);

        get("/api/commande/get/Client/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return commandeController.commandeDAO.getClientCommande(id);

        },gson::toJson);

        get("/api/commande/get/prix/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return commandeController.commandeDAO.getPrixCommande(id);

        },gson::toJson);
        get("/api/commande/get/id/:id", (req,res)-> {

            res.type("application/json");

            long id = Long.parseLong(req.params("id"));

            return commandeController.commandeDAO.getCommandeByID(id);

        },gsonWithSerializer::toJson);

        post("/api/commandes/add", (req, res) -> {
            JsonObject commandeJson = JsonParser.parseString(req.body()).getAsJsonObject();
            Contact contact = null;

            if(commandeJson.has("client")){
                JsonObject ClientJson = commandeJson.get("client").getAsJsonObject();
                contact = commandeController.contactDao.findContactById(ClientJson.get("id").getAsInt());
            }

            List<DetailleCommande> detailCommandes = new ArrayList<>();
            if(commandeJson.has("detailsCommandes")){
                JsonArray detailsCommandesJson = commandeJson.get("detailsCommandes").getAsJsonArray();
                for(int i = 0 ; i < detailsCommandesJson.size() ; i++){
                    JsonObject detailCommandeJson = detailsCommandesJson.get(i).getAsJsonObject();
                    DetailleCommande detailCommande = parseDetailCommande(detailCommandeJson);
                    detailCommandes.add(detailCommande);
                }
            }


            java.sql.Date dateCommande = commandeJson.has("dateCommande") ?
                    java.sql.Date.valueOf(commandeJson.get("dateCommande").getAsString()) :
                    new java.sql.Date(System.currentTimeMillis());
            java.sql.Date dateReglement = commandeJson.has("dateReglement") ?
                    java.sql.Date.valueOf(commandeJson.get("dateReglement").getAsString()) :
                    new java.sql.Date(System.currentTimeMillis());

            double prix = commandeJson.has("totalCommande") ?
                    commandeJson.get("totalCommande").getAsDouble() :
                    0.0;

            EtatCmd statutCommande = commandeJson.has("etatCommande") ?
                    EtatCmd.valueOf(commandeJson.get("etatCommande").getAsString()) :
                    null;

            Commande commandeCree = new Commande(contact, detailCommandes, dateCommande, dateReglement, prix, statutCommande);
            if(!detailCommandes.isEmpty()){
                for(DetailleCommande detailCommande: detailCommandes){
                    detailCommande.setCommandeObjet(commandeCree);
                }
            }

            commandeController.commandeDAO.addCommande(commandeCree);
            return "commande ajouté avec succès !";

        }, gson::toJson);


        post("/api/commande/add", (req, res) -> {
            res.type("application/json");

            JsonObject commande = new JsonParser().parse(req.body()).getAsJsonObject();

            JsonObject commandeJson = JsonParser.parseString(req.body()).getAsJsonObject();
            Contact contact = null;

            if(commandeJson.has("client")){
                JsonObject ClientJson = commandeJson.get("client").getAsJsonObject();
                contact = commandeController.contactDao.findContactById(ClientJson.get("id").getAsInt());
            }

            // Récupérer les champs de la commande
            java.sql.Date dateCommande = new java.sql.Date(System.currentTimeMillis()); // Date actuelle
            java.sql.Date dateReglement = null;
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
                dateReglement = java.sql.Date.valueOf(jsonDateReglement.getAsString());
            }
            if (jsonDateCommande != null) {
                dateCommande = java.sql.Date.valueOf(jsonDateCommande.getAsString());
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
           // commandeController.commandeDAO.addCommande(new Commande(contact,dateCommande, dateReglement, totalCommande, etatCommande, (List<DetailleCommande>) detailCommandeObject));

            return "Commande créée avec succès!";
        }, gson::toJson);

        put("/api/commande/update/:id/:etat", (req, res) -> {
            long id = Long.parseLong(req.params("id"));
            String str = String.copyValueOf(req.params("etat").toCharArray());
            res.type("application/json");

            Commande commande = commandeController.commandeDAO.getCommandeByID(id);
            if (commande == null) {
                res.status(404);
                return "Commande non trouvé";
            }

            JsonObject commandeJson = JsonParser.parseString(req.body()).getAsJsonObject();

            if (commandeJson.has("etatCommande")) {
                commandeController.commandeDAO.changeStatutAchat(id,str);
            }
            return "Changements effectués avec succès !";
        }, gson::toJson);

        }
}
