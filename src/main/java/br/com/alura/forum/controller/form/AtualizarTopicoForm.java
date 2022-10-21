package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotBlank;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizarTopicoForm {

	@NotBlank
	private String titulo;
	@NotBlank
	private String mensagem;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Topico atualizar(Topico topico, TopicoRepository topicoRepository) {
		topico.setTitulo(titulo);
		topico.setMensagem(mensagem);
		return topico;
	}
	
}
