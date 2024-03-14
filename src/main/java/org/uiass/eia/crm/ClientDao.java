package org.uiass.eia.crm;

import org.uiass.eia.helper.HibernateUtility;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class ClientDao {
	private EntityManager em;
	private EntityTransaction tr;


	public ClientDao() {

		this.em= HibernateUtility.getEntityManger();
		tr=em.getTransaction();
	}


	public void addClient(Client cli) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			em.persist(cli);
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	public void deleteClient(Client cli) {
		// TODO Auto-generated method stub
		try {
			tr.begin();
			Client entity = em.find(Client.class,cli);
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

	public List<Client> getAllClients() {
		// TODO Auto-generated method stub
		Query query= em.createQuery("from Client");
		return query.getResultList();
	}

	public Client findClientById(int id) {
		// TODO Auto-generated method stub
		return em.find(Client.class, id);
	}

	public Client findClientByName(String nom) {
		TypedQuery<Client> query = em.createQuery("FROM Client WHERE nom = :nom", Client.class);
		query.setParameter("nom", nom);
		try {
			return query.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		}
	}
}
