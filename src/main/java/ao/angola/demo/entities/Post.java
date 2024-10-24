package ao.angola.demo.entities;

import java.util.ArrayList;
import java.util.List;

import ao.angola.demo.enums.StatusAprovacao;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "posts")
public class Post {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_post")
	private Long id;
	
	@Column(name = "titulo", unique = true, updatable = true)
	private String titulo;
	
	@Column(name = "descricao")
	private String descricao;

	@Column( name = "status_aprovacao")
	@Enumerated(EnumType.STRING)
	private StatusAprovacao statusAprovacao;

	@Column(name = "visible")
	private boolean visible;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserModel user;
	
	//@OrderBy("id")
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "post_comentarios",
	joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "comentario_id"))
	private List<Comentario> comentarios = new ArrayList<Comentario>();
	
	
}
