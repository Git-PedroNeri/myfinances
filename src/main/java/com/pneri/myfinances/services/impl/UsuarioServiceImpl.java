package com.pneri.myfinances.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.exceptions.UsuarioErrorAuthentication;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.repositories.UsuarioRepository;
import com.pneri.myfinances.services.UsuarioService;

@Service // Faz com que o container do spring gerencie esta classe
public class UsuarioServiceImpl implements UsuarioService {

//	@Autowired não necessario se eu estiver usando um construtor
	UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario autenticarUsuario(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		if (!usuario.isPresent()) {
			throw new UsuarioErrorAuthentication("Usuario não encontrado :(");
		}
		if (!usuario.get().getSenha().equals(senha)) {
			throw new UsuarioErrorAuthentication("Senha inválida :(");

		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario cadastrarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		Usuario user = usuarioRepository.save(usuario);
		return user;

	}

	/**
	 * Busca na base de dados se existe um usuario com este email
	 */
	@Override
	public void validarEmail(String email) {
		Boolean existsEmail = usuarioRepository.existsByEmail(email);
		if (existsEmail) {
			throw new UsuarioBussinessException("Already exists a User with this email: " + email);
		}
	}

	public List<Usuario> listarAllUsuarios() {
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findUsuarioById(Long id) {
		return usuarioRepository.findById(id);
	}
}
