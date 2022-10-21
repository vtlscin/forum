package br.com.alura.forum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.config.validacao.AutenticacaoService;
import br.com.alura.forum.repository.UsuarioRepository;
import br.com.alura.forum.service.TokenService;

@Configuration
@EnableWebSecurity
@Profile(value = {"prod", "test"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	private AutenticacaoService autenticacaoService;
	
	private TokenService tokenService;
	
	private UsuarioRepository usuarioRepository;
	
	public SecurityConfiguration(AutenticacaoService autenticacaoService, TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.autenticacaoService = autenticacaoService;
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//configuracao de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//configuracao de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.antMatchers(HttpMethod.GET,"/topicos").permitAll()
			.antMatchers(HttpMethod.GET,"/topicos/*").permitAll()
			.antMatchers(HttpMethod.POST,"/auth").permitAll()
			.antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
			.antMatchers(HttpMethod.DELETE,"/topicos/**").hasRole("MODERADOR")
			.anyRequest().authenticated()
			.and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(new FiltroAutenticacao(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}
	
//	//configuracao de autorizacao
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests()
//			.antMatchers(HttpMethod.GET,"/topicos").permitAll()
//			.antMatchers(HttpMethod.GET,"/topicos/*").permitAll()
//			.anyRequest().authenticated()
//			.and().formLogin();
//	}
	
	//configuracao de arquivos estaticos(js,css)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
	       .antMatchers("/**.html",
	                               "/v3/api-docs/**",
	                               "/webjars/**",
	                               "/configuration/**",
	                               "/swagger-resources/**",
	                               "/swagger-ui/**");
	}
}
