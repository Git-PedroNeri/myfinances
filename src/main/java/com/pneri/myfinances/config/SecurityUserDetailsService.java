package com.pneri.myfinances.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.model.repositories.UsuarioRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private UsuarioRepository usuarioRepository;

	public SecurityUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

		Usuario usuarioEncontrado = usuarioRepository.findByEmail(login)
				.orElseThrow(() -> new UsernameNotFoundException("Emaiol não cadastraod."));

		return userFactory(usuarioEncontrado);

	}

	private UserDetails userFactory(Usuario usuarioEncontrado) {
		return User.builder().username(usuarioEncontrado.getEmail()).password(usuarioEncontrado.getSenha())
				.roles("USER").build();
	}

}
