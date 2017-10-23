package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Arquivo;

public class ArquivoDao implements DAO<Arquivo> {

	@Inject
	private EntityManager manager;

	public List<Arquivo> findAll() {
		return manager.createQuery("Select a from Arquivo a").getResultList();
	}

	public Arquivo findById(Long id) {
		Query query = manager.createQuery("Select a from Arquivo a where a.id = :pId");
		query.setParameter("pId", id);
		return (Arquivo) query.getSingleResult();
	}

	public List<Arquivo> findByName(String nome) {
		Query query = manager.createQuery("Select a from Arquivo a where a.nomeArquivo like :pNome");
		query.setParameter("pNome", "%"+nome+"%");
		return query.getResultList();
	}

	@Transactional
	public boolean save(Arquivo arquivo) {
		try {
			manager.persist(arquivo);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	@Transactional
	public boolean delete(Long id) {
		Arquivo arquivo = findById(id);
		try {
			manager.remove(arquivo);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;

	}


}
