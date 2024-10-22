package ao.angola.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ao.angola.demo.entities.Comentario;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "titulo", "descricao", "user", "comentarios"})
public class PostResponseDTO {

	@JsonProperty("id")
	private Long id;
	
	@NotBlank(message = "Campo titulo nao pode estar vasio.")
	@JsonProperty("titulo")
	private String titulo;

	@NotBlank(message = "Campo titulo descricao.")
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("user")
	private UserResponseDTO user;
	
	@JsonProperty("comentarios")
	private List<Comentario> comentarios = new ArrayList<Comentario>();
}
