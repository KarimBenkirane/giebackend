package org.uiass.eia.controller;

import com.google.gson.*;
import org.uiass.eia.crm.*;

import java.util.*;

import static spark.Spark.*;

public class ContactController {
    private ContactDao contactDao = ContactDao.getInstance();
    private AdresseDao adresseDao = AdresseDao.getInstance();

    public ContactController(){

    }

    public static void main(String[] args) {
        ContactController contactController = new ContactController();
        Gson gson = new Gson();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");




        //Contacts
        //Exemple d'utilisation : http://localhost:4567/api/contacts/all → retourne tous les contacts
        get("/api/contacts/all", (req,res)-> {

            res.type("application/json");

            return contactController.contactDao.getAllContacts();

        },gson::toJson);



        //Exemple d'utilisation : http://localhost:4567/api/contacts/get/8 → retourne le contact avec l'id 8
        get("/api/contacts/get/:id", (req,res)-> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");

            return contactController.contactDao.findContactById(id);

        },gson::toJson);



        //Exemple d'utilisation : http://localhost:4567/api/contacts/delete/8 → supprime le contact avec l'id 8
        // !!!! Envoyer une requête DELETE dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        delete("/api/contacts/delete/:id",(req,res) -> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");
            contactController.contactDao.deleteContactById(id);


            return "Contact supprimé avec succès!";


        },gson::toJson);




        // Particuliers
        //Exemple d'utilisation : http://localhost:4567/api/particuliers/all → retourne tous les particuliers
        get("/api/particuliers/all", (req, res) -> {

            res.type("application/json");

            return contactController.contactDao.getAllParticuliers();

        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/particuliers/get/Mohamed → retourne les particuliers ayant le nom Mohamed
        get("/api/particuliers/get/:nom", (req,res)-> {
            String nom = req.params("nom");

            res.type("application/json");

            return contactController.contactDao.findParticuliersByNom(nom);

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



            if(!(jsonEmail instanceof JsonNull)) {
                email = jsonEmail.getAsString();
            }
            if(!(jsonFax instanceof JsonNull)) {
                fax = jsonFax.getAsString();
            }
            if(!(jsonTelephone instanceof JsonNull)) {
                telephone = jsonTelephone.getAsString();
            }
            if(!(jsonNom instanceof JsonNull)) {
                nom = jsonNom.getAsString();
            }
            if(!(jsonPrenom instanceof JsonNull)) {
                prenom = jsonPrenom.getAsString();
            }


            if(!(jsonRue instanceof JsonNull)) {
                rue = jsonRue.getAsString();
            }
            if(!(jsonNumeroRue instanceof JsonNull)) {
                numeroRue = jsonNumeroRue.getAsInt();
            }
            if(!(jsonCodePostal instanceof JsonNull)) {
                codePostal = jsonCodePostal.getAsInt();
            }
            if(!(jsonQuartier instanceof JsonNull)) {
                quartier = jsonQuartier.getAsString();
            }
            if(!(jsonVille instanceof JsonNull)) {
                ville = jsonVille.getAsString();
            }
            if(!(jsonPays instanceof JsonNull)) {
                pays = jsonPays.getAsString();
            }


            Adresse adresseObjet = new Adresse(rue,numeroRue,quartier,codePostal,ville,pays);
            contactController.adresseDao.addAdresse(adresseObjet);
            contactController.contactDao.addParticulier(telephone,email,fax,adresseObjet,nom,prenom);

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
            int adresse_id = contactController.contactDao.findAdresseIdByContactId(id);
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
            JsonElement jsonRue = particulier.get("rue");
            JsonElement jsonNumeroRue = particulier.get("numeroRue");
            JsonElement jsonCodePostal = particulier.get("codePostal");
            JsonElement jsonQuartier = particulier.get("quartier");
            JsonElement jsonVille = particulier.get("ville");
            JsonElement jsonPays = particulier.get("pays");



            if(!(jsonEmail instanceof JsonNull)) {
                email = jsonEmail.getAsString();
                contactController.contactDao.changeEmail(id,email);
            }
            if(!(jsonFax instanceof JsonNull)) {
                fax = jsonFax.getAsString();
                contactController.contactDao.changeFax(id, fax);
            }
            if(!(jsonTelephone instanceof JsonNull)) {
                telephone = jsonTelephone.getAsString();
                contactController.contactDao.changeTelephone(id, telephone);
            }
            if(!(jsonNom instanceof JsonNull)) {
                nom = jsonNom.getAsString();
                contactController.contactDao.changeNom(id, nom);
            }
            if(!(jsonPrenom instanceof JsonNull)) {
                prenom = jsonPrenom.getAsString();
                contactController.contactDao.changePrenom(id, prenom);
            }


            if(!(jsonRue instanceof JsonNull)) {
                rue = jsonRue.getAsString();
                contactController.adresseDao.changeRue(adresse_id, rue);
            }
            if(!(jsonNumeroRue instanceof JsonNull)) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1)
                    contactController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
            }
            if(!(jsonCodePostal instanceof JsonNull)) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1)
                    contactController.adresseDao.changeCodePostal(adresse_id, codePostal);
            }
            if(!(jsonQuartier instanceof JsonNull)) {
                quartier = jsonQuartier.getAsString();
                contactController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(!(jsonVille instanceof JsonNull)) {
                ville = jsonVille.getAsString();
                contactController.adresseDao.changeVille(adresse_id, ville);
            }
            if(!(jsonPays instanceof JsonNull)) {
                pays = jsonPays.getAsString();
                contactController.adresseDao.changePays(adresse_id, pays);
            }



            return "Changements effectués avec succès!";

        },gson::toJson);




        //Entreprises
        //Exemple d'utilisation : http://localhost:4567/api/entreprises/all → retourne toutes les entreprises
        get("/api/entreprises/all", (req, res) -> {

            res.type("application/json");

            return contactController.contactDao.getAllEntreprises();

        }, gson::toJson);








    }
}
