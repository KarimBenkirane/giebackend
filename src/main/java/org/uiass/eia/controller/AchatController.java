package org.uiass.eia.controller;

import com.google.gson.*;
import org.uiass.eia.achat.*;
import org.uiass.eia.crm.AdresseDao;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class AchatController {

    private AchatDao.ProduitDao produitDao = AchatDao.ProduitDao.getInstance();
    private AchatDao achatDao = AchatDao.getInstance();
    private ContactDao contactDao = ContactDao.getInstance();
    private AdresseDao adresseDao = AdresseDao.getInstance();


    public AchatController() {

    }

    private static DetailAchat parseDetailAchat(JsonObject detailAchatJson) {

        AchatController achatController = new AchatController();

        Produit produit = detailAchatJson.has("produitObjet") ?
                achatController.produitDao.getProduitByID(detailAchatJson.get("produitObjet").getAsJsonObject().get("id").getAsLong()):
                null;

        int qteAchetee = detailAchatJson.has("qteAchetee") ?
                detailAchatJson.get("qteAchetee").getAsInt():
                -1;

        double prixAchat = detailAchatJson.has("prixAchat") ?
                detailAchatJson.get("prixAchat").getAsDouble():
                0.0;

        double reduction =  detailAchatJson.has("reduction") ?
                detailAchatJson.get("reduction").getAsDouble():
                0.0;

        return new DetailAchat(produit,qteAchetee,prixAchat,reduction);





    }

    public static void main(String[] args) {

        Gson gson = new Gson();
        AchatController achatController = new AchatController();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");



        get("/api/produits/get/all", (req,res)-> {

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


        get("/api/produits/get/id/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return achatController.produitDao.getProduitByID(id);

        },gson::toJson);


        get("/api/produits/get/byPrix", (req,res)-> {

            res.type("application/json");

            double prixMin = Double.parseDouble(req.queryParams("prixMin"));
            double prixMax = Double.parseDouble(req.queryParams("prixMax"));

            return achatController.produitDao.getProduitsByPriceRange(prixMin, prixMax);


        },gson::toJson);


        delete("/api/produits/delete/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));

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
            long id = Long.parseLong(req.params("id"));
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


        get("/api/produits/get/categories", (req,res)-> {

            res.type("application/json");

            return achatController.produitDao.getAllCategories();


        },gson::toJson);

        get("/api/produits/get/marques", (req,res)-> {

            res.type("application/json");

            return achatController.produitDao.getAllMarques();


        },gson::toJson);


        post("/api/produits/advSearch", (req, res) -> {
            JsonObject infosJson = JsonParser.parseString(req.body()).getAsJsonObject();

            String marque = infosJson.has("marque") ?
                    infosJson.get("marque").getAsString() :
                    null;

            String modele = infosJson.has("modele") ?
                    infosJson.get("modele").getAsString() :
                    null;

            Integer qteStock = infosJson.has("qtStock") ?
                    infosJson.get("qtStock").getAsInt() :
                    null;

            Double prixMin = infosJson.has("prixMin") ?
                    infosJson.get("prixMin").getAsDouble() :
                    null;
            Double prixMax = infosJson.has("prixMax") ?
                    infosJson.get("prixMax").getAsDouble() :
                    null;

            String description = infosJson.has("description") ?
                    infosJson.get("description").getAsString() :
                    null;

            String disponibilite = "tout";

            if(infosJson.has("disponibilite")) {
                disponibilite = infosJson.get("disponibilite").getAsString(); //tout,indisponible,disponible
            }

            JsonArray categoriesArray = infosJson.has("categorie") ?
                    infosJson.get("categorie").getAsJsonArray() :
                    null;

            List<String> categoriesList = null;

            if(categoriesArray != null && !categoriesArray.isEmpty()){
                categoriesList = new ArrayList<>();
                for(int i = 0; i < categoriesArray.size(); i++){
                    categoriesList.add(categoriesArray.get(i).getAsString());
                }
            }

            System.out.println("côté controller -> "+categoriesList);


            return achatController.produitDao.getProduitsByCriteria(marque,modele,qteStock,prixMin,prixMax,description,disponibilite,categoriesList);

        }, gson::toJson);

//////////////////////////////////////////////////////////////////////////////////////////////////////

        Gson gsonWithSerializer = new GsonBuilder()
                .registerTypeAdapter(DetailAchat.class, (JsonSerializer<DetailAchat>) (detailAchat, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", detailAchat.getId());
                    jsonObject.add("produitObjet", context.serialize(detailAchat.getProduitObjet()));
                    jsonObject.addProperty("qteAchetee", detailAchat.getQteAchetee());
                    jsonObject.addProperty("prixAchat", detailAchat.getPrixAchat());
                    jsonObject.addProperty("reduction", detailAchat.getReduction());
                    return jsonObject;
                })
                .create();



        get("/api/achats/get/all", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchats();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/cancelled", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsAnnules();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/inprogress", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsEnCours();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/done", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsEffectues();

        },gsonWithSerializer::toJson);



        get("/api/achats/get/detailsachats/:id", (req,res)-> {

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


        get("/api/achats/get/fournisseurs", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllFournisseurs();

        },gson::toJson);


        post("/api/achats/advSearch", (req, res) -> {

            JsonObject reqBody = JsonParser.parseString(req.body()).getAsJsonObject();

            String fournisseur = reqBody.has("fournisseur") ?
                    reqBody.get("fournisseur").getAsString() : null;

            String statut = reqBody.has("statut") ?
                    reqBody.get("statut").getAsString() : null;

            String dateApres = reqBody.has("dateApres") ?
                    reqBody.get("dateApres").getAsString() : null;

            String dateAvant = reqBody.has("dateAvant") ?
                    reqBody.get("dateAvant").getAsString() : null;

            Double prixMin = reqBody.has("prixMin") ?
                    reqBody.get("prixMin").getAsDouble() : null;

            Double prixMax = reqBody.has("prixMax") ?
                    reqBody.get("prixMax").getAsDouble() : null;


            return achatController.achatDao.getAchatsByCriteria(fournisseur,statut,dateApres,dateAvant,prixMin,prixMax);




        }, gsonWithSerializer::toJson);


    }

}
