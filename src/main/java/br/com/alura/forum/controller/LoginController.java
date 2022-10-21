package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.form.LoginForm;
import br.com.alura.forum.dto.TokenDto;
import br.com.alura.forum.service.TokenService;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class LoginController {

	private AuthenticationManager authManager;
	private TokenService tokenService;
	
	public LoginController(AuthenticationManager authManager, TokenService tokenService) {
		this.authManager = authManager;
		this.tokenService = tokenService;
	}

	@PostMapping
	public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginForm form){
		
		UsernamePasswordAuthenticationToken dadosLogin = form.toToken();
		
		try{
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.geraToken(authentication);
			return ResponseEntity.ok(new TokenDto(token,"Bearer"));
		}catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
