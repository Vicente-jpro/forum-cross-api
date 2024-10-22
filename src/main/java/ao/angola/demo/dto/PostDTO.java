package ao.angola.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "titulo", "descricao"})
public class PostDTO {
	
	@JsonProperty("id")
	private Long id;
	
	@NotBlank(message = "Campo titulo nao pode estar vasio.")
	@JsonProperty("titulo")
	private String titulo;

	@NotBlank(message = "Campo titulo descricao.")
	@JsonProperty("descricao")
	private String descricao;
	
}
