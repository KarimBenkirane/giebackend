package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ContactDao {
	private EntityManager em;
	private EntityTransaction tr;


	public ContactDao() {

		this.em= HibernateUtility.getEntityManger();
		tr=em.getTransaction();
	}


	public List<Contact> getAllContacts() {
		// TODO Auto-generated method stub
		Query query= em.createQuery("from Contact");
		return query.getResultList();
	}


}
