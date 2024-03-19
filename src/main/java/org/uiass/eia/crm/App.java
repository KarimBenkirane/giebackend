package org.uiass.eia.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class App {
    private EntityManager em;
    private EntityTransaction tr;

    public App() {

    }

    public static void main( String[] args ) {
        ContactDao contactDao = ContactDao.getContactDao();
        List<Entreprise> entreprises = contactDao.findEntrepriseByRaisonSociale("Company 3");
        for(Entreprise ent : entreprises){
            System.out.println(ent.getEmail());
        }
    }
}


