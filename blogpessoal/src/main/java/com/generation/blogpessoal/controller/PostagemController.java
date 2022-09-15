package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController

@RequestMapping("/postagens")
//precisa de um endpoint-um endereço (que é passado com o "/") que vai executar uma função
public class PostagemController {

	// ingeção de dependencia= tranferencia de responsabilidade para o repository,
	// sem precisar instanciar toda vez
	@Autowired
	private PostagemRepository repository;

	// Todas as linha a baixo são iguais a SELECT * FROM tb_postagem

	@GetMapping("/titulo{titulo}") // aplica-se apenas ao método, usada no mapeamento de solicitações HTTP GET, em
									// alguns métodos de tratamento específicos

	// programando uma resposta, para receber uma lista com objeto Postagem, para
	// toda vez que o metodo get for utilizado atraves de ResponseEntity
	// List vai preparar um Array de respostas

	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo)); // retorna o que achar no
																							// banco de dados, aqui dá o
																							// // estato(.ok que
																							// significa
																							// 200)

	}

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	// equivalente a instrução: INSERT INTO tb_postagens (titulo, texto, data)
	// VALUES ("Título", "Texto", CURRENT_TIMESTAMP());.

	// notação @PutMapping indica que o Método put(Postagem postagem), responderá a
	// todas as requisições do tipo HTTP PUT,

	// Put cria um objeto dentro do banco de dados, a partir do findbyid, se existir
	// o id ele deica atualizar

	// SQL = UPDATE tb_postagens SET titulo = "titulo", texto ="texto" WHERE id=1
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		return repository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
	}

	// implementando o Método delete(Long id) na Classe Postagem Controller.
	// Traçando um paralelo com o MySQL, seria o equivalente a instrução: DELETE
	// FROM tb_postagens WHERE id = id;.
	// DELETE from tb_postagens WHERE id=1
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")

	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = repository.findById(id);

		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		repository.deleteById(id);
	}

}
