package br.com.db1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.db1.dao.impl.PessoaDao;
import br.com.db1.model.Pessoa;
import br.com.db1.service.Criptografia;
import br.com.db1.type.Sexo;

@ApplicationScoped
@Named
public class PessoaBean {

	@Inject
	private PessoaDao dao;

	@Inject
	private Criptografia criptografia;

	private List<Pessoa> list;

	private String nomePessoaFiltrada;

	private Pessoa pessoa;

	private String senha;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@PostConstruct
	public void init() {
		zerarLista();
	}

	private void zerarLista() {
		list = new ArrayList<Pessoa>();
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

	public List<Pessoa> getList() {
		return list;
	}

	public String novo() {
		this.pessoa = new Pessoa();
		return "cadastrarPessoa";
	}

	public String salvar() {
		if (this.pessoa.getId() == null) {
			this.pessoa.setSenha(criptografia.criptografar(senha, "MD5"));
		}
		if (!dao.save(this.pessoa)) {
			adicionarMensagem("Erro ao cadastrar a UF.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("UF salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			nomePessoaFiltrada = this.pessoa.getNome();
			listarPessoa();
		}
		return "uf";
	}

	public String editar(Pessoa uf) {
		this.pessoa = dao.findById(uf.getId());
		return "cadastrarUf";
	}

	public String remover(Pessoa uf) {
		if (!dao.delete(uf.getId())) {
			adicionarMensagem("Erro ao remover a UF.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("UF removida com sucesso.", FacesMessage.SEVERITY_INFO);
			listarPessoa();
		}
		return "uf";
	}

	public void listarPessoa() {
		zerarLista();
		if (!nomePessoaFiltrada.isEmpty()) {
			list.addAll(dao.findByName(nomePessoaFiltrada));
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

	public String getNomePessoaFiltrada() {
		return nomePessoaFiltrada;
	}

	public void setNomePessoaFiltrada(String nomePessoaFiltrada) {
		this.nomePessoaFiltrada = nomePessoaFiltrada;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
