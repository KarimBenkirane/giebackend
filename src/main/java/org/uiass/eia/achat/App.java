package org.uiass.eia.achat;

import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;
import org.uiass.eia.crm.ContactDao;
import org.uiass.eia.crm.Particulier;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {

    public static void main(String[] args) {

        ProduitDao produitDao = ProduitDao.getInstance();
        AchatDao achatDao = AchatDao.getInstance();
        ContactDao contactDao = ContactDao.getInstance();


    }

}
