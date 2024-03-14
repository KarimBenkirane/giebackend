package org.uiass.eia.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class App {
    private EntityManager em;
    private EntityTransaction tr;

    public App() {

    }
    public static void main( String[] args )
    {
        List<Client> listClients = new ArrayList<>();
        ClientDao dao = new ClientDao();
        Client cli = new Client("Khalil Araoui","Rabat","+212 123456789","khalilaraoui@gmail.com",20000);
        dao.addClient(cli);
        listClients = dao.getAllClients();

        for(Client c : listClients){
            System.out.println(c);
        }
    }
}
