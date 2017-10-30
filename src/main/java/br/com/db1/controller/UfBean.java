package br.com.db1.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.db1.dao.impl.UfDao;
import br.com.db1.model.Uf;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ApplicationScoped
@Named
public class UfBean {

	private UfDao dao;

	@Inject
	public UfBean(UfDao dao) {
		this.dao = dao;
	}

	private List<Uf> list;

	private String nomeUfFiltrada;

	private Uf uf;

	@PostConstruct
	public void init() {
		zerarLista();
	}

	private void zerarLista() {
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
		this.uf = new Uf();
		return "cadastrarUf";
	}

	public String salvar() {
		if (!dao.save(this.uf)) {
			adicionarMensagem("Erro ao cadastrar a UF.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("UF salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			nomeUfFiltrada = this.uf.getNome();
			listarUf();
		}
		return "uf";
	}

	public String editar(Uf uf) {
		this.uf = dao.findById(uf.getId());
		return "cadastrarUf";
	}

	public String remover(Uf uf) {
		if (!dao.delete(uf.getId())) {
			adicionarMensagem("Erro ao remover a UF.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("UF removida com sucesso.", FacesMessage.SEVERITY_INFO);
			listarUf();
		}
		return "uf";
	}

	public void listarUf() {
		zerarLista();
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

	public void imprimirRelatorio() throws JRException, IOException {
		// compilacao do JRXML
		FacesContext context = FacesContext.getCurrentInstance();
	    String caminho = context.getExternalContext().getRealPath("reports/uf.jrxml");
	    
		if (new File(caminho).exists() == false) {
			return;
		}
		JasperDesign jasperDesign = JRXmlLoader.load(caminho);
		JasperReport report = JasperCompileManager.compileReport(jasperDesign);

		JasperPrint print = JasperFillManager.fillReport(report, null,
				new JRBeanCollectionDataSource(list));

		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletOutputStream servletOutputStream = response.getOutputStream();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=uf.pdf");

		JasperExportManager.exportReportToPdfStream(print, servletOutputStream);

		context.responseComplete();

	}

}
