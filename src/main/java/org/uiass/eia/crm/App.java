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
        Adresse a1 = new Adresse("Rue Lotissement Kerimate",11,"Amerchich","Marrakech","Maroc");
        Particulier p1 = new Particulier("0612345678", "test@email.com", "05241567920",a1, "Benkirane", "Mohamed Karim");
        ParticulierDao particulierDao = new ParticulierDao();
        particulierDao.addParticulier(p1);
    }
}
