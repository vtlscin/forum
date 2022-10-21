package br.com.alura.forum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import br.com.alura.forum.service.TokenService;

public class FiltroAutenticacao extends OncePerRequestFilter{

	private TokenService tokenService;
	
	private UsuarioRepository usuarioRepository;
	
	public FiltroAutenticacao(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = obtemToken(request);
		//System.out.println(token);
		boolean tokenValido = tokenService.isTokenValido(token);
		//System.out.println(tokenValido);
		if(tokenValido) {
			autenticarToken(token);
		}
		filterChain.doFilter(request, response);
	}

	private void autenticarToken(String token) {
		Long id = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioRepository.findById(id).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String obtemToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			token = "";
		}
		else {
			token = token.substring(7,token.length());
		}
		return token;
	}

}
