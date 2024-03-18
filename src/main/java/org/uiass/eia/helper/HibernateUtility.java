package org.uiass.eia.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtility {

	private static EntityManager entityManager;
	public static EntityManager getEntityManger() {
		if (entityManager == null) {
			try {
				EntityManagerFactory factory = Persistence.createEntityManagerFactory("gie-backend");
				entityManager = factory.createEntityManager();
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		return entityManager;
	}
}

