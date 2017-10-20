package br.com.db1.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import br.com.db1.dao.impl.ArquivoDao;
import br.com.db1.model.Arquivo;

@ApplicationScoped
@Named
public class ArquivoBean {

	@Inject
	private ArquivoDao dao;

	private List<Arquivo> list;

	private String nomeArquivoFiltrado;

	private Arquivo arquivo;

	private Part arquivoUpado;

	public Part getArquivoUpado() {
		return arquivoUpado;
	}

	public void setArquivoUpado(Part arquivoUpado) {
		this.arquivoUpado = arquivoUpado;
	}

	@PostConstruct
	public void init() {
		zerarLista();
	}

	public void download(Arquivo arquivoParametro) throws IOException {
		try {
			Path path = Paths.get("c:\temp");
			Files.write(path, arquivo.getArquivo());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNomeArquivo() {
		String header = arquivoUpado.getHeader("content-disposition");
		if (header == null)
			return "";
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "";
	}

	public String importa() {
		try {
			this.arquivo.setNomeArquivo(getNomeArquivo());
			this.arquivo.setExtensaoArquivo(arquivoUpado.getContentType());

			byte[] arquivoByte = IOUtils.toByteArray(arquivoUpado.getInputStream());
			this.arquivo.setArquivo(arquivoByte);
			salvar();

		} catch (IOException e) {
			adicionarMensagem("Erro ao enviar o arquivo " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
		return "arquivo";
	}

	private void zerarLista() {
		list = new ArrayList<Arquivo>();
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public String getNomeUfFiltrada() {
		return nomeArquivoFiltrado;
	}

	public void setNomeUfFiltrada(String nomeUfFiltrada) {
		this.nomeArquivoFiltrado = nomeUfFiltrada;
	}

	public String getNomeArquivoFiltrado() {
		return nomeArquivoFiltrado;
	}

	public void setNomeArquivoFiltrado(String nomeArquivoFiltrado) {
		this.nomeArquivoFiltrado = nomeArquivoFiltrado;
	}

	public List<Arquivo> getList() {
		return list;
	}

	public String novo() {
		this.arquivo = new Arquivo();
		return "cadastrarArquivo";
	}

	public void salvar() {
		if (!dao.save(this.arquivo)) {
			adicionarMensagem("Erro ao enviar o arquivo.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("Arquivo salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			nomeArquivoFiltrado = this.arquivo.getNomeArquivo();
			listarArquivo();
		}

	}

	public String editar(Arquivo arquivo) {
		this.arquivo = dao.findById(arquivo.getId());
		return "cadastrarArquivo";
	}

	public String remover(Arquivo arquivo) {
		if (!dao.delete(arquivo.getId())) {
			adicionarMensagem("Erro ao remover o arquivo.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("Arquivo removido com sucesso.", FacesMessage.SEVERITY_INFO);
			listarArquivo();
		}
		return "arquivo";
	}

	public void listarArquivo() {
		zerarLista();
		if (!nomeArquivoFiltrado.isEmpty()) {
			list.addAll(dao.findByName(nomeArquivoFiltrado));
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

}
