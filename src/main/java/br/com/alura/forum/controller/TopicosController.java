package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.dto.DetalheTopicosDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	private TopicoRepository topicoRepository;

	private CursoRepository cursoRepository;
	
	public TopicosController(TopicoRepository topicoRepository, CursoRepository cursoRepository) {
		this.topicoRepository = topicoRepository;
		this.cursoRepository = cursoRepository;
	}

	@GetMapping
	public Page<TopicoDto> buscar(@RequestParam(required = false) String nomeCurso, 
			@PageableDefault(sort="id",direction = Direction.DESC) Pageable paginacao){
		
		if(nomeCurso == null) {			
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.toTopicoDto(topicos);
		}
		else {
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			return TopicoDto.toTopicoDto(topicos);
		}
	}
	
	@GetMapping("/todos")
	@Cacheable("buscaTodos")
	public List<TopicoDto> buscarTodos(String nomeCurso){
		
		if(nomeCurso == null) {			
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.toTopicoDto(topicos);
		}
		else {
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDto.toTopicoDto(topicos);
		}
	}
	
	@PostMapping
	@CacheEvict(value = "buscaTodos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder builder) {
		Topico topico = form.toTopico(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); //retorna o endereco do recurso criado.
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalheTopicosDto> detalha(@PathVariable("id") Long id) {
		
		Optional<Topico> resposta = topicoRepository.findById(id);
		if(resposta.isPresent()) {
			return ResponseEntity.ok(new DetalheTopicosDto(resposta.get())); 
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "buscaTodos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable("id") Long id, @RequestBody @Valid AtualizarTopicoForm form){
		Optional<Topico> resposta = topicoRepository.findById(id);
		if(resposta.isPresent()) {
			Topico topico = form.atualizar(resposta.get(), topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "buscaTodos", allEntries = true)
	public ResponseEntity<?> excluir(@PathVariable("id") Long id){
		Optional<Topico> resposta = topicoRepository.findById(id);
		if(resposta.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
