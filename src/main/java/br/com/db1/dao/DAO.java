package br.com.db1.dao;

import java.util.List;

public interface DAO<T> {
	List<T> findAll();

	List<T> findById(Long id);

	List<T> findByName();

	boolean save(T t);

	boolean delete(T t);

}
