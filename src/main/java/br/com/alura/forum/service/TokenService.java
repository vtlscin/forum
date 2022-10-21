package br.com.alura.forum.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String geraToken(Authentication authentication) {
		
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date expiracao = new Date(hoje.getTime() + Long.valueOf(expiration));
		
		return Jwts.builder()
				   .setIssuer("Aplicacao forum alura")
				   .setSubject(logado.getId().toString())
				   .setExpiration(expiracao)
				   .signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isTokenValido(String token) {
		boolean tokenValido = false;
		
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			tokenValido = true;
		}catch (Exception e) {
			System.out.println(e.getMessage()); 
		}
		
		return tokenValido;
	}

	public Long getIdUsuario(String token) {
		 Jws<Claims> jws =  Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
		 return Long.valueOf(jws.getBody().getSubject());
	}
	
}
