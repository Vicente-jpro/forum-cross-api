package ao.angola.demo.dto;

import java.util.ArrayList;
import java.util.List;

import ao.angola.demo.enums.StatusAprovacao;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ao.angola.demo.entities.Comentario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "titulo", "descricao", "approved", "visible", "user", "comentarios"})
public class PostResponseDTO {

	@JsonProperty("id")
	private Long id;
	
	@NotBlank(message = "Campo titulo nao pode estar vasio.")
	@JsonProperty("titulo")
	private String titulo;

	@NotBlank(message = "Campo não pode estar vazio descricao.")
	@JsonProperty("descricao")
	private String descricao;


	//@NotBlank(message = "Campo não pode estar vazio aprovação.")
	@JsonProperty("status_aprovacao")
	private StatusAprovacao statusAprovacao;
	
	@JsonProperty("user")
	private UserResponseDTO user;

	@JsonProperty("visible")
	private boolean visible;

	@JsonProperty("comentarios")
	private List<ComentarioDTO> comentarios = new ArrayList<ComentarioDTO>();
}
