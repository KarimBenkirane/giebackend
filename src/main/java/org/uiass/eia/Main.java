package org.uiass.eia;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.addClass(Client.class);
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Client client = new Client("F9801","Adresse","+212123456","hdqj@gmail.com","www.google.com",40000);
        session.save(client);
        session.flush();
        session.close();
    }
}
