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
        Adresse a1 = new Adresse("L'hivernage",7,"Gueliz","Marrakech","Maroc");
        Particulier p1 = new Particulier("0612345678", "test@email.com", "0512345678",a1, "Benkirane", "Karim");
        ParticulierDao particulierDao = new ParticulierDao();
        particulierDao.addParticulier(p1);
    }
}
