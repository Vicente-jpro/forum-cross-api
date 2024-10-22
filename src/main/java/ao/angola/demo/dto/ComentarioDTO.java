package ao.angola.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("comentario")
	private String comentario;
	
	@JsonProperty("user")
	private UserDTO user;
}
