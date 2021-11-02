package com.pneri.myfinances.services;

import java.util.List;
import java.util.Optional;

import com.pneri.myfinances.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario cadastrar(Usuario usuario);

	List<Usuario> listarAll();

	void validarEmail(String email);

	Optional<Usuario> findById(Long id);

}
