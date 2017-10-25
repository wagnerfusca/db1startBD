package br.com.db1.filter;

import java.util.Date;

import br.com.db1.type.Sexo;

public class PessoaFiltro {

	private String nome;

	private Date dataInicial = new Date();

	private Date dataFinal = new Date();

	private Boolean administrador = null;

	private Sexo sexo = Sexo.T;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

}
