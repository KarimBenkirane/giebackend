package org.uiass.eia.controller;

import com.google.gson.Gson;
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



        //Exemple d'utilisation : http://localhost:4567/api/contacts/change?id=8&telephone=123&fax=212 → change le téléphone
        //et le fax du contact ayant l'id 8 respectivement par 123 et 212.
        // !!!! Envoyer une requête PUT dans Postman (ne pas mettre l'adresse directement dans le navigateur) !!!!
        put("/api/contacts/change", (req,res)-> {
            String stId = req.queryParams("id");
            String fax = req.queryParams("fax");
            String telephone = req.queryParams("telephone");

            int id = Integer.parseInt(stId);
            res.type("application/json");

            //Changer le téléphone et le fax en même temps
            if(telephone != null && fax!=null){
                contactController.contactDao.changeTelephone(id,telephone);
                contactController.contactDao.changeFax(id,fax);
            }
            //Changer le téléphone seulement
            if(telephone != null && fax==null)
                contactController.contactDao.changeTelephone(id,telephone);
            //Changer le fax seulement
            if(fax != null && telephone==null)
                contactController.contactDao.changeFax(id,fax);


            return "Changements effectués avec succès!";

        },gson::toJson);




        // Particuliers
        //Exemple d'utilisation : http://localhost:4567/api/particuliers/all → retourne tous les particuliers
        get("/api/particuliers/all", (req, res) -> {

            res.type("application/json");

            return contactController.contactDao.getAllParticuliers();

        }, gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/particuliers/get/Mohamed → retourne le particulier ayant le nom Mohamed
        get("/api/particuliers/get/:nom", (req,res)-> {
            String nom = req.params("nom");

            res.type("application/json");

            return contactController.contactDao.findParticuliersByNom(nom);

        },gson::toJson);




        //Exemple d'utilisation : http://localhost:4567/api/particuliers/add?nom=Mohamed&prenom=Ahmed&email=mohamed@email.com&fax=123&telephone=123&codePostal=20000&numeroRue=12&rue=ExempleRue&pays=Maroc&quartier=ExempleQuartier&ville=Rabat
        post("/api/particuliers/add", (req,res)-> {
            String nom = req.queryParams("nom");
            String prenom = req.queryParams("prenom");
            String email = req.queryParams("email");
            String fax = req.queryParams("fax");
            String telephone = req.queryParams("telephone");

            String stCodePostal = req.queryParams("codePostal");
            String stNumeroRue = req.queryParams("numeroRue");
            String rue = req.queryParams("rue");
            String pays = req.queryParams("pays");
            String quartier = req.queryParams("quartier");
            String ville = req.queryParams("ville");

            int codePostal = Integer.parseInt(stCodePostal);
            int numeroRue = Integer.parseInt(stNumeroRue);

            Adresse adresse = new Adresse(rue,numeroRue,quartier,codePostal,ville,pays);
            contactController.adresseDao.addAdresse(adresse);
            contactController.contactDao.addParticulier(telephone,email,fax,adresse,nom,prenom);

            res.type("application/json");

            return "Particulier ajouté avec succès!";

        },gson::toJson);




        //Entreprises
        //Exemple d'utilisation : http://localhost:4567/api/entreprises/all → retourne toutes les entreprises
        get("/api/entreprises/all", (req, res) -> {

            res.type("application/json");

            return contactController.contactDao.getAllEntreprises();

        }, gson::toJson);








    }
}
