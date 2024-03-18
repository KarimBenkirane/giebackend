package org.uiass.eia.crm;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Singleton pattern for SessionFactory

public class GetSessionFactory {

    private static SessionFactory sessionFactory;

    private GetSessionFactory(){}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {

            // Create Configuration
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.setProperty("hibernate.connection.autocommit", "true");
            
            configuration.addAnnotatedClass(Contact.class);
            configuration.addAnnotatedClass(Entreprise.class);
            configuration.addAnnotatedClass(Particulier.class);
            configuration.addAnnotatedClass(Adresse.class);

            // Create Session Factory
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}

