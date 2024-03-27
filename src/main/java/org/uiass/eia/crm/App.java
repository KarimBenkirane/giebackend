package org.uiass.eia.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class App {
    private EntityManager em;
    private EntityTransaction tr;

    public App() {

    }

    public static void main( String[] args ) {
        ContactDao contactDao = ContactDao.getInstance();
        System.out.println(contactDao.findAdresseIdByContactId(12));
    }
}


