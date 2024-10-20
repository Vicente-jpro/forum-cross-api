package ao.angola.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ao.angola.demo.entities.Provincia;
import ao.angola.demo.exceptions.ProvinciaNotFoundException;
import ao.angola.demo.repositories.ProvinciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProvinciaService {

	private final ProvinciaRepository provinciaRepository;
	
	public List<Provincia> getProvincias(){
		log.info("Buscando todas as provincias");
		return provinciaRepository.findAll();
	}
	
	public Provincia getProvinciaById(Long idProvincia){
		log.info("Buscando provincia pelo Id: {}", idProvincia);
		
		Provincia provincia =  provinciaRepository.findById(idProvincia).get();
		if (provincia == null) {
			log.error("Provincia nao encontrada. Identificador invalido");
			throw new ProvinciaNotFoundException("Provincia nao encontrada. Identificador invalido");
			
		}
		return provincia;
	}
}
