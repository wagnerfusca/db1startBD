package br.com.db1.type;

public enum Sexo {

	T("Todos"), M("Masculino"),F("Feminino");
	
	String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	Sexo(String descricao) {
		this.descricao = descricao;
	}
	
	public static Sexo[] getSexoCadastro() {
		return new Sexo[] {F, M};
	}
	
	public static Sexo[] getSexoFiltro() {
		return new Sexo[] {T, F, M};
	}
}
