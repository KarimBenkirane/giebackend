package org.uiass.eia.achat;

import com.google.gson.Gson;
import org.uiass.eia.crm.ContactDao;

public class App {

    public static void main(String[] args) {
        Gson gson = new Gson();
        ProduitDao produitDao = ProduitDao.getInstance();
        AchatDao achatDao = AchatDao.getInstance();
        ContactDao contactDao = ContactDao.getInstance();

        System.out.println(gson.toJson(produitDao.getAllCategories()));
    }

}
