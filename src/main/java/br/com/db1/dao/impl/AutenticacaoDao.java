package br.com.db1.dao.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.db1.model.Pessoa;
import br.com.db1.service.Criptografia;

public class AutenticacaoDao{

	@Inject
	private EntityManager manager;

	@Inject
	private Criptografia criptografia;
	
	public Pessoa findById(String usuario, String senha) {
		try {
			byte[] senhaCriptografada = criptografia.criptografar(senha, "MD5"); 
			Query query = manager.createQuery("Select p from Pessoa p where p.email = :pEmail and p.senha = :pSenha");
			query.setParameter("pEmail", usuario);
			query.setParameter("pSenha", senhaCriptografada);
			return (Pessoa) query.getSingleResult();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
