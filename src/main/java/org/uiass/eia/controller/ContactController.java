package org.uiass.eia.controller;

import com.google.gson.Gson;
import org.uiass.eia.crm.ContactDao;
import org.uiass.eia.crm.Entreprise;
import org.uiass.eia.crm.Particulier;

import java.util.*;

import static spark.Spark.*;

public class ContactController {
    private ContactDao contactDao = ContactDao.getInstance();

    public ContactController(){

    }

    public static void main(String[] args) {
        ContactController contactController = new ContactController();
        Gson gson = new Gson();

        System.out.println("Serveur démarré sur l'adresse http://localhost:4567");




        //Contacts
        get("/contacts", (req,res)-> {
            String id = req.queryParams("id");

            res.type("application/json");

            if(id != null){
                return contactController.contactDao.findContactById(Integer.parseInt(id));
            }
            //Aucun queryParam -> Afficher tous les contacts
            return contactController.contactDao.getAllContacts();

        },gson::toJson);




        // Particuliers
        get("/particuliers", (req, res) -> {
            String id = req.queryParams("id");
            String nom = req.queryParams("nom");

            res.type("application/json");

            if(id != null && nom !=null){
                Particulier particulier = contactController.contactDao.findParticulierById(Integer.parseInt(id));
                if(particulier != null && particulier.getNom().equals(nom)){
                    return particulier;
                }
                return null;
            }
            if (id != null) {
                return contactController.contactDao.findParticulierById(Integer.parseInt(id));
            }
            if (nom != null) {
                List<Particulier> particuliers = contactController.contactDao.findParticuliersByNom(nom);
                if(particuliers.isEmpty()){
                    return null;
                }
                return particuliers;
            }
            //Aucun queryParam n'est donné -> Afficher tous les particuliers
            else {
                return contactController.contactDao.getAllParticuliers();
            }

        }, gson::toJson);




        //Entreprises
        //Trouver les entités Entreprise par leur différents attributs
        //Les queryParams peuvent être utilisés ensemble -> localhost:4567?id=8&fj=SARL va retourner
        //les entités ayant l'ID 8 ET la forme juridique SARL
        //sinon retourne null
        get("/entreprises", (req, res) -> {
            String id = req.queryParams("id");
            String raisonSociale = req.queryParams("rs");
            String formeJuridique = req.queryParams("fj");

            res.type("application/json");

            if(id != null && raisonSociale != null && formeJuridique != null){
                Entreprise entreprise = contactController.contactDao.findEntrepriseById(Integer.parseInt(id));
                if(entreprise != null && entreprise.getRaisonSociale().equals(raisonSociale) && entreprise.getFormeJuridique().equals(formeJuridique)){
                    return entreprise;
                }
                return null;
            }

            if(id != null && formeJuridique != null){
                Entreprise entreprise = contactController.contactDao.findEntrepriseById(Integer.parseInt(id));
                if(entreprise != null && entreprise.getFormeJuridique().equals(formeJuridique)){
                    return entreprise;
                }
                return null;
            }

            if(id != null && raisonSociale != null){
                Entreprise entreprise = contactController.contactDao.findEntrepriseById(Integer.parseInt(id));
                if(entreprise != null && entreprise.getRaisonSociale().equals(raisonSociale)){
                    return entreprise;
                }
                return null;
            }

            if(formeJuridique != null && raisonSociale != null){
                List<Entreprise> filteredEntreprises = new ArrayList<>();
                List<Entreprise> entreprisesByFormeJuridique = contactController.contactDao.findEntrepriseByFormeJuridique(formeJuridique);
                for(Entreprise entreprise : entreprisesByFormeJuridique){
                    if(entreprise != null && entreprise.getRaisonSociale().equals(raisonSociale)){
                        filteredEntreprises.add(entreprise);
                    }
                }
                if(filteredEntreprises.isEmpty())
                    return null;
                return filteredEntreprises;
            }

            if(id != null){
                return contactController.contactDao.findContactById(Integer.parseInt(id));
            }

            if(raisonSociale != null){
                List<Entreprise> entreprises = contactController.contactDao.findEntrepriseByRaisonSociale(raisonSociale);
                if(entreprises.isEmpty()){
                    return null;
                }
                return entreprises;
            }

            if(formeJuridique != null){
                List<Entreprise> entreprises = contactController.contactDao.findEntrepriseByFormeJuridique(formeJuridique);
                if(entreprises.isEmpty()){
                    return null;
                }
                return entreprises;
            }

            //Dans le cas où aucun queryParam n'est donné, on aura toutes les entités en retour ->
            //  localhost:4567/entreprises va retourner toutes les entités Entreprise
            if(id == null && raisonSociale == null && formeJuridique == null){
                List<Entreprise> allEntreprises = contactController.contactDao.getAllEntreprises();
                return allEntreprises;
            }
            else{
                return null;
            }

        }, gson::toJson);








    }
}
