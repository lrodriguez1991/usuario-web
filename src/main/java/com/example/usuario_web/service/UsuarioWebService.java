package com.example.usuario_web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.usuario_web.model.UsuarioWeb;
import com.example.usuario_web.repository.UsuarioWebRepository;

@Service
public class UsuarioWebService {

	private final UsuarioWebRepository repository;

	public UsuarioWebService(UsuarioWebRepository repository) {
		this.repository = repository;
	}

	public List<UsuarioWeb> listar() {
		return repository.findAll();
	}

	public Optional<UsuarioWeb> obtener(Long id) {
		return repository.findById(id);
	}

	public UsuarioWeb registrar(UsuarioWeb p) {
		return repository.save(p);
	}

	public Object editar(Long id, UsuarioWeb p) {
		return repository.findById(id).map(u -> {
			u.setName(p.getName());
			u.setEmail(p.getEmail());
			return repository.save(u);
		}).orElseThrow(() -> new RuntimeException("Not found"));
	}

	public void eliminar(Long id) {
		repository.deleteById(id);
	}

}
