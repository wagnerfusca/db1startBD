package br.com.db1.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.db1.dao.impl.UfDao;
import br.com.db1.model.Uf;

@RequestScoped
@Named
public class UfBean {

	@Inject
	private UfDao dao;

	private String nome;

	public void salvar() {
		Uf uf = new Uf();
		uf.setNome(nome);
		dao.inserir(uf);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
