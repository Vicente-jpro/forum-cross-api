package ao.angola.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ao.angola.demo.dto.EnderecoDto;
import ao.angola.demo.entities.Endereco;
import ao.angola.demo.exceptions.EnderecoException;
import ao.angola.demo.service.EnderecoService;
import ao.angola.demo.util.CurrentUser;
import ao.angola.demo.util.LoggedInUser;
import ao.angola.demo.util.SelfLinkHateoas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/enderecos")
public class EnderecoController {

	private final EnderecoService enderecoService;
	private final ModelMapper modelMapper;
	
	@PostMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public EnderecoDto salvar(@RequestBody EnderecoDto enderecoDTO, 
			@LoggedInUser CurrentUser currentUser) {
		try {

			Endereco endereco = enderecoService.salvar(enderecoDTO, currentUser.getUser());
			EnderecoDto enderecoResponseDTO = modelMapper.map(endereco, EnderecoDto.class);
			
			Link selfLink = SelfLinkHateoas.getLink(EnderecoDto.class, enderecoResponseDTO.getId());
			enderecoResponseDTO.add(selfLink);
			
			return enderecoResponseDTO;
		} catch (Exception e) {
			log.error("Usuario ja possue um endereco ID: "+currentUser.getUser().getId());
			throw new EnderecoException("Usuario ja possue um endereco ");
		}
		
	}
	
	@PatchMapping(path = "/{id_endereco}", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public EnderecoDto atualizar(@RequestBody EnderecoDto enderecoRequestDTO, 
			@PathVariable("id_endereco") Long idEndereco,
			@LoggedInUser CurrentUser currentUser) {	
		
		Endereco enderecoSalvo = this.enderecoService.atualizar(enderecoRequestDTO, currentUser.getUser());
		EnderecoDto enderecoResponseDto = modelMapper.map(enderecoSalvo, EnderecoDto.class);
		
		Link selfLink = SelfLinkHateoas.getLink(EnderecoDto.class, enderecoResponseDto.getId());
		enderecoResponseDto.add(selfLink);
		
		return enderecoResponseDto;
	}
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public EnderecoDto findByUser(@LoggedInUser CurrentUser currentUser) {
		Endereco endereco = this.enderecoService.findByUser(currentUser.getUser());
		EnderecoDto enderecoResponseDto = this.modelMapper.map(endereco, EnderecoDto.class);

		Link selfLink = SelfLinkHateoas.getLink(EnderecoDto.class, enderecoResponseDto.getId());
		enderecoResponseDto.add(selfLink);
		
		return enderecoResponseDto;
	}
	
	
	
}
