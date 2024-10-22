package ao.angola.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ao.angola.demo.entities.Comentario;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "titulo", "descricao", "approved", "user", "comentarios"})
public class PostResponseDTO {

	@JsonProperty("id")
	private Long id;
	
	@NotBlank(message = "Campo titulo nao pode estar vasio.")
	@JsonProperty("titulo")
	private String titulo;

	@NotBlank(message = "Campo titulo descricao.")
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("approved")
	private boolean approved;
	
	@JsonProperty("user")
	private UserResponseDTO user;
	
	@JsonProperty("comentarios")
	private List<ComentarioDTO> comentarios = new ArrayList<ComentarioDTO>();
}
