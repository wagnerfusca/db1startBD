package br.com.db1.dao.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.model.Pessoa;
import br.com.db1.type.Sexo;

public class PessoaDao implements DAO<Pessoa> {

	@Inject
	private EntityManager manager;

	public List<Pessoa> findAll() {
		return manager.createQuery("Select p from Pessoa p").getResultList();
	}

	public Pessoa findById(Long id) {
		Query query = manager.createQuery("Select p from Pessoa p where p.id = :pId");
		query.setParameter("pId", id);
		return (Pessoa) query.getSingleResult();
	}

	public List<Pessoa> findByName(String nome) {
		Query query = manager.createQuery("Select p from Pessoa p where p.nome like :pNome");
		query.setParameter("pNome", "%" + nome + "%");
		return query.getResultList();
	}

	@Transactional
	public boolean save(Pessoa pessoa) {
		try {
			if (pessoa.getId() != null) {
				manager.merge(pessoa);
			} else {
				manager.persist(pessoa);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	@Transactional
	public boolean delete(Long id) {
		Pessoa pessoa = findById(id);
		pessoa.setAtivo(Boolean.FALSE);
		try {
			manager.merge(pessoa);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	public List<Pessoa> pesquisaPersonalizada(String nome, Date dataInicial, Date dataFinal, Boolean administrador,
			Sexo sexo) {
		StringBuilder sb = new StringBuilder("Select p from Pessoa p where 1 = 1 ");
		if (!nome.isEmpty()) {
			sb.append(" and nome like :pNome ");
		}

		if (dataInicial != null && dataFinal != null) {
			sb.append(" and dataCadastro beetween :pDataInicial and :pDataFinal ");
		}
		
		if (administrador) {
			sb.append(" and administrador = :pAdministrador ");
		}
		
		if (sexo != null) {
			sb.append(" and sexo = :pSexo ");
		}

		Query query = manager.createQuery(sb.toString());

		if (!nome.isEmpty()) {
			query.setParameter("pNome", "%" + nome + "%");
		}

		if (dataInicial != null && dataFinal != null) {
			query.setParameter("pDataInicial", dataInicial);
			query.setParameter("pDataFinal", dataFinal);
		}
		
		if (administrador) {
			query.setParameter("pAdministrador", administrador);
		}
		
		if (sexo != null) {
			query.setParameter("pSexo", sexo);
		}
		
		return query.getResultList();
	}

}
