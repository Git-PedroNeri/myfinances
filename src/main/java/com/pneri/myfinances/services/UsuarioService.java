package com.pneri.myfinances.services;

import java.util.List;
import java.util.Optional;

import com.pneri.myfinances.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticarUsuario(String email, String senha);

	Usuario cadastrarUsuario(Usuario usuario);

	List<Usuario> listarAllUsuarios();

	void validarEmail(String email);

	Optional<Usuario> findUsuarioById(Long id);

}
