package org.uiass.eia.controller;

import static spark.Spark.*;

import com.google.gson.Gson;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;
import org.uiass.eia.crm.Entreprise;
import org.uiass.eia.crm.Particulier;

import javax.servlet.http.Part;
import java.util.*;


import java.util.List;

public class ContactController {
    private ContactDao contactDao = new ContactDao();

    public ContactController(){

    }

    public static void main(String[] args) {
        ContactController contactController = new ContactController();
        Gson gson = new Gson();

        System.out.println("serveur démarré sur l'adresse http://localhost:4567");

        get("/contacts", (req,res)-> {
            List<Contact> listeContacts = contactController.contactDao.getAllContacts();
            res.type("application/json");
            return listeContacts;

        },gson::toJson);

        get("/particuliers", (req,res)-> {
            List<Particulier> listeParticuliers = contactController.contactDao.getAllParticuliers();
            res.type("application/json");
            return listeParticuliers;

        },gson::toJson);

        get("/entreprises", (req,res)-> {
            List<Entreprise> listeEntreprises = contactController.contactDao.getAllEntreprises();
            res.type("application/json");
            return listeEntreprises;

        },gson::toJson);

        get("/particuliers/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Particulier particulier = contactController.contactDao.findParticulierById(id);
            res.type("application/json");
            return particulier;
        }, gson::toJson);

        get("/entreprises/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Entreprise entreprise = contactController.contactDao.findEntrepriseById(id);
            res.type("application/json");
            return entreprise;
        }, gson::toJson);

    }
}
