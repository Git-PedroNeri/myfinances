package com.pneri.myfinances.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.exceptions.UsuarioErrorAuthentication;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.repositories.UsuarioRepository;
import com.pneri.myfinances.services.UsuarioService;

@Service // Faz com que o container do spring gerencie uma instancia dessa classe
public class UsuarioServiceImpl implements UsuarioService {

//	@Autowired não necessario se eu estiver usando um construtor
	private UsuarioRepository usuarioRepository;// Dependencia
	private PasswordEncoder encoder;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.encoder = encoder;
	}

	/**
	 *
	 */
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		if (!usuario.isPresent()) {
			throw new UsuarioErrorAuthentication("Usuario não encontrado :" + email);
		}

		boolean matches = encoder.matches(senha, usuario.get().getSenha());// Compara a senha digitada com a
																			// Criptografada

		if (!matches) {
			throw new UsuarioErrorAuthentication("Senha inválida :(");

		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario cadastrar(Usuario usuario) {
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
		Usuario user = usuarioRepository.save(usuario);
		return user;

	}

	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCripto = encoder.encode(senha);
		usuario.setSenha(senhaCripto);
	}

	/**
	 * Busca na base de dados se existe um usuario com este email, caso não exista
	 * lança uma exception
	 */
	@Override
	public void validarEmail(String email) {
		Boolean existsEmail = usuarioRepository.existsByEmail(email);
		if (existsEmail) {
			throw new UsuarioBussinessException("Already exists a User with this email: " + email);
		}
	}

	public List<Usuario> listarAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}
}
