package br.com.db1.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.faces.context.FacesContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import br.com.db1.AbstractTestCase;
import br.com.db1.dao.impl.UfDao;
import br.com.db1.model.Uf;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ FacesContext.class })
public class UfBeanTest extends AbstractTestCase {

	@Mock
	private UfBean bean;
	@Mock
	private UfDao dao;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		dao = new UfDao(manager);
		bean = new UfBean(dao);
	}

	@Test
	public void initTest() {
		bean.init();
		Assert.assertTrue(bean.getList().size() == 0);

		Uf uf = new Uf();
		uf.setNome("TT");
		bean.getList().add(uf);
		Assert.assertTrue(bean.getList().size() == 1);

		bean.init();
		Assert.assertTrue(bean.getList().size() == 0);

	}

	@Test
	public void novoTest() {
		Assert.assertEquals("cadastrarUf", bean.novo());

	}

	@Test
	@Ignore
	public void salvarTest() {
		FacesContext facesContext = mock(FacesContext.class);
		when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
		    
		Uf uf = new Uf();
		uf.setNome("TT");
		bean.setUf(uf);
		when(bean.salvar());
		Assert.assertEquals("uf", bean.salvar());
		Assert.assertTrue(bean.getList().size() == 1);
		Assert.assertEquals("TT", bean.getNomeUfFiltrada());

	}

}
