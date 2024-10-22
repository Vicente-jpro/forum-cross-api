package ao.angola.demo.service;

import org.springframework.stereotype.Service;

import ao.angola.demo.entities.Comentario;
import ao.angola.demo.exceptions.ComentarioException;
import ao.angola.demo.repositories.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioService {

	private final ComentarioRepository comentarioRepository;
	
	public void eliminar(Long idPost, Long idComentario) {
		log.info("Eliminado Comentario. {}", idComentario);
		
		Comentario comentario = findById(idComentario);
		comentarioRepository.delete(comentario);
	}
	
	public Comentario atualizar(Comentario novoComentario, Long idComentario) {
		log.info("Atualizando o comentario...");
		Comentario coment = findById(idComentario);
		novoComentario.setId(coment.getId());
		
		return salvar(novoComentario);
		
	}

	
	public Comentario salvar(Comentario comentario) {
		log.info("Salvando comentario...");
		return comentarioRepository.save(comentario);
	}
	
	public Comentario findById(Long idComentario) {
		log.info("Buscando o comentario pelo ID: {}", idComentario);
		
		Comentario comentario = this.comentarioRepository.findById(idComentario).get();
		if(comentario == null) {
			log.error("Comentario nao exite ID: {}", idComentario);
			throw new ComentarioException("Comentario nao exite.");
		}
		
		return comentario;
	}
}
