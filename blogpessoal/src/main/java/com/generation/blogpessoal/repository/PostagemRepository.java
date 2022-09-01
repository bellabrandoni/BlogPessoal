package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;

//com @repository Classe com os métodos pré-implementadados (get,post,put e delete).

@Repository
public interface PostagemRepository extends JpaRepository <Postagem, Long>{
	//JPA - Biblioteca de métodos e instruções que descreve como deve ser o comportamento dos frameworks.
	
	
	//SLELECT * From tb_postagens WHERE titulo LIKE "%%"
	
		public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo")String titulo);

		

}
