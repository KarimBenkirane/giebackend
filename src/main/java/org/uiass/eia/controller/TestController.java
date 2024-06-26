package org.uiass.eia.controller;

import com.google.gson.*;
import org.uiass.eia.achat.*;
import org.uiass.eia.crm.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class TestController {

    private ProduitDao produitDao = ProduitDao.getInstance();
    private AchatDao achatDao = AchatDao.getInstance();
    private ContactDao contactDao = ContactDao.getInstance();
    private AdresseDao adresseDao = AdresseDao.getInstance();


    public TestController() {

    }

    private static DetailAchat parseDetailAchat(JsonObject detailAchatJson) {

        TestController testController = new TestController();

        Produit produit = detailAchatJson.has("produitObjet") ?
                testController.produitDao.getProduitByID(detailAchatJson.get("produitObjet").getAsJsonObject().get("id").getAsLong()):
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
        TestController testController = new TestController();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");



        get("/api/produits/get/all", (req,res)-> {

            res.type("application/json");

            return testController.produitDao.getAllProduits();

        },gson::toJson);


        get("/api/produits/get/available", (req,res)-> {

            res.type("application/json");

            return testController.produitDao.getAllAvailableProduits();

        },gson::toJson);


        get("/api/produits/get/unavailable", (req,res)-> {

            res.type("application/json");

            return testController.produitDao.getAllUnavailableProduits();

        },gson::toJson);


        get("/api/produits/get/marque/:marque", (req,res)-> {

            res.type("application/json");
            String marque = req.params("marque");

            return testController.produitDao.getProduitsByMarque(marque);

        },gson::toJson);


        get("/api/produits/get/id/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return testController.produitDao.getProduitByID(id);

        },gson::toJson);


        get("/api/produits/get/byPrix", (req,res)-> {

            res.type("application/json");

            double prixMin = Double.parseDouble(req.queryParams("prixMin"));
            double prixMax = Double.parseDouble(req.queryParams("prixMax"));

            return testController.produitDao.getProduitsByPriceRange(prixMin, prixMax);


        },gson::toJson);


        delete("/api/produits/delete/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));

            res.type("application/json");

            boolean status = testController.produitDao.deleteProduitByID(id);

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

            testController.produitDao.addProduit(new Produit(marque, modele, qteStock, prix, description, CategorieProduit.valueOf(stCategorieProduit.toUpperCase())));

            return "Produit ajouté avec succès !";

        }, gson::toJson);


        put("/api/produits/update/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));
            res.type("application/json");

            Produit produit = testController.produitDao.getProduitByID(id);
            if (produit == null) {
                res.status(404);
                return "Produit non trouvé";
            }

            JsonObject produitJson = JsonParser.parseString(req.body()).getAsJsonObject();

            if (produitJson.has("marque")) {
                testController.produitDao.changeMarqueProduit(id, produitJson.get("marque").getAsString());
            }

            if (produitJson.has("modele")) {
                testController.produitDao.changeModeleProduit(id, produitJson.get("modele").getAsString());
            }

            if (produitJson.has("qteStock")) {
                testController.produitDao.changeQteStockProduit(id, produitJson.get("qteStock").getAsInt());
            }

            if (produitJson.has("prix")) {
                testController.produitDao.changePrixProduit(id, produitJson.get("prix").getAsDouble());
            }

            if (produitJson.has("description")) {
                testController.produitDao.changeDescriptionProduit(id, produitJson.get("description").getAsString());
            }

            if (produitJson.has("categorie")) {
                testController.produitDao.changeCategorieProduit(id, produitJson.get("categorie").getAsString());
            }

            return "Changements effectués avec succès !";
        }, gson::toJson);


        get("/api/produits/get/categories", (req,res)-> {

            res.type("application/json");

            return testController.produitDao.getAllCategories();


        },gson::toJson);

        get("/api/produits/get/marques", (req,res)-> {

            res.type("application/json");

            return testController.produitDao.getAllMarques();


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


            return testController.produitDao.getProduitsByCriteria(marque,modele,qteStock,prixMin,prixMax,description,disponibilite,categoriesList);

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

            return testController.achatDao.getAllAchats();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/cancelled", (req,res)-> {

            res.type("application/json");

            return testController.achatDao.getAllAchatsAnnules();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/inprogress", (req,res)-> {

            res.type("application/json");

            return testController.achatDao.getAllAchatsEnCours();

        },gsonWithSerializer::toJson);


        get("/api/achats/get/done", (req,res)-> {

            res.type("application/json");

            return testController.achatDao.getAllAchatsEffectues();

        },gsonWithSerializer::toJson);



        get("/api/achats/get/detailsachats/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));


            return testController.achatDao.getDetailsAchats(id);

        },gsonWithSerializer::toJson);



        get("/api/achats/get/byFournisseur/:fournisseur_id", (req,res)-> {

            res.type("application/json");
            int fournisseur_id = Integer.parseInt(req.params("fournisseur_id"));


            return testController.achatDao.getAchatsByFournisseur(fournisseur_id);

        },gsonWithSerializer::toJson);


        get("/api/achats/get/fournisseur/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return testController.achatDao.getFournisseurAchat(id);

        },gson::toJson);


        get("/api/achats/get/prix/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));

            return testController.achatDao.getPrixAchat(id);

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
                return testController.achatDao.getAchatsBetweenDates(dateBefore,dateAfter);
            }
            else if(dateBefore != null){
                System.out.println(dateBefore);
                return testController.achatDao.getAchatsBeforeDate(dateBefore);
            }
            else if(dateAfter != null){
                System.out.println(dateAfter);
                return testController.achatDao.getAchatsAfterDate(dateAfter);
            }

            res.status(500);
            return "Erreur lors de la saisie des dates";

        },gsonWithSerializer::toJson);



        get("/api/achats/get/byDate/:date", (req,res)-> {

            res.type("application/json");
            Date date = Date.valueOf(req.params("date"));


            return testController.achatDao.getAchatsByDate(date);

        },gsonWithSerializer::toJson);


        get("/api/achats/get/date/:id", (req,res)-> {

            res.type("application/json");
            long id = Long.parseLong(req.params("id"));


            return testController.achatDao.getDateAchat(id);

        },gson::toJson);



        get("/api/achats/get/id/:id", (req,res)-> {

            res.type("application/json");

            long id = Long.parseLong(req.params("id"));

            return testController.achatDao.getAchatByID(id);

        },gsonWithSerializer::toJson);



        delete("/api/achats/delete/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));

            res.type("application/json");

            boolean status = testController.achatDao.deleteAchatByID(id);

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
                contact = testController.contactDao.findContactById(fournisseurJson.get("id").getAsInt());
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

            testController.achatDao.addAchat(achatCree);
            return "Achat ajouté avec succès !";

        }, gson::toJson);




        put("/api/achats/update/:id", (req, res) -> {
            long id = Long.parseLong(req.params("id"));
            res.type("application/json");

            Achat achat = testController.achatDao.getAchatByID(id);
            if (achat == null) {
                res.status(404);
                return "Achat non trouvé";
            }

            JsonObject achatJson = JsonParser.parseString(req.body()).getAsJsonObject();

            if (achatJson.has("fournisseur")) {
                JsonObject founrisseurJson = achatJson.get("fournisseur").getAsJsonObject();
                Contact contact = testController.contactDao.findContactById(founrisseurJson.get("id").getAsInt());
                testController.achatDao.changeFournisseurAchat(id,contact);
            }

//            if (achatJson.has("dateAchat")) {
//                testController.achatDao.changeDateAchat(id,Date.valueOf(achatJson.get("dateAchat").getAsString()));
//            }

            if (achatJson.has("prix")) {
                testController.achatDao.changePrixAchat(id, achatJson.get("prix").getAsDouble());
            }

            if (achatJson.has("statutAchat")) {
                testController.achatDao.changeStatutAchat(id, achatJson.get("statutAchat").getAsString());
            }

            if(achatJson.has("detailsAchat")){
                testController.achatDao.deleteAllDetailAchats(achat.getId());
                List<DetailAchat> detailAchats = new ArrayList<>();
                JsonArray detailsAchatsJson = achatJson.get("detailsAchat").getAsJsonArray();
                for(int i = 0 ; i < detailsAchatsJson.size() ; i++){
                    JsonObject detailAchatJson = detailsAchatsJson.get(i).getAsJsonObject();
                    DetailAchat detailAchat = parseDetailAchat(detailAchatJson);
                    detailAchat.setAchatObjet(achat);
                    detailAchats.add(detailAchat);
                }
                testController.achatDao.changeDetailsAchat(id, detailAchats);
            }

            return "Changements effectués avec succès !";
        }, gson::toJson);


        get("/api/achats/get/fournisseurs", (req,res)-> {

            res.type("application/json");

            return testController.achatDao.getAllFournisseurs();

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


            return testController.achatDao.getAchatsByCriteria(fournisseur,statut,dateApres,dateAvant,prixMin,prixMax);




        }, gsonWithSerializer::toJson);



        //Contacts
        //Exemple d'utilisation : http://localhost:4567/api/contacts/all → retourne tous les contacts
        get("/api/contacts/all", (req,res)-> {

            res.type("application/json");

            return testController.contactDao.getAllContacts();

        },gson::toJson);



        get("/api/contacts/get/email/:email", (req,res)-> {
            String email = req.params(":email");
            res.type("application/json");

            return testController.contactDao.getContactsByEmail(email);

        },gson::toJson);




        get("/api/particuliers/get/prenom/:prenom", (req,res)-> {
            String prenom = req.params(":prenom");
            res.type("application/json");

            return testController.contactDao.findParticuliersByPrenom(prenom);

        },gson::toJson);




        get("/api/particuliers/get/email/:email", (req,res)-> {
            String prenom = req.params(":email");
            res.type("application/json");

            return testController.contactDao.getParticuliersByEmail(prenom);

        }, gson::toJson);




        get("/api/entreprises/get/email/:email", (req,res)-> {
            String prenom = req.params(":email");
            res.type("application/json");

            return testController.contactDao.getEntreprisesByEmail(prenom);

        }, gson::toJson);



        //Exemple d'utilisation : http://localhost:4567/api/contacts/get/8 → retourne le contact avec l'id 8
        get("/api/contacts/get/:id", (req,res)-> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");

            return testController.contactDao.findContactById(id);

        },gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/contacts/delete/8 → supprime le contact avec l'id 8
        // !!!! Envoyer une requête DELETE dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        delete("/api/contacts/delete/:id", (req, res) -> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");
            Contact contact = testController.contactDao.findContactById(id);
            if(contact == null){
                throw new RuntimeException("Contact introuvable.");
            }
            //Récupérer l'ID de l'adresse afin de la supprimer une fois que le contact a été lui aussi supprimé
            int adresse_id = testController.contactDao.findAdresseIdByContactId(id);
            boolean status = testController.contactDao.deleteContactById(id);
            testController.adresseDao.deleteAdresse(adresse_id);
            if(status){
                return "Suppression effectuée avec succès!";
            }else{
                throw new RuntimeException("Suppression ne marche pas");
            }

        }, gson::toJson);




        // Particuliers
        //Exemple d'utilisation : http://localhost:4567/api/particuliers/all → retourne tous les particuliers
        get("/api/particuliers/all", (req, res) -> {

            res.type("application/json");

            return testController.contactDao.getAllParticuliers();

        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/particuliers/get/nom/Mohamed → retourne les particuliers ayant le nom Mohamed
        get("/api/particuliers/get/nom/:nom", (req,res)-> {
            String nom = req.params("nom");

            res.type("application/json");

            return testController.contactDao.findParticuliersByNom(nom);

        },gson::toJson);




        post("/api/particuliers/add", (req,res)-> {

            res.type("application/json");

            JsonObject particulier = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos particulier
            String nom = null;
            String prenom = null;
            String telephone = null;
            String email = null;
            String fax = null;

            //Infos adresse
            JsonObject adresse = particulier.get("adresse").getAsJsonObject();


            String rue = null;
            int numeroRue = -1;
            int codePostal = -1;
            String quartier = null;
            String ville = null;
            String pays = null;


            //Json Particulier
            JsonElement jsonEmail = particulier.get("email");
            JsonElement jsonFax = particulier.get("fax");
            JsonElement jsonTelephone = particulier.get("telephone");
            JsonElement jsonNom = particulier.get("nom");
            JsonElement jsonPrenom = particulier.get("prenom");

            //Json Adresse
            JsonElement jsonRue = adresse.get("rue");
            JsonElement jsonNumeroRue = adresse.get("numeroRue");
            JsonElement jsonCodePostal = adresse.get("codePostal");
            JsonElement jsonQuartier = adresse.get("quartier");
            JsonElement jsonVille = adresse.get("ville");
            JsonElement jsonPays = adresse.get("pays");



            if(jsonEmail != null) {
                email = jsonEmail.getAsString();
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
            }
            if(jsonNom != null) {
                nom = jsonNom.getAsString();
            }
            if(jsonPrenom != null) {
                prenom = jsonPrenom.getAsString();
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
            }



            Adresse adresseObjet = new Adresse(rue,numeroRue,quartier,codePostal,ville,pays);
            testController.adresseDao.addAdresse(adresseObjet);
            testController.contactDao.addParticulier(new Particulier(telephone,email,fax,adresseObjet,nom,prenom));

            return "Particulier ajouté avec succès!";

        },gson::toJson);




        put("/api/particuliers/change", (req,res)-> {

            JsonObject particulier = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos Particulier
            int id = particulier.get("id").getAsInt();
            String email = null;
            String fax = null;
            String telephone = null;
            String nom = null;
            String prenom = null;


            //Infos Adresse
            int adresse_id = testController.contactDao.findAdresseIdByContactId(id);
            String rue = null;
            int numeroRue = -1;
            int codePostal = -1;
            String quartier = null;
            String ville = null;
            String pays = null;


            //Json Particulier
            JsonElement jsonEmail = particulier.get("email");
            JsonElement jsonFax = particulier.get("fax");
            JsonElement jsonTelephone = particulier.get("telephone");
            JsonElement jsonNom = particulier.get("nom");
            JsonElement jsonPrenom = particulier.get("prenom");

            JsonObject adresse = particulier.get("adresse").getAsJsonObject();

            //Json Adresse
            JsonElement jsonRue = adresse.get("rue");
            JsonElement jsonNumeroRue = adresse.get("numeroRue");
            JsonElement jsonCodePostal = adresse.get("codePostal");
            JsonElement jsonQuartier = adresse.get("quartier");
            JsonElement jsonVille = adresse.get("ville");
            JsonElement jsonPays = adresse.get("pays");



            if(jsonEmail != null) {
                email = jsonEmail.getAsString();
                testController.contactDao.changeEmail(id, email);
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
                testController.contactDao.changeFax(id, fax);
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
                testController.contactDao.changeTelephone(id, telephone);
            }
            if(jsonNom != null) {
                nom = jsonNom.getAsString();
                testController.contactDao.changeNom(id, nom);
            }
            if(jsonPrenom != null) {
                prenom = jsonPrenom.getAsString();
                testController.contactDao.changePrenom(id, prenom);
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
                testController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    testController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    testController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                testController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                testController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
                testController.adresseDao.changePays(adresse_id, pays);
            }


            return "Changements effectués avec succès!";

        },gson::toJson);




        //Entreprises
        //Exemple d'utilisation : http://localhost:4567/api/entreprises/all → retourne toutes les entreprises
        get("/api/entreprises/all", (req, res) -> {

            res.type("application/json");

            return testController.contactDao.getAllEntreprises();

        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/entreprises/get/raisonsociale/CompanyName → retourne les entreprises ayant pour raison sociale CompanyName
        get("/api/entreprises/get/raisonsociale/:raisonSociale", (req,res)-> {
            String raisonSociale = req.params("raisonSociale");

            res.type("application/json");

            return testController.contactDao.findEntrepriseByRaisonSociale(raisonSociale);

        },gson::toJson);




        get("/api/entreprises/get/formejuridique/:formeJuridique", (req,res)-> {
            String formeJuridique = req.params("formeJuridique");

            res.type("application/json");

            return testController.contactDao.findEntrepriseByFormeJuridique(formeJuridique);

        },gson::toJson);




        put("/api/entreprises/change", (req,res)-> {

            JsonObject entreprise = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos Particulier
            int id = entreprise.get("id").getAsInt();
            String email = null;
            String fax = null;
            String telephone = null;
            String raisonSociale = null;
            String formeJuridique = null;


            //Infos Adresse
            int adresse_id = testController.contactDao.findAdresseIdByContactId(id);
            String rue = null;
            int numeroRue = -1;
            int codePostal = -1;
            String quartier = null;
            String ville = null;
            String pays = null;


            //Json Entreprise
            JsonElement jsonEmail = entreprise.get("email");
            JsonElement jsonFax = entreprise.get("fax");
            JsonElement jsonTelephone = entreprise.get("telephone");
            JsonElement jsonRaisonSociale = entreprise.get("raisonSociale");
            JsonElement jsonFormeJuridique = entreprise.get("formeJuridique");

            JsonObject adresse = entreprise.get("adresse").getAsJsonObject();

            //Json Adresse
            JsonElement jsonRue = adresse.get("rue");
            JsonElement jsonNumeroRue = adresse.get("numeroRue");
            JsonElement jsonCodePostal = adresse.get("codePostal");
            JsonElement jsonQuartier = adresse.get("quartier");
            JsonElement jsonVille = adresse.get("ville");
            JsonElement jsonPays = adresse.get("pays");



            if(jsonEmail != null) {
                email = jsonEmail.getAsString();
                testController.contactDao.changeEmail(id, email);
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
                testController.contactDao.changeFax(id, fax);
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
                testController.contactDao.changeTelephone(id, telephone);
            }
            if(jsonRaisonSociale != null) {
                raisonSociale = jsonRaisonSociale.getAsString();
                testController.contactDao.changeRaisonSociale(id, raisonSociale);
            }
            if(jsonFormeJuridique != null) {
                formeJuridique = jsonFormeJuridique.getAsString();
                testController.contactDao.changeFormeJuridique(id, formeJuridique);
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
                testController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    testController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    testController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                testController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                testController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
                testController.adresseDao.changePays(adresse_id, pays);
            }




            return "Changements effectués avec succès!";

        },gson::toJson);




        post("/api/entreprises/add", (req,res)-> {

            res.type("application/json");

            JsonObject particulier = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos particulier
            String raisonSociale = null;
            String formeJuridique = null;
            String telephone = null;
            String email = null;
            String fax = null;

            //Infos adresse
            JsonObject adresse = particulier.get("adresse").getAsJsonObject();


            String rue = null;
            int numeroRue = -1;
            int codePostal = -1;
            String quartier = null;
            String ville = null;
            String pays = null;


            //Json Particulier
            JsonElement jsonEmail = particulier.get("email");
            JsonElement jsonFax = particulier.get("fax");
            JsonElement jsonTelephone = particulier.get("telephone");
            JsonElement jsonRaisonSociale = particulier.get("raisonSociale");
            JsonElement jsonFormeJuridique = particulier.get("formeJuridique");

            //Json Adresse
            JsonElement jsonRue = adresse.get("rue");
            JsonElement jsonNumeroRue = adresse.get("numeroRue");
            JsonElement jsonCodePostal = adresse.get("codePostal");
            JsonElement jsonQuartier = adresse.get("quartier");
            JsonElement jsonVille = adresse.get("ville");
            JsonElement jsonPays = adresse.get("pays");



            if(jsonEmail != null) {
                email = jsonEmail.getAsString();
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
            }
            if(jsonRaisonSociale != null) {
                raisonSociale = jsonRaisonSociale.getAsString();
            }
            if(jsonFormeJuridique != null) {
                formeJuridique = jsonFormeJuridique.getAsString();
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
            }



            Adresse adresseObjet = new Adresse(rue,numeroRue,quartier,codePostal,ville,pays);
            testController.adresseDao.addAdresse(adresseObjet);
            testController.contactDao.addEntreprise(new Entreprise(telephone,email,fax,adresseObjet,raisonSociale,formeJuridique));

            return "Entreprise ajoutée avec succès!";

        },gson::toJson);
        //Adresses

        //Exemple d'utilisation : http://localhost:4567/api/adresses/delete/8 → supprime l'adresse avec l'id 8
        // !!!! Envoyer une requête DELETE dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        delete("/api/adresses/delete/:id", (req, res) -> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");
            Adresse adresse = testController.adresseDao.findAdresseById(id);
            if(adresse == null){
                throw new RuntimeException("Adresse introuvable.");
            }
            testController.adresseDao.deleteAdresse(id);
            return "Suppression effectuée avec succès!";
        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/adresses/all → retourne toutes les adresses
        get("/api/adresses/all", (req, res) -> {

            res.type("application/json");

            return testController.adresseDao.getAllAdresses();

        }, gson::toJson);




        put("/api/adresses/change", (req,res)-> {

            JsonObject adresse = new JsonParser().parse(req.body()).getAsJsonObject();

            //Infos Adresse
            int adresse_id = adresse.get("id").getAsInt();
            String rue = null;
            int numeroRue = -1;
            int codePostal = -1;
            String quartier = null;
            String ville = null;
            String pays = null;


            //Json Adresse
            JsonElement jsonRue = adresse.get("rue");
            JsonElement jsonNumeroRue = adresse.get("numeroRue");
            JsonElement jsonCodePostal = adresse.get("codePostal");
            JsonElement jsonQuartier = adresse.get("quartier");
            JsonElement jsonVille = adresse.get("ville");
            JsonElement jsonPays = adresse.get("pays");



            if(jsonRue != null) {
                rue = jsonRue.getAsString();
                testController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    testController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    testController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                testController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                testController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
                testController.adresseDao.changePays(adresse_id, pays);
            }




            return "Changements effectués avec succès!";

        },gson::toJson);


        post("/api/email/send", (req,res)-> {
            JsonObject email = new JsonParser().parse(req.body()).getAsJsonObject();

            String recipient = email.get("recipient").getAsString();
            String content = email.get("content").getAsString();
            String subject = email.get("subject").getAsString();

            MailSender.sendMail(subject,content,recipient);

            return "Email envoyé avec succès !";

        },gson::toJson);


    }

}
