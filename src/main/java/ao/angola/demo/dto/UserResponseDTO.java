package ao.angola.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("user_name")
	private String userName;
	
	@JsonProperty("full_name")
	private String fullName;
	
	@JsonProperty("enabled")
	private Boolean enabled = false;
	
	@JsonProperty("permissions")
	private List<PermissionDTO> permissions;
	
}