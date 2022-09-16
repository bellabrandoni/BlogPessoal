package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@entity indica ao sp que o objeto abaixo vai ser uma tabela no banco de dados
@Entity

//@table ser para dar um nome para tabela a ser criada, sem ela a tabela é criada com o nome do objeto
@Table(name="tb_postagens")

public class Postagem {

    // indica que o id da tabela será uma chave primaria
    @Id
    // indica que a chave primaria será auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="O atributo titulo é obrigatório e não pode utilizar espaços em branco")

    //define o minimo e o maximo de letras que podem ser inseridas
    @Size(min= 4, max=100, message="O campo precisa ter no minimo 4 letras e no maximo 100 letras")
    private String titulo;

    //define o campo de texto como campo obrigatório
    @NotNull
    private String texto;

    @UpdateTimestamp
    private LocalDateTime data;
    
    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Tema tema;
    
    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Usuario usuario;
 
    
    public Usuario getUsuario() {
		return usuario;
	}
    
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Long getId() {
        return id;
    }
	
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public LocalDateTime getData() {
        return data;
    }
    
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    
	public Tema getTema() {
		return tema;
	}
	
	public void setTema(Tema tema) {
		this.tema = tema;
	}
    
    
}