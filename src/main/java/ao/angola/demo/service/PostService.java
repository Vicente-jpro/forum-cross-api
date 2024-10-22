package ao.angola.demo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

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
	private final ModelMapper modelMapper;
	
	public Post salvar(Post post) {
		log.info("Salvando Post...");
		return this.postRepository.save(post);
	}
	
	public Post atualizar(Post post, Long idPost) {
		Post p = findById(idPost);
		post.setId(idPost);
		return salvar(post);
	}
	
	public Post findById(Long idPost) {
		log.info("Buscando Post...");
		Post post = this.postRepository.findById(idPost).get();
		
		if (post == null ) {
			log.error("Post nao existe ID: {}", idPost);
			throw new PostException("Post nao existe.");
		}
		return post;
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
	
	public Post comentar(Post post) {
		log.info("Adicionando comentario no post: {} ", post.getId() );
		Post pt = findById(post.getId());
		
		post.getComentarios()
			.stream()
			.forEach( p -> pt.getComentarios().add(p));
		
		return salvar(post);
		
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
		return postRepository.findAllPosts();
	}
	
	
}