package com.example.usuario_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usuario_web.model.UsuarioWeb;

@Repository
public interface UsuarioWebRepository extends JpaRepository<UsuarioWeb, Long> {

}
