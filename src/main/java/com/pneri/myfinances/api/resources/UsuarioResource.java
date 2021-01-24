package com.pneri.myfinances.api.resources;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pneri.myfinances.api.dto.UsuarioDTO;
import com.pneri.myfinances.exceptions.UsuarioBussinessException;
import com.pneri.myfinances.exceptions.UsuarioErrorAuthentication;
import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.services.LancamentoService;
import com.pneri.myfinances.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	UsuarioService usuarioService;
	LancamentoService lancamentoService;

	public UsuarioResource(UsuarioService usuarioService, LancamentoService lancamentoService) {
		this.usuarioService = usuarioService;
		this.lancamentoService = lancamentoService;
	}

	@GetMapping("/list")
	public ResponseEntity<List<Usuario>> listarAllUsuarios() {
		List<Usuario> usuarios = usuarioService.listarAllUsuarios();
		return ResponseEntity.ok(usuarios);
	}

	@PostMapping("/autenticate")
	public ResponseEntity autenticate(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = usuarioService.autenticarUsuario(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (UsuarioErrorAuthentication e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping
	public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
		try {
			Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (UsuarioBussinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("{id}/saldo")
	public ResponseEntity ObterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> findUsuarioById = usuarioService.findUsuarioById(id);
		if (!findUsuarioById.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		BigDecimal saldo = lancamentoService.obterSaldoByUsuario(id);
		return ResponseEntity.ok(saldo);
	}

}
