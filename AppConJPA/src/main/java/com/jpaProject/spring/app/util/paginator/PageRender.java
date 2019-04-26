package com.jpaProject.spring.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	private String url;
	private Page<T> page;

	private int totalPaginas;
	private int cantElementosPorPagina;
	private int paginaActual;

	private List<PageItem> paginas;
// Ojear bien este codigo de paginacion, esta bueno. Pero es medio enredado
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<>();
		cantElementosPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (totalPaginas <= cantElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= cantElementosPorPagina / 2) {
				desde = 1;
				hasta = cantElementosPorPagina;
			} else if (paginaActual >= totalPaginas - cantElementosPorPagina / 2) {
				desde = totalPaginas - cantElementosPorPagina + 1;
				hasta = cantElementosPorPagina;
			} else {
				desde = paginaActual - cantElementosPorPagina / 2;
				hasta = cantElementosPorPagina;
			}

		}
		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}
	}// fin de constructor

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
