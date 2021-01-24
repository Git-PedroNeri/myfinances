package com.pneri.myfinances.services;

import java.util.List;

import com.pneri.myfinances.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticarUsuario(String email, String senha);

	Usuario cadastrarUsuario(Usuario usuario);

	List<Usuario> listarAllUsuarios();

	void validarEmail(String email);

}
