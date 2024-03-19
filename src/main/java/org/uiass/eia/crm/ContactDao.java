package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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

	public Contact findContactById(int id) {
		// TODO Auto-generated method stub
		return em.find(Contact.class, id);
	}

	public List<Entreprise> findEntrepriseByRaisonSociale(String raisonSociale) {
		TypedQuery<Contact> query = em.createQuery("select c from Contact c where c.raisonSociale= :raisonSociale", Contact.class);
		query.setParameter("raisonSociale", raisonSociale);
		List<Entreprise> entreprises = new ArrayList<>();
		try {
			List<Contact> contacts = query.getResultList();
			for(Contact contact : contacts){
				if(contact instanceof Entreprise){
					entreprises.add((Entreprise) contact);
				}
			}
		} catch (NonUniqueResultException e) {
			return null;
		}
		return entreprises;
	}

	public List<Particulier> findParticuliersByNom(String nom) {
		TypedQuery<Contact> query = em.createQuery(
				"SELECT c FROM Contact c WHERE c.nom = :nom", Contact.class);
		query.setParameter("nom", nom);
		List<Particulier> particuliers = new ArrayList<>();
		try {
			List<Contact> results = query.getResultList();
			for (Contact result : results) {
				if (result instanceof Particulier) {
					particuliers.add((Particulier) result);
				}
			}
		} catch (NoResultException e) {
			// Handle the case where no result is found
		}
		return particuliers;
	}


	public void deleteContactById(int id) {
		try {
			tr.begin();
			Contact contact = em.find(Contact.class, id);
			if (contact != null) {
				em.remove(contact);
				tr.commit();
			} else {
			}
		} catch (Exception e) {
			tr.rollback();
		}
	}

	public void changeTelephone(int id,String telephone){
		String hql = "update Contact set telephone= :telephone where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("telephone",telephone);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeFax(int id,String fax){
		String hql = "update Contact set fax= :fax where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("fax",fax);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeNom(int id,String nom){
		String hql = "update Contact set nom= :nom where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("nom",nom);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changePrenom(int id,String prenom){
		String hql = "update Contact set prenom= :prenom where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("prenom",prenom);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeRaisonSociale(int id,String raisonSociale){
		String hql = "update Contact set raisonSociale= :raisonSociale where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("raisonSociale",raisonSociale);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}


	public void changeFormeJuridique(int id,String formeJuridique){
		String hql = "update Contact set formeJuridique= :formeJuridique where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("formeJuridique",formeJuridique);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeEmail(int id,String email){
		String hql = "update Contact set email= :email where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("email",email);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeAdresseId(int id,int adresse_id){
		String hql = "update Contact set adresse_id= :adresse_id where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("adresse_id",adresse_id);
			query.executeUpdate();
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}





}
