package ao.angola.demo.dto;


import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "morada1", "morada2", "telemovel1", "telemovel2", "municipio"})
public class PerfilDTO extends RepresentationModel<PerfilDTO>{

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("morada1")
	private String morada1;
	
	@JsonProperty("morada2")
	private String morada2;
	
	@JsonProperty("telemovel1")
	private String telemovel1;
	
	@JsonProperty("telemovel2")
	private String telemovel2;
	
	@JsonProperty("municipio")
	private MunicipioDTO municipio;
	
}
