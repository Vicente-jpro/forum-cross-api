package ao.angola.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentario {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id_comentario")
	private Long id;
	
	@Column( name = "comentatio")
	private String comentario;
	
	@ManyToOne
	@JoinColumn( name = "user_id")
	private UserModel user;
}
