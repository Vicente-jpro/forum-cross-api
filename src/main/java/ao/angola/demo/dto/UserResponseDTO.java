package ao.angola.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	private Long id;
	private String userName;	
	private String fullName;
	private Boolean enabled = false;
	private List<PermissionDTO> permissions;
	
	
}