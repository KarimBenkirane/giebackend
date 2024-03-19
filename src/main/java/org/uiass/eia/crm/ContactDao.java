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
	private static ContactDao contactDao;


	//Singleton pattern
	public static ContactDao getContactDao(){
		if(contactDao == null)
			contactDao = new ContactDao();
		return contactDao;
	}


	private ContactDao() {
		this.em= HibernateUtility.getEntityManger();
		tr=em.getTransaction();
	}


	public List<Contact> getAllContacts() {
		// TODO Auto-generated method stub
		Query query= em.createQuery("from Contact");
		return query.getResultList();
	}

	public List<Entreprise> getAllEntreprises() {
		// TODO Auto-generated method stub
		Query query= em.createQuery(" from Contact where contact_type='Entreprise' ");
		return query.getResultList();
	}

	public List<Particulier> getAllParticuliers() {
		// TODO Auto-generated method stub
		Query query= em.createQuery(" from Contact where contact_type='Particulier' ");
		return query.getResultList();
	}

	public void addEntreprise(String telephone, String email, String fax, Adresse adresse, String raisonSociale, String formeJuridique) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(new Entreprise(telephone,email,fax,adresse,raisonSociale,formeJuridique));
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void addParticulier(String telephone, String email, String fax, Adresse adresse, String nom, String prenom) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(new Particulier(telephone,email,fax,adresse,nom,prenom));
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}


	public Entreprise findEntrepriseById(int id) {
		// TODO Auto-generated method stub
		return em.find(Entreprise.class, id);
	}

	public Particulier findParticulierById(int id) {
		// TODO Auto-generated method stub
		return em.find(Particulier.class, id);
	}

	public Contact findEntrepriseByRaisonSociale(String raisonSociale) {
		TypedQuery<Contact> query = em.createQuery("from Contact where contact_type='Entreprise' and raisonSociale = :raisonSociale", Contact.class);
		query.setParameter("raisonSociale", raisonSociale);
		try {
			return query.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		}
	}

	public Contact findParticulierByNom(String nom) {
		TypedQuery<Contact> query = em.createQuery("from Contact where contact_type='Particulier' and nom = :nom", Contact.class);
		query.setParameter("nom", nom);
		try {
			return query.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		}
	}



}
