package org.uiass.eia.controller;

import com.google.gson.*;
import org.uiass.eia.commande.*;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;


public class CommandeController {

    private CommandeDAO commandeDAO = CommandeDAO.getInstance();
    private ProduitDao produitDao =ProduitDao.getInstance();
    private DetailleCommandeDao detailleCommandeDao =DetailleCommandeDao.getInstance();
    private MarqueDao marqueDao=MarqueDao.getInstance();
    private CategorieProduitDao categorieProduitDao=CategorieProduitDao.getInstance();
    private ContactDao contactDao = ContactDao.getInstance();

    public CommandeController(){}
    private static DetailleCommande parseDetailCommande(JsonObject detailCommandeJson) {

        CommandeController commandeController = new CommandeController();

        Produit produit = detailCommandeJson.has("produitObjet") ?
                commandeController.produitDao.findProduitById(detailCommandeJson.get("produitObjet").getAsJsonObject().get("id").getAsLong()):
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
                    DetailleCommande detailCommende = parseDetailCommande(detailCommandeJson);
                    detailCommandes.add(detailCommende);
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

            Commande commandeCree = new Commande(contact,dateCommande,dateReglement,prix,statutCommande,detailCommandes);
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
            commandeController.commandeDAO.addCommande(new Commande(contact,dateCommande, dateReglement, totalCommande, etatCommande, (List<DetailleCommande>) detailCommandeObject));

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

         /* get("/api/achats/get/detailsachats/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));


            return achatController.achatDao.getDetailsAchats(id);

        },gsonWithSerializer::toJson);



        get("/api/achats/get/byFournisseur/:fournisseur_id", (req,res)-> {

            res.type("application/json");
            int fournisseur_id = Integer.parseInt(req.params("fournisseur_id"));


            return achatController.achatDao.getAchatsByFournisseur(fournisseur_id);

        },gsonWithSerializer::toJson);


        get("/api/achats/get/fournisseur/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return achatController.achatDao.getFournisseurAchat(id);

        },gson::toJson);


        get("/api/achats/get/prix/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return achatController.achatDao.getPrixAchat(id);

        },gson::toJson);


        get("/api/achats/get/betweenDates", (req,res)-> {

            res.type("application/json");

            Date dateBefore = req.queryParams("dateBefore") != null ?
                    Date.valueOf(req.queryParams("dateBefore")) :
                    null;

            Date dateAfter = req.queryParams("dateAfter") != null ?
                    Date.valueOf(req.queryParams("dateAfter")) :
                    null;

            if(dateBefore != null && dateAfter != null){
                System.out.println(dateAfter);
                System.out.println(dateBefore);
                return achatController.achatDao.getAchatsBetweenDates(dateBefore,dateAfter);
            }
            else if(dateBefore != null){
                System.out.println(dateBefore);
                return achatController.achatDao.getAchatsBeforeDate(dateBefore);
            }
            else if(dateAfter != null){
                System.out.println(dateAfter);
                return achatController.achatDao.getAchatsAfterDate(dateAfter);
            }

            res.status(500);
            return "Erreur lors de la saisie des dates";

        },gsonWithSerializer::toJson);



        get("/api/achats/get/byDate/:date", (req,res)-> {

            res.type("application/json");
            Date date = Date.valueOf(req.params("date"));


            return achatController.achatDao.getAchatsByDate(date);

        },gsonWithSerializer::toJson);


        get("/api/achats/get/date/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));


            return achatController.achatDao.getDateAchat(id);

        },gson::toJson);



        get("/api/achats/get/id/:id", (req,res)-> {

            res.type("application/json");

            long id = Long.parseLong(req.params("id"));

            return achatController.achatDao.getAchatByID(id);

        },gsonWithSerializer::toJson);



        delete("/api/achats/delete/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));

            res.type("application/json");

            boolean status = achatController.achatDao.deleteAchatByID(id);

            if(status)
                return "Suppression effectuée avec succès !";

            res.status(500);
            return "Erreur lors de la suppression";
        }, gson::toJson);




        post("/api/achats/add", (req, res) -> {
            JsonObject achatJson = JsonParser.parseString(req.body()).getAsJsonObject();
            Contact contact = null;

            if(achatJson.has("fournisseur")){
                JsonObject fournisseurJson = achatJson.get("fournisseur").getAsJsonObject();
                contact = achatController.contactDao.findContactById(fournisseurJson.get("id").getAsInt());
            }

            List<DetailAchat> detailAchats = new ArrayList<>();
            if(achatJson.has("detailsAchat")){
                JsonArray detailsAchatsJson = achatJson.get("detailsAchat").getAsJsonArray();
                for(int i = 0 ; i < detailsAchatsJson.size() ; i++){
                    JsonObject detailAchatJson = detailsAchatsJson.get(i).getAsJsonObject();
                    DetailAchat detailAchat = parseDetailAchat(detailAchatJson);
                    detailAchats.add(detailAchat);
                }
            }

            Date dateAchat = achatJson.has("dateAchat") ?
                    Date.valueOf(achatJson.get("dateAchat").getAsString()) :
                    Date.valueOf(LocalDate.now());

            double prix = achatJson.has("prix") ?
                    achatJson.get("prix").getAsDouble() :
                    0.0;

            StatutAchat statutAchat = achatJson.has("statutAchat") ?
                    StatutAchat.valueOf(achatJson.get("statutAchat").getAsString()) :
                    null;

            Achat achatCree = new Achat(contact,detailAchats,dateAchat,prix,statutAchat);
            if(!detailAchats.isEmpty()){
                for(DetailAchat detailAchat : detailAchats){
                    detailAchat.setAchatObjet(achatCree);
                }
            }

            achatController.achatDao.addAchat(achatCree);
            return "Achat ajouté avec succès !";

        }, gson::toJson);




        put("/api/achats/update/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));
            res.type("application/json");

            Achat achat = achatController.achatDao.getAchatByID(id);
            if (achat == null) {
                res.status(404);
                return "Produit non trouvé";
            }

            JsonObject achatJson = JsonParser.parseString(req.body()).getAsJsonObject();

            if (achatJson.has("fournisseur")) {
                JsonObject founrisseurJson = achatJson.get("fournisseur").getAsJsonObject();
                Contact contact = achatController.contactDao.findContactById(founrisseurJson.get("id").getAsInt());
                achatController.achatDao.changeFournisseurAchat(id,contact);
            }

            if (achatJson.has("dateAchat")) {
                achatController.achatDao.changeDateAchat(id,Date.valueOf(achatJson.get("dateAchat").getAsString()));
            }

            if (achatJson.has("prix")) {
                achatController.achatDao.changePrixAchat(id, achatJson.get("prix").getAsDouble());
            }

            if (achatJson.has("statutAchat")) {
                achatController.achatDao.changeStatutAchat(id, achatJson.get("statutAchat").getAsString());
            }

            if(achatJson.has("detailsAchat")){
                achatController.achatDao.deleteAllDetailAchats(achat.getId());
                List<DetailAchat> detailAchats = new ArrayList<>();
                JsonArray detailsAchatsJson = achatJson.get("detailsAchat").getAsJsonArray();
                for(int i = 0 ; i < detailsAchatsJson.size() ; i++){
                    JsonObject detailAchatJson = detailsAchatsJson.get(i).getAsJsonObject();
                    DetailAchat detailAchat = parseDetailAchat(detailAchatJson);
                    detailAchat.setAchatObjet(achat);
                    detailAchats.add(detailAchat);
                }
                achatController.achatDao.changeDetailsAchat(id, detailAchats);
            }

            return "Changements effectués avec succès !";
        }, gson::toJson);


    }














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
        },gsonWithSerializer::toJson);

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
                    String dateString = request.params("date");

                    // Pas besoin de parser la date car le format est directement utilisable
                    java.sql.Date date = java.sql.Date.valueOf(dateString);

                    response.type("application/json");
                    return gson.toJson( commandeController.commandeDAO.getCommandesByDate(date));
                });
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


        *//*post("/api/commande/add", (req, res) -> {
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
            commandeController.commandeDAO.addCommande(new Commande(dateCommande, dateReglement, totalCommande, etatCommande, (List<DetailleCommande>) detailCommandeObject));

            return "Commande créée avec succès!";
        }, gson::toJson);


*/
        }
}
