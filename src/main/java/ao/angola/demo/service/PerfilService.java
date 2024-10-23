package ao.angola.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import ao.angola.demo.dto.PerfilDTO;
import ao.angola.demo.entities.Perfil;
import ao.angola.demo.entities.Municipio;
import ao.angola.demo.entities.UserModel;
import ao.angola.demo.exceptions.PerfilException;
import ao.angola.demo.repositories.PerfilRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PerfilService {

	private final PerfilRepository perfilRepository;
	private final MunicipioService municipioService;
	private final ModelMapper modelMapper;
	
	public Perfil salvar(PerfilDTO perfilDTO, UserModel usuario) {
		log.info("Salvando o perfil...");
		
		Perfil perfil = modelMapper.map(perfilDTO, Perfil.class);
		Municipio municipio = municipioService.findById(perfilDTO.getMunicipio().getId());
		perfil.setUser(usuario);
		perfil.setMunicipio(municipio);
		
		return perfilRepository.save(perfil);
	}
	
	public Perfil atualizar(PerfilDTO perfilDTO, UserModel user) {
		log.info("Atualizando o perfil...");		
		Perfil perfilSalvo = findByUser(user);
		perfilDTO.setId(perfilSalvo.getId());
		
		return this.salvar(perfilDTO, user);
	}
	
	public Perfil findByUser( UserModel user) {
		log.info("Buscando o perfil co ID: {}", user.getId());
		
		Perfil perfil = perfilRepository.findByUser(user);
		if(perfil == null) {
			log.error("Perfil nao encontrado ID: {}", user.getId());
			throw new PerfilException("Perfil nao encontrado.");
		}
		
		return perfil;
	}

	public List<Perfil> findAll(){
		return this.perfilRepository.findAll();
	}
	

	
}
