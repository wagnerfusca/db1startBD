package br.com.db1.type;

public enum Sexo {

	M("Masculino"),F("Feminino");
	
	String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	Sexo(String descricao) {
		this.descricao = descricao;
	}
}
