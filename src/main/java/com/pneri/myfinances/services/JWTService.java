package com.pneri.myfinances.services;

import com.pneri.myfinances.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JWTService {

	/**
	 * @param usuario
	 * @return
	 */
	String gerarToken(Usuario usuario);

	/**
	 * @param Token
	 * @return
	 * @throws ExpiredJwtException
	 */
	Claims obterClaims(String token) throws ExpiredJwtException;

	boolean isTokenValido(String token);

	String obterLoginUsuario(String token);

}
