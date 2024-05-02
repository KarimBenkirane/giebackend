package org.uiass.eia.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.uiass.eia.achat.*;
import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.AdresseDao;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class AchatController {

    private ProduitDao produitDao = ProduitDao.getInstance();
    private AchatDao achatDao = AchatDao.getInstance();
    private ContactDao contactDao = ContactDao.getInstance();
    private AdresseDao adresseDao = AdresseDao.getInstance();


    public AchatController() {

    }

    private static DetailAchat parseDetailAchat(JsonObject detailAchatJson) {

        AchatController achatController = new AchatController();

        long achat_id = detailAchatJson.has("achat_id") ?
                detailAchatJson.get("achat_id").getAsLong():
                -1;
        Achat achat = achatController.achatDao.getAchatByID(achat_id);
        long produit_id = detailAchatJson.has("produit_id") ?
                detailAchatJson.get("produit_id").getAsLong():
                -1;
        Produit produit = achatController.produitDao.getProduitByID(produit_id);

        int qteAchetee = detailAchatJson.has("qteAchetee") ?
                detailAchatJson.get("qteAchetee").getAsInt():
                -1;

        double prixAchat = detailAchatJson.has("prixAchat") ?
                detailAchatJson.get("prixAchat").getAsDouble():
                0.0;

        double reduction =  detailAchatJson.has("reduction") ?
                detailAchatJson.get("reduction").getAsDouble():
                0.0;

        return new DetailAchat(achat,produit,qteAchetee,prixAchat,reduction);





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
            long id = Long.getLong(req.params("id"));

            return achatController.produitDao.getProduitByID(id);

        },gson::toJson);


        get("/api/produits/prix", (req,res)-> {

            res.type("application/json");

            double prixMin = Double.parseDouble(req.queryParams("prixMin"));
            double prixMax = Double.parseDouble(req.queryParams("prixMax"));

            return achatController.produitDao.getProduitsByPriceRange(prixMin, prixMax);


        },gson::toJson);


        delete("/api/produits/delete/:id", (req, res) -> {
            long id = Long.getLong(req.params("id"));

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
            long id = Long.getLong(req.params("id"));
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


//////////////////////////////////////////////////////////////////////////////////////////////////////


        get("/api/achats/all", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchats();

        },gson::toJson);


        get("/api/achats/cancelled", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsAnnules();

        },gson::toJson);


        get("/api/achats/inprogress", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsEnCours();

        },gson::toJson);


        get("/api/achats/done", (req,res)-> {

            res.type("application/json");

            return achatController.achatDao.getAllAchatsEffectues();

        },gson::toJson);



        get("/api/achats/get/detailsachats/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.getLong(req.params("id"));


            return achatController.achatDao.getDetailsAchats(id);

        },gson::toJson);


        get("/api/achats/get/date/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.getLong(req.params("id"));


            return achatController.achatDao.getDateAchat(id);

        },gson::toJson);


        get("/api/achats/get/fournisseur/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.getLong(req.params("id"));

            return achatController.achatDao.getFournisseurAchat(id);

        },gson::toJson);


        get("/api/achats/get/prix/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.getLong(req.params("id"));

            return achatController.achatDao.getPrixAchat(id);

        },gson::toJson);


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

            StatutAchat statutAchat = achatJson.has("statut_achat") ?
                    StatutAchat.valueOf(achatJson.get("statut_achat").getAsString()) :
                    null;


            achatController.achatDao.addAchat(new Achat(contact,detailAchats,dateAchat,prix,statutAchat));

            return "Achat ajouté avec succès !";

        }, gson::toJson);




        get("/api/achats/dates", (req,res)-> {

            res.type("application/json");
            Date dateBefore = Date.valueOf(req.queryParams("dateBefore"));
            Date dateAfter = Date.valueOf(req.queryParams("dateAfter"));

            if(dateBefore != null && dateAfter != null){
                return achatController.achatDao.getAchatsBetweenDates(dateBefore,dateAfter);
            }
            else if(dateBefore != null){
                return achatController.achatDao.getAchatsBeforeDate(dateBefore);
            }
            else if(dateAfter != null){
                return achatController.achatDao.getAchatsAfterDate(dateAfter);
            }

            res.status(500);
            return "Erreur lors de la saisie des dates";

        },gson::toJson);



        get("/api/achats/get/date/:date", (req,res)-> {

            res.type("application/json");
            Date date = Date.valueOf(req.params("date"));


            return achatController.achatDao.getAchatsByDate(date);

        },gson::toJson);



        get("/api/achats/get/:id", (req,res)-> {

            res.type("application/json");

            long id = Long.getLong(req.params("id"));

            return achatController.achatDao.getAchatByID(id);

        },gson::toJson);



        delete("/api/achats/delete/:id", (req, res) -> {
            long id = Long.getLong(req.params("id"));

            res.type("application/json");

            boolean status = achatController.achatDao.deleteAchatByID(id);

            if(status)
                return "Suppression effectuée avec succès !";

            res.status(500);
            return "Erreur lors de la suppression";
        }, gson::toJson);




        put("/api/achats/update/:id", (req, res) -> {
            long id = Long.getLong(req.params("id"));
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

            if (achatJson.has("statut_achat")) {
                achatController.achatDao.changeStatutAchat(id, achatJson.get("statut_achat").getAsString());
            }

            if(achatJson.has("detailsAchat")){
                List<DetailAchat> detailAchats = new ArrayList<>();
                JsonArray detailsAchatsJson = achatJson.get("detailsAchat").getAsJsonArray();
                for(int i = 0 ; i < detailsAchatsJson.size() ; i++){
                    JsonObject detailAchatJson = detailsAchatsJson.get(i).getAsJsonObject();
                    DetailAchat detailAchat = parseDetailAchat(detailAchatJson);
                    detailAchats.add(detailAchat);
                }
                achatController.achatDao.changeDetailsAchat(id, detailAchats);
            }

            return "Changements effectués avec succès !";
        }, gson::toJson);




    }

}
