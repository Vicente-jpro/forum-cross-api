package ao.angola.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfils")
public class Perfil {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil")
	private Long id;
	
	@Column(name = "morada1", length = 100)
	private String morada1;

	@Column(name = "morada2", length = 100)
	private String morada2;
	
	@Column(name = "telemovel1", length = 20, unique = true, updatable = true)
	private String telemovel1;
	
	@Column(name = "telemovel2", length = 20, unique = true, updatable = true)
	private String telemovel2;
	
	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true, updatable = true)
	private UserModel user;
	

}
