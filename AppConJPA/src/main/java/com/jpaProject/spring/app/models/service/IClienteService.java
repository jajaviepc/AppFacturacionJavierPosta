package com.jpaProject.spring.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jpaProject.spring.app.models.entity.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();

	public Page<Cliente> findAll(Pageable pageable);
	
	public void save(Cliente cliente);

	public Cliente findById(Long id);

	public void delete(Long id);
}
