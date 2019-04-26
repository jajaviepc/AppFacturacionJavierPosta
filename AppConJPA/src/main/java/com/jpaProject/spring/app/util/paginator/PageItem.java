package com.jpaProject.spring.app.util.paginator;

public class PageItem {

	private int numero;
	boolean actual;

	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}

}
