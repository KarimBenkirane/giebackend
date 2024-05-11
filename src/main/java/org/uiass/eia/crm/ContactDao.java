package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {
	private EntityManager em;
	private EntityTransaction tr;
	private static ContactDao contactDao;

	//Singleton pattern
	public static ContactDao getInstance() {
		if (contactDao == null)
			contactDao = new ContactDao();
		return contactDao;
	}


	private ContactDao() {
		this.em = HibernateUtility.getEntityManger();
		tr = em.getTransaction();
	}


	public List<Contact> getAllContacts() {
		// TODO Auto-generated method stub
		Query query = em.createQuery("from Contact");
		return query.getResultList();
	}

	public List<Entreprise> getAllEntreprises() {
		// TODO Auto-generated method stub
		Query query = em.createQuery(" from Contact where contact_type='Entreprise' ");
		return query.getResultList();
	}

	public List<Particulier> getAllParticuliers() {
		// TODO Auto-generated method stub
		Query query = em.createQuery(" from Contact where contact_type='Particulier' ");
		return query.getResultList();
	}

	public void addEntreprise(String telephone, String email, String fax, Adresse adresse, String raisonSociale, String formeJuridique) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(new Entreprise(telephone, email, fax, adresse, raisonSociale, formeJuridique));
			tr.commit();

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void addEntreprise(Entreprise entreprise) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(entreprise);
			tr.commit();

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}


	public void addParticulier(String telephone, String email, String fax, Adresse adresse, String nom, String prenom) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(new Particulier(telephone, email, fax, adresse, nom, prenom));
			tr.commit();

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void addParticulier(Particulier particulier) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(particulier);
			tr.commit();

		} catch (Exception e) {
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

	public int findAdresseIdByContactId(int contactId) {
		Query query = em.createQuery("SELECT c.adresse.id FROM Contact c WHERE c.id = :contactId");
		query.setParameter("contactId", contactId);
		Integer adresseId = (Integer) query.getSingleResult();
		return adresseId != null ? adresseId : -1; // Return -1 if no address found
	}

	public List<Entreprise> findEntrepriseByRaisonSociale(String raisonSociale) {
		TypedQuery<Contact> query = em.createQuery("select c from Contact c where c.raisonSociale= :raisonSociale", Contact.class);
		query.setParameter("raisonSociale", raisonSociale);
		List<Entreprise> entreprises = new ArrayList<>();
		try {
			List<Contact> contacts = query.getResultList();
			for (Contact contact : contacts) {
				if (contact instanceof Entreprise) {
					entreprises.add((Entreprise) contact);
				}
			}
		} catch (NonUniqueResultException e) {
			return null;
		}
		return entreprises;
	}

	public List<Entreprise> findEntrepriseByFormeJuridique(String formeJuridique) {
		TypedQuery<Contact> query = em.createQuery("select c from Contact c where c.formeJuridique= :formeJuridique", Contact.class);
		query.setParameter("formeJuridique", formeJuridique);
		List<Entreprise> entreprises = new ArrayList<>();
		try {
			List<Contact> contacts = query.getResultList();
			for (Contact contact : contacts) {
				if (contact instanceof Entreprise) {
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
		String hql = "delete from Contact where id = :id";
		try {
			tr.begin();
			Query query = em.createQuery(hql);
			query.setParameter("id", id);
			query.executeUpdate();
			tr.commit();

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeTelephone(int id, String telephone) {
		try {

			Contact contact = this.findContactById(id);

			if (contact == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}
			if(contact.getTelephone() == null || !(contact.getTelephone().equals(telephone))){
				tr.begin();
				contact.setTelephone(telephone);
				em.flush();
				tr.commit();
			}
		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeFax(int id, String fax) {
		try {

			Contact contact = this.findContactById(id);

			if (contact == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}


			if(contact.getFax() == null || !(contact.getFax().equals(fax))){
				tr.begin();
				contact.setFax(fax);
				em.flush();
				tr.commit();
			}
		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeNom(int id, String nom) {
		try {

			Particulier particulier = this.findParticulierById(id);

			if (particulier == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}


			if(particulier.getNom() == null || !(particulier.getNom().equals(nom))){
				tr.begin();
				particulier.setNom(nom);
				em.flush();
				tr.commit();
			}
		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changePrenom(int id, String prenom) {
		try {

			Particulier particulier = this.findParticulierById(id);

			if (particulier == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}

			if(particulier.getPrenom() == null || !(particulier.getPrenom().equals(prenom))){
				tr.begin();
				particulier.setPrenom(prenom);
				em.flush();
				tr.commit();
			}
		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeRaisonSociale(int id, String raisonSociale) {
		try {
			Entreprise entreprise = this.findEntrepriseById(id);

			if (entreprise == null) {
				throw new EntityNotFoundException("Entreprise not found with id: " + id);
			}
			if(entreprise.getRaisonSociale() == null || !(entreprise.getRaisonSociale().equals(raisonSociale))){
				tr.begin();
				entreprise.setRaisonSociale(raisonSociale);
				em.flush();
				tr.commit();
			}

		} catch (Exception e) {
			tr.rollback();
			// Handle other exceptions
			e.printStackTrace(); // or log the exception
		}
	}



	public void changeFormeJuridique(int id, String formeJuridique) {
		try {
			Entreprise entreprise = this.findEntrepriseById(id);

			if (entreprise == null) {
				throw new EntityNotFoundException("Entreprise not found with id: " + id);
			}
			if(entreprise.getFormeJuridique() == null || !(entreprise.getFormeJuridique().equals(formeJuridique))){
				tr.begin();
				entreprise.setFormeJuridique(formeJuridique);
				em.flush();
				tr.commit();
			}
		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeEmail(int id, String email) {
		try {
			Contact contact = this.findContactById(id);

			if (contact == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}

			if(contact.getEmail() == null || !(contact.getEmail().equals(email))){
				tr.begin();
				contact.setEmail(email);
				em.flush();
				tr.commit();
			}

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void changeAdresseId(int id, int adresse_id) {
		try {

			Contact contact = this.findContactById(id);

			if (contact == null) {
				throw new EntityNotFoundException("Contact not found with id: " + id);
			}


			if(contact.getAdresse().getId()!=adresse_id){
				tr.begin();
				Adresse adresse = contact.getAdresse();
				adresse.setId(adresse_id);
				contact.setAdresse(adresse);
				em.flush();
				tr.commit();
			}

		} catch (Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}


	public List<Contact> getContactsByEmail(String email) {
		List<Contact> contacts = new ArrayList<>();
		try {
			Query query = em.createQuery("FROM Contact WHERE email = :email");
			query.setParameter("email", email);
			contacts = query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
		}

		return contacts;
	}




	public List<Particulier> getParticuliersByEmail(String email) {
		List<Particulier> particuliers = new ArrayList<>();
		try {
			Query query = em.createQuery("FROM Contact WHERE contact_type='Particulier' AND email = :email");
			query.setParameter("email", email);
			particuliers = query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
		}

		return particuliers;
	}




	public List<Entreprise> getEntreprisesByEmail(String email) {
		List<Entreprise> entreprises = new ArrayList<>();
		try {
			Query query = em.createQuery("FROM Contact WHERE contact_type='Entreprise' AND email = :email");
			query.setParameter("email", email);
			entreprises = query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
		}

		return entreprises;
	}

	public List<Particulier> findParticuliersByPrenom(String prenom) {
		TypedQuery<Contact> query = em.createQuery("select c from Contact c where c.prenom= :prenom", Contact.class);
		query.setParameter("prenom", prenom);
		List<Particulier> particuliers = new ArrayList<>();
		try {
			List<Contact> contacts = query.getResultList();
			for (Contact contact : contacts) {
				if (contact instanceof Particulier) {
					particuliers.add((Particulier) contact);
				}
			}
		} catch (NonUniqueResultException e) {
			return null;
		}
		return particuliers;
	}

}
