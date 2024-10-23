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

import ao.angola.demo.dto.PerfilDTO;
import ao.angola.demo.entities.Perfil;
import ao.angola.demo.exceptions.PerfilException;
import ao.angola.demo.service.PerfilService;
import ao.angola.demo.util.CurrentUser;
import ao.angola.demo.util.LoggedInUser;
import ao.angola.demo.util.SelfLinkHateoas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/perfils")
public class PerfilController {

	private final PerfilService perfilService;
	private final ModelMapper modelMapper;
	
	@PostMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PerfilDTO salvar(@RequestBody PerfilDTO perfilDTO, 
			@LoggedInUser CurrentUser currentUser) {
		try {

			Perfil perfil = perfilService.salvar(perfilDTO, currentUser.getUser());
			PerfilDTO perfilResponseDTO = modelMapper.map(perfil, PerfilDTO.class);
			
			Link selfLink = SelfLinkHateoas.getLink(PerfilDTO.class, perfilResponseDTO.getId());
			perfilResponseDTO.add(selfLink);
			
			return perfilResponseDTO;
		} catch (Exception e) {
			log.error("Usuario ja possue um perfil ID: "+currentUser.getUser().getId());
			throw new PerfilException("Usuario ja possue um perfil ");
		}
		
	}
	
	@PatchMapping(path = "/{id_perfil}", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PerfilDTO atualizar(@RequestBody PerfilDTO perfilRequestDTO, 
			@PathVariable("id_perfil") Long idPerfil,
			@LoggedInUser CurrentUser currentUser) {	
		
		Perfil perfilSalvo = this.perfilService.atualizar(perfilRequestDTO, currentUser.getUser());
		PerfilDTO perfilResponseDto = modelMapper.map(perfilSalvo, PerfilDTO.class);
		
		Link selfLink = SelfLinkHateoas.getLink(PerfilDTO.class, perfilResponseDto.getId());
		perfilResponseDto.add(selfLink);
		
		return perfilResponseDto;
	}
	
	
	@GetMapping( path = "/user",  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PerfilDTO findByUser(@LoggedInUser CurrentUser currentUser) {
		Perfil perfil = this.perfilService.findByUser(currentUser.getUser());
		PerfilDTO perfilResponseDto = this.modelMapper.map(perfil, PerfilDTO.class);

		Link selfLink = SelfLinkHateoas.getLink(PerfilDTO.class, perfilResponseDto.getId());
		perfilResponseDto.add(selfLink);
		
		return perfilResponseDto;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<PerfilDTO> findAll() {

		return this.perfilService.findAll()
					.stream()
				.map( perfil ->{
					PerfilDTO perfilResponseDto = this.modelMapper.map(perfil, PerfilDTO.class);
					return perfilResponseDto;
				}).collect(Collectors.toList());

	}




}
