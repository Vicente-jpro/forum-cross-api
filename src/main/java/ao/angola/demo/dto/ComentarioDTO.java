package ao.angola.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
	
	@JsonProperty("id")
	private Long id;

    @NotEmpty(message = "Campo comentario está vázio.")
	@JsonProperty("comentario")
	private String comentario;
	
	@JsonProperty("user")
	private UserDTO user;
}
