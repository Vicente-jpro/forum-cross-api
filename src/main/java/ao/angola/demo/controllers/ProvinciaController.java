package ao.angola.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ao.angola.demo.entities.Provincia;
import ao.angola.demo.service.ProvinciaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/provincias")
public class ProvinciaController {

	private final ProvinciaService provinciaService;
	
	@GetMapping( produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Provincia> getProvincias(){
		return provinciaService.getProvincias();
	}
	
	
	@GetMapping(path = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Provincia getProvinciaById(@PathVariable("id") Long idProvincia) {
		return provinciaService.getProvinciaById(idProvincia);
	}
}
