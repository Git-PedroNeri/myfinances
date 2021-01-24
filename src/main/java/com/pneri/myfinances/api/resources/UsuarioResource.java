package com.pneri.myfinances.api.resources;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pneri.myfinances.api.dto.UsuarioDTO;
import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.exceptions.UsuarioErrorAuthentication;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	UsuarioService service;

	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}

	@GetMapping("/list")
	public ResponseEntity<List<Usuario>> listarAllUsuarios() {
		List<Usuario> usuarios = service.listarAllUsuarios();
		return ResponseEntity.ok(usuarios);
	}

	@PostMapping("/autenticate")
	public ResponseEntity autenticate(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticarUsuario(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (UsuarioErrorAuthentication e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping
	public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
		try {
			Usuario usuarioSalvo = service.cadastrarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (UsuarioBussinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
