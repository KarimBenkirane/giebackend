package org.uiass.eia.helper;

import org.uiass.eia.crm.Contact;

import javax.persistence.*;
import java.util.List;

public class ContactDao {
	private EntityManager em;
	private EntityTransaction tr;


	public ContactDao() {

		this.em= HibernateUtility.getEntityManger();
		tr=em.getTransaction();
	}


	public void addContact(Contact contact) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(contact);
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void deleteContact(Contact contact) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			Contact entity = em.find(Contact.class,contact);
			if (entity != null) { // vérifier que l'objet existe
				em.remove(entity);
			}
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);
		}

	}

	public List<Contact> getAllClients() {
		// TODO Auto-generated method stub
		Query query= em.createQuery("from Contact");
		return query.getResultList();
	}

	public Contact findContactById(int id) {
		// TODO Auto-generated method stub
		return em.find(Contact.class, id);
	}

	public Contact findContactByName(String nom) {
		TypedQuery<Contact> query = em.createQuery("FROM Contact WHERE nom = :nom", Contact.class);
		query.setParameter("nom", nom);
		try {
			return query.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		}
	}
}
