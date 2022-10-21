package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroValidacaoHandler {

	private MessageSource messageSource;
	
	public ErroValidacaoHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<FormErroDto> handle(MethodArgumentNotValidException exception) {
		List<FormErroDto> errosTratados = new ArrayList<FormErroDto>();
		
		List<FieldError> erros = exception.getBindingResult().getFieldErrors();
		
		erros.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			
			FormErroDto erro = new FormErroDto(mensagem, e.getField());
			
			errosTratados.add(erro);
			
		});
		
		return errosTratados;
		
	}
	
}
