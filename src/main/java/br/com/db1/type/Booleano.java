package br.com.db1.type;

public enum Booleano {
	S("Sim"),N("Não");
	
	String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	Booleano(String descricao) {
		this.descricao = descricao;
	}
	
}
