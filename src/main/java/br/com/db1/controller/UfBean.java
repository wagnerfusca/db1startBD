package br.com.db1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.db1.dao.impl.UfDao;
import br.com.db1.model.Uf;

@RequestScoped
@Named
public class UfBean {

	@Inject
	private UfDao dao;

	private List<Uf> list;

	private String nomeUfFiltrada;

	private Uf uf;

	@PostConstruct
	public void init() {
		list = new ArrayList<Uf>();
	}

	public String getNomeUfFiltrada() {
		return nomeUfFiltrada;
	}

	public void setNomeUfFiltrada(String nomeUfFiltrada) {
		this.nomeUfFiltrada = nomeUfFiltrada;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public List<Uf> getList() {
		return list;
	}

	public String novo() {
		uf = new Uf();
		return "cadastrarUf";
	}

	public void salvar() {
		if (!dao.save(uf)) {
			adicionarMensagem("Erro ao cadastrar a UF.", FacesMessage.SEVERITY_ERROR);
		}

		adicionarMensagem("UF salvo com sucesso.", FacesMessage.SEVERITY_INFO);
	}

	public String editar() {
		uf = dao.findById(1L);
		return "cadastrarUf";
	}

	public void remover(Uf uf) {
		if (!dao.delete(uf.getId())) {
			adicionarMensagem("Erro ao remover a UF.", FacesMessage.SEVERITY_ERROR);
		}

		adicionarMensagem("UF removida com sucesso.", FacesMessage.SEVERITY_INFO);
	}

	public void listarUf() {
		if (!nomeUfFiltrada.isEmpty()) {
			list.addAll(dao.findByName(nomeUfFiltrada));
		} else {
			list.addAll(dao.findAll());
		}
	}

	public void adicionarMensagem(String mensagem, Severity tipoMensagem) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage(mensagem);
		fm.setSeverity(tipoMensagem);
		fc.addMessage(null, fm);

	}

}
