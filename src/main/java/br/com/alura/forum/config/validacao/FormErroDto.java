package br.com.alura.forum.config.validacao;

public class FormErroDto {

	private String mensagem;
	private String campo;
	
	
	public FormErroDto(String mensagem, String campo) {
		this.mensagem = mensagem;
		this.campo = campo;
	}


	public String getErro() {
		return mensagem;
	}


	public void setErro(String mensagem) {
		this.mensagem = mensagem;
	}


	public String getCampo() {
		return campo;
	}


	public void setCampo(String campo) {
		this.campo = campo;
	};
}
