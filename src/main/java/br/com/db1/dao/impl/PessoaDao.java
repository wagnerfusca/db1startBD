package br.com.db1.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.db1.dao.DAO;
import br.com.db1.dao.Transactional;
import br.com.db1.filter.PessoaFiltro;
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

	public List<Pessoa> pesquisaPersonalizada(PessoaFiltro filtro) {
		StringBuilder sb = new StringBuilder("Select p from Pessoa p where 1 = 1 ");
		montaFiltroPesquisa(filtro, sb);

		Query query = manager.createQuery(sb.toString());

		montaValoresPesquisa(filtro, query);
		
		return query.getResultList();
	}

	private void montaValoresPesquisa(PessoaFiltro filtro, Query query) {
		if (!filtro.getNome().isEmpty()) {
			query.setParameter("pNome", filtro.getNome() + "%");
		}

		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("pDataInicial", filtro.getDataInicial());
			query.setParameter("pDataFinal", filtro.getDataFinal());
		}
		
		if (filtro.getAdministrador() != null) {
			query.setParameter("pAdministrador", filtro.getAdministrador());
		}
		
		if (!Sexo.T.equals(filtro.getSexo())) {
			query.setParameter("pSexo", filtro.getSexo());
		}
	}

	private void montaFiltroPesquisa(PessoaFiltro filtro, StringBuilder sb) {
		if (!filtro.getNome().isEmpty()) {
			sb.append(" and nome like :pNome ");
		}

		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			sb.append(" and dataCadastro between :pDataInicial and :pDataFinal ");
		}
		
		if (filtro.getAdministrador() != null) {
			sb.append(" and administrador = :pAdministrador ");
		}
		
		if (!Sexo.T.equals(filtro.getSexo())) {
			sb.append(" and sexo = :pSexo ");
		}
	}

}
