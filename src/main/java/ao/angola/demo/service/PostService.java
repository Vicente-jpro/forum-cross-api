package ao.angola.demo.service;

import java.util.Arrays;
import java.util.List;

import ao.angola.demo.enums.StatusAprovacao;
import org.springframework.stereotype.Service;

import ao.angola.demo.entities.Comentario;
import ao.angola.demo.entities.Post;
import ao.angola.demo.entities.UserModel;
import ao.angola.demo.exceptions.PostException;
import ao.angola.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final ComentarioService comentarioService;
	
	public Post salvar(Post post) {
		log.info("Salvando Post...");
		try {
			return this.postRepository.save(post);
		} catch (Exception e) {
			log.info("Erro ao salvar post. Escolhe outro titulo.");
			throw new PostException("Erro ao salvar post. Escolhe outro titulo já existe");
		}
	}
	
	public Post atualizar(Post post, UserModel user) {
		log.info("Atualizando o post...");
		Post postSalvo = findByIdAndUser(post.getId(), user);
		post.setId(postSalvo.getId());
		return salvar(post);
	}


	public Post atualizarAprovacao(Long idPost, UserModel user) {
		log.info("Atualizando o estado do post...");
		Post postSalvo = findByIdAndUser(idPost, user);

		if(isAprovado(postSalvo)) {
			postSalvo.setStatusAprovacao( StatusAprovacao.APROVADO);
		}else {
			postSalvo.setStatusAprovacao( StatusAprovacao.REPROVADO);
		}
		return salvar(postSalvo);
	}

	public Post atualizarVisibilidade(Long idPost, UserModel user) {
		log.info("Atualizando o estado do post...");
		Post postSalvo = findByIdAndUser(idPost, user);
		postSalvo.setVisible(!postSalvo.isVisible());
		return salvar(postSalvo);
	}


	public Post findById(Long idPost) {
		log.info("Buscando Post...");
		Post post = this.postRepository.findById(idPost).get();
		
		if (post != null ) {
			return post;
		}

		log.error("Post nao existe ID: {}", idPost);
		throw new PostException("Post nao existe.");
	}
	
	public Post findByIdAndUser(Long idPost, UserModel user) {
		log.info("Buscando Post pelo id = {} e user_id = {} ...", idPost, user.getId());
		Post post = this.postRepository.findByIdAndUser(idPost, user);
		
		if (post == null ) {
			log.error("Post nao exite ou pertence a outro usuario: {} / ", idPost);
			throw new PostException("Post nao exite ou pertence a outro usuario.");
		}
		return post;
	}
	
	public Post comentar(Comentario comentario, Long idPost) {
		log.info("Adicionando comentario no post: {} ", idPost );
		
		Post postSalvo = findById(idPost);
		postSalvo.getComentarios().add(comentario);
		
		return salvar(postSalvo);
		
	}
	
	public Post atualizarComentario(Comentario comentario, Long idPost) {
		
		//Colocar exception aqui findByIdAndCommentId
		Post post = postRepository.findByIdAndCommentId(comentario.getId(), idPost);
		if(post == null){
			log.error("Post nao existe ID: {}", idPost);
			throw new PostException("Post nao existe.");
		}
		Comentario coment = post.getComentarios().get(0);
		coment.setComentario(comentario.getComentario());
		
		Comentario cometario = comentarioService.atualizar(coment, comentario.getId());
		List<Comentario> comentarios = Arrays.asList(cometario) ;
		post.setComentarios(comentarios);
		return post;
	}
	
	
	public void eliminar(Long idPost) {
		log.info("Eliminado o post...");
		Post p = findById(idPost);
		this.postRepository.delete(p);
	}
	
	public List<Post> findByApprovedTrue(){
		log.info("Buscando todos os posts aprovados...");
		return postRepository.findByApprovedTrue();
	}
	
	public List<Post> findByApprovedFalse(){
		log.info("Buscando todos os posts nao aprovados...");
		return postRepository.findByApprovedFalse();
	}
	
	public List<Post> findAll(){
		log.info("Buscando todos os posts...");
		return postRepository.findAll();
	}


	private boolean isAprovado(Post post) {
		return post.getStatusAprovacao() == StatusAprovacao.APROVADO;
	}
	
}
