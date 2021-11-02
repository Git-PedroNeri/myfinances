package com.pneri.myfinances.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pneri.myfinances.model.entity.Usuario;
import com.pneri.myfinances.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTServiceImpl implements JWTService {

	@Value("${jwt.expiracao}")
	String expiracao;
	@Value("jwt.chave-assinatura")
	String chaveAssinatura;

	@Override
	public String gerarToken(Usuario usuario) {
		long exp = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpricao = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dataHoraExpricao.atZone(ZoneId.systemDefault()).toInstant();
		java.util.Date data = Date.from(instant);

		String horaExpiracaoToken = dataHoraExpricao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

		String token = Jwts.builder().setExpiration(data).setSubject(usuario.getEmail())
				.claim("nome", usuario.getNome()).claim("horaExpiracao", horaExpiracaoToken)
				.signWith(SignatureAlgorithm.HS512, chaveAssinatura).compact();

		return token;
	}

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {

		return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();

	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			java.util.Date dataEx = claims.getExpiration();
			LocalDateTime dataExpiracao = dataEx.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			boolean tokenTimeIsExpirado = LocalDateTime.now().isAfter(dataExpiracao);

			return !tokenTimeIsExpirado;

		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String obterLoginUsuario(String token) {

		Claims claims = obterClaims(token);
		return claims.getSubject();

	}

}
