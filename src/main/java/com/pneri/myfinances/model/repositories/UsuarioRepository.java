package com.pneri.myfinances.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pneri.myfinances.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Feature Query Method Spring Data Busca uma propriedade com nome de email
	 * dentro da Entidade Usuario Automaticamente o Spring data cria as queries
	 * necessarias.
	 * 
	 * @param email
	 * @return
	 */
	Optional<Usuario> findByEmail(String email);

	Boolean existsByEmail(String email);

}
