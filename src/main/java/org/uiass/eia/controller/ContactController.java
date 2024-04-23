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



        get("/api/contacts/get/email/:email", (req,res)-> {
            String email = req.params(":email");
            res.type("application/json");

            return contactController.contactDao.getContactsByEmail(email);

        },gson::toJson);




        get("/api/particuliers/get/prenom/:prenom", (req,res)-> {
            String prenom = req.params(":prenom");
            res.type("application/json");

            return contactController.contactDao.findParticuliersByPrenom(prenom);

        },gson::toJson);




        get("/api/particuliers/get/email/:email", (req,res)-> {
            String prenom = req.params(":email");
            res.type("application/json");

            return contactController.contactDao.getParticuliersByEmail(prenom);

        }, gson::toJson);




        get("/api/entreprises/get/email/:email", (req,res)-> {
            String prenom = req.params(":email");
            res.type("application/json");

            return contactController.contactDao.getEntreprisesByEmail(prenom);

        }, gson::toJson);



        //Exemple d'utilisation : http://localhost:4567/api/contacts/get/8 → retourne le contact avec l'id 8
        get("/api/contacts/get/:id", (req,res)-> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");

            return contactController.contactDao.findContactById(id);

        },gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/contacts/delete/8 → supprime le contact avec l'id 8
        // !!!! Envoyer une requête DELETE dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        delete("/api/contacts/delete/:id", (req, res) -> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");
            Contact contact = contactController.contactDao.findContactById(id);
            if(contact == null){
                throw new RuntimeException("Contact introuvable.");
            }
            //Récupérer l'ID de l'adresse afin de la supprimer une fois que le contact a été lui aussi supprimé
            int adresse_id = contactController.contactDao.findAdresseIdByContactId(id);
            contactController.contactDao.deleteContactById(id);
            contactController.adresseDao.deleteAdresse(adresse_id);
            return "Suppression effectuée avec succès!";
        }, gson::toJson);




        // Particuliers
        //Exemple d'utilisation : http://localhost:4567/api/particuliers/all → retourne tous les particuliers
        get("/api/particuliers/all", (req, res) -> {

            res.type("application/json");

            return contactController.contactDao.getAllParticuliers();

        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/particuliers/get/nom/Mohamed → retourne les particuliers ayant le nom Mohamed
        get("/api/particuliers/get/nom/:nom", (req,res)-> {
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
            contactController.adresseDao.addAdresse(adresseObjet);
            contactController.contactDao.addParticulier(new Particulier(telephone,email,fax,adresseObjet,nom,prenom));

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
                contactController.contactDao.changeEmail(id, email);
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
                contactController.contactDao.changeFax(id, fax);
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
                contactController.contactDao.changeTelephone(id, telephone);
            }
            if(jsonNom != null) {
                nom = jsonNom.getAsString();
                contactController.contactDao.changeNom(id, nom);
            }
            if(jsonPrenom != null) {
                prenom = jsonPrenom.getAsString();
                contactController.contactDao.changePrenom(id, prenom);
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
                contactController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    contactController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    contactController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                contactController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                contactController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
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




        //Exemple d'utilisation : http://localhost:4567/api/entreprises/get/raisonsociale/CompanyName → retourne les entreprises ayant pour raison sociale CompanyName
        get("/api/entreprises/get/raisonsociale/:raisonSociale", (req,res)-> {
            String raisonSociale = req.params("raisonSociale");

            res.type("application/json");

            return contactController.contactDao.findEntrepriseByRaisonSociale(raisonSociale);

        },gson::toJson);




        get("/api/entreprises/get/formejuridique/:formeJuridique", (req,res)-> {
            String formeJuridique = req.params("formeJuridique");

            res.type("application/json");

            return contactController.contactDao.findEntrepriseByFormeJuridique(formeJuridique);

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
            int adresse_id = contactController.contactDao.findAdresseIdByContactId(id);
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
                contactController.contactDao.changeEmail(id, email);
            }
            if(jsonFax != null) {
                fax = jsonFax.getAsString();
                contactController.contactDao.changeFax(id, fax);
            }
            if(jsonTelephone != null) {
                telephone = jsonTelephone.getAsString();
                contactController.contactDao.changeTelephone(id, telephone);
            }
            if(jsonRaisonSociale != null) {
                raisonSociale = jsonRaisonSociale.getAsString();
                contactController.contactDao.changeRaisonSociale(id, raisonSociale);
            }
            if(jsonFormeJuridique != null) {
                formeJuridique = jsonFormeJuridique.getAsString();
                contactController.contactDao.changeFormeJuridique(id, formeJuridique);
            }

            if(jsonRue != null) {
                rue = jsonRue.getAsString();
                contactController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    contactController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    contactController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                contactController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                contactController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
                contactController.adresseDao.changePays(adresse_id, pays);
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
            contactController.adresseDao.addAdresse(adresseObjet);
            contactController.contactDao.addEntreprise(new Entreprise(telephone,email,fax,adresseObjet,raisonSociale,formeJuridique));

            return "Entreprise ajoutée avec succès!";

        },gson::toJson);
        //Adresses

        //Exemple d'utilisation : http://localhost:4567/api/adresses/delete/8 → supprime l'adresse avec l'id 8
        // !!!! Envoyer une requête DELETE dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        delete("/api/adresses/delete/:id", (req, res) -> {
            String stId = req.params("id");
            int id = Integer.parseInt(stId);

            res.type("application/json");
            Adresse adresse = contactController.adresseDao.findAdresseById(id);
            if(adresse == null){
                throw new RuntimeException("Adresse introuvable.");
            }
            contactController.adresseDao.deleteAdresse(id);
            return "Suppression effectuée avec succès!";
        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/adresses/all → retourne toutes les adresses
        get("/api/adresses/all", (req, res) -> {

            res.type("application/json");

            return contactController.adresseDao.getAllAdresses();

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
                contactController.adresseDao.changeRue(adresse_id, rue);
            }
            if(jsonNumeroRue != null) {
                numeroRue = jsonNumeroRue.getAsInt();
                if(numeroRue != -1) {
                    contactController.adresseDao.changeNumeroRue(adresse_id, numeroRue);
                }
            }
            if(jsonCodePostal != null) {
                codePostal = jsonCodePostal.getAsInt();
                if(codePostal != -1) {
                    contactController.adresseDao.changeCodePostal(adresse_id, codePostal);
                }
            }
            if(jsonQuartier != null) {
                quartier = jsonQuartier.getAsString();
                contactController.adresseDao.changeQuartier(adresse_id, quartier);
            }
            if(jsonVille != null) {
                ville = jsonVille.getAsString();
                contactController.adresseDao.changeVille(adresse_id, ville);
            }
            if(jsonPays != null) {
                pays = jsonPays.getAsString();
                contactController.adresseDao.changePays(adresse_id, pays);
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
