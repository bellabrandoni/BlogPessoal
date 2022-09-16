package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

//Indica que a classe UsuarioRepositoryTest é uma classe de test, e que esse teste será rodado numa porta aleatoria local no meu computador(desde que ela já não esteja sendo usada)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

//indica que o teste a ser feito será um teste unitário(por classe)
	
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class UsuarioControllerTest {
	
	//serve para ter acesso aos verbos http(post.get.put.delete)em modo
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	//serve para conseguirmos usar as funções de serviço do usuario
	@Autowired
	private UsuarioService usuarioService;
	
	//serve para acessar o banco de dados 'h2'
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//antes de começar o teste, limpa o banco de dados h2 e cadastra o usuario
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Root", "root@root.com", "rootroot", ""));
	}
	
	@Test
	@DisplayName("Cadastrar um Usuário")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario (0L,
				"Paulo Antunes", "paulo@email.com.br","13465278","https://i.imgur.com/FVTvs20.jpg"));
		
			ResponseEntity<Usuario> corpoResposta = testRestTemplate
					.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
	
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(),corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do Uuário")

	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario (0L,
				"Maria da Silva", "maria_silva@email.com.br", "1346578", "https://i.imgur.com/FQTvs20.jpg"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Maria da Silva", "maria_silva@email.com.br", "1346578", "https://i.imgur.com/FQTvs20.jpg"));
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}

	@Test 
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario (0L,
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/FQTvs20.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123", "https://i.imgur.com/FQTvs20.jpg");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		
	}
	
	@Test
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/FQTvs20.jpg" ));
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/FQTvs20.jpg" ));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	
}
