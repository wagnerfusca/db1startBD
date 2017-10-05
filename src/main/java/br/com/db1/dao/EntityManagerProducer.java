package br.com.db1.dao;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer implements Serializable {

	private static final long serialVersionUID = 1L;

	private EntityManagerFactory factory;

	
	@Produces
	@ApplicationScoped
	public void createFactory() {
		this.factory = Persistence.createEntityManagerFactory("db1start");
	}

	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager manager) {
		if (manager.isOpen()) {
			manager.close();
		}
	}
}
