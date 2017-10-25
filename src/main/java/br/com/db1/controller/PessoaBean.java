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
import br.com.db1.filter.PessoaFiltro;
import br.com.db1.model.Pessoa;
import br.com.db1.service.Criptografia;
import br.com.db1.type.Booleano;
import br.com.db1.type.Sexo;

@ApplicationScoped
@Named
public class PessoaBean {

	@Inject
	private PessoaDao dao;

	@Inject
	private Criptografia criptografia;

	private List<Pessoa> list;

	private Pessoa pessoa;

	private String senha;
	
	private PessoaFiltro filtro = new PessoaFiltro();

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@PostConstruct
	public void init() {
		zerarLista();
		filtro = new PessoaFiltro();
	}

	public PessoaFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(PessoaFiltro filtro) {
		this.filtro = filtro;
	}

	private void zerarLista() {
		list = new ArrayList<Pessoa>();
	}

	public Sexo[] getSexoCadastro() {
		return Sexo.getSexoCadastro();
	}
	
	public Sexo[] getSexoFiltro() {
		return Sexo.getSexoFiltro();
	}
	
	public Booleano[] getAdministradorFiltro() {
		return Booleano.values();
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
			adicionarMensagem("Erro ao cadastrar a Pessoa.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("Pessoa salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			filtro.setNome(this.pessoa.getNome());
			listarPessoa();
		}
		return "pessoa";
	}

	public String editar(Pessoa pessoa) {
		this.pessoa = dao.findById(pessoa.getId());
		return "cadastrarPessoa";
	}

	public String remover(Pessoa pessoa) {
		if (!dao.delete(pessoa.getId())) {
			adicionarMensagem("Erro ao remover a pessoa.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("Pessoa removida com sucesso.", FacesMessage.SEVERITY_INFO);
			listarPessoa();
		}
		return "pessoa";
	}

	public void listarPessoa() {
		zerarLista();
		list.addAll(dao.pesquisaPersonalizada(filtro));
	}

	public void adicionarMensagem(String mensagem, Severity tipoMensagem) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage(mensagem);
		fm.setSeverity(tipoMensagem);
		fc.addMessage(null, fm);

	}

	

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
