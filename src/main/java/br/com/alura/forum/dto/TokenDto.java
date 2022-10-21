package br.com.alura.forum.dto;

public class TokenDto {

	private String token;
	private String tipo;
	
	public String getToken() {
		return token;
	}
	public String getTipo() {
		return tipo;
	}
	public TokenDto(String token, String tipo) {
		super();
		this.token = token;
		this.tipo = tipo;
	}

	
	
}
