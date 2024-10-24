package ao.angola.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {

	@NotEmpty(message = "Campo Email não pode estar vázio.")
	@JsonProperty("email")
    private String email;

	@NotEmpty(message = "Campo Password não pode estar vázio.")
	@JsonProperty("password")
	private String password;
}
