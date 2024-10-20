package ao.angola.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordRestDTO {
	@JsonProperty("new_password")
	private String newPassword;
	
	@JsonProperty("confirme_password")
	private String confirmePassword;
}
