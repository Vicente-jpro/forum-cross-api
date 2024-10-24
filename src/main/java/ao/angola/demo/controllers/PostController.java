package ao.angola.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import ao.angola.demo.enums.StatusAprovacao;
import ao.angola.demo.service.ComentarioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ao.angola.demo.dto.ComentarioDTO;
import ao.angola.demo.dto.PostDTO;
import ao.angola.demo.dto.PostResponseDTO;
import ao.angola.demo.entities.Comentario;
import ao.angola.demo.entities.Post;
import ao.angola.demo.entities.UserModel;
import ao.angola.demo.service.PostService;
import ao.angola.demo.util.CurrentUser;
import ao.angola.demo.util.LoggedInUser;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final ComentarioService comentarioService;
	private final ModelMapper modelMapper;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponseDTO salvar(@RequestBody @Valid PostDTO postDTO, @LoggedInUser CurrentUser currentUser) {

		Post post = modelMapper.map(postDTO, Post.class);
		UserModel user = currentUser.getUser();

		post.setUser(user);
		post.setStatusAprovacao(StatusAprovacao.PENDENTE);
		post.setVisible(true);

		Post postSalvo = postService.salvar(post);

		postDTO.setId(post.getId());

		PostResponseDTO postResponseDTO = modelMapper.map(postSalvo, PostResponseDTO.class);

		return postResponseDTO;
	}




	@PatchMapping(value = "/{id_post}", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDTO atualizar(@RequestBody PostDTO postDTO, @PathVariable("id_post") Long idPost,
			@LoggedInUser CurrentUser currentUser) {

		UserModel user = currentUser.getUser();

		postDTO.setId(idPost);
		Post novoPost = modelMapper.map(postDTO, Post.class);
		Post postAtualizado = this.postService.atualizar(novoPost, user);
		PostResponseDTO postResponseDTO = modelMapper.map(postAtualizado, PostResponseDTO.class);
	
		return postResponseDTO;
	}

	@PatchMapping(value = "/{id_post}/approvation",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDTO changeApprovation(
							 @PathVariable("id_post") Long idPost,
							 @LoggedInUser CurrentUser currentUser) {

		UserModel user = currentUser.getUser();
		Post post = postService.atualizarAprovacao(idPost, user);

		PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);

		return postResponseDTO;
	}

	@PatchMapping(value = "/{id_post}/visibility",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDTO changeVisibitlity(
			@PathVariable("id_post") Long idPost,
			@LoggedInUser CurrentUser currentUser) {

		UserModel user = currentUser.getUser();
		Post post = postService.atualizarVisibilidade(idPost, user);

		PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);

		return postResponseDTO;
	}


	@GetMapping(value = "/{id_post}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDTO findById(@PathVariable("id_post") Long idPost) {

		Post post = postService.findById(idPost);
		PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);

		return postResponseDTO;
	}

	@DeleteMapping("/{id_post}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable("id_post") Long idPost, @LoggedInUser CurrentUser currentUser) {

		Post post = postService.findByIdAndUser(idPost, currentUser.getUser());
		this.postService.eliminar(post.getId());

	}

	@RolesAllowed("ADMIN")
	@DeleteMapping("/admin/{id_post}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable("id_post") Long idPost) {

		Post post = postService.findById(idPost);
		this.postService.eliminar(post.getId());

	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<PostResponseDTO> findAll(){
		
		return postService.findAll()
				  .stream()
				  .map(post -> {
					 
					  PostResponseDTO postResponseDTO = 
							  modelMapper.map(post, PostResponseDTO.class);
					  return postResponseDTO;
				  }).collect(Collectors.toList());
	
	}
	
	
	@GetMapping("/approvation")
	@ResponseStatus(HttpStatus.OK)
	public List<PostResponseDTO> findByStatusApprovation(@RequestParam("status") String status){
		StatusAprovacao statusAprovacao = StatusAprovacao.valueOf(status);
		return postService.findByStatusApprovation(statusAprovacao)
				  .stream()
				  .map(post -> {
					 
					  PostResponseDTO postResponseDTO = 
							  modelMapper.map(post, PostResponseDTO.class);
					  return postResponseDTO;
				  }).collect(Collectors.toList());

	}
	

	
	@PostMapping(value = "/{id_post}/comentarios", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponseDTO comentar(@RequestBody @Valid ComentarioDTO comentarioDTO,
			@PathVariable("id_post") Long idPost, @LoggedInUser CurrentUser currentUser){
		
		Comentario comentario = modelMapper.map(comentarioDTO, Comentario.class);
		comentario.setUser(currentUser.getUser());
		
		Post post = this.postService.comentar(comentario, idPost);
		PostResponseDTO responseDTO = modelMapper.map(post, PostResponseDTO.class);
		
		return responseDTO;
		
	}
	
	@PatchMapping(value = "/{id_post}/comentarios/{id_comentario}", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponseDTO atualizarCometario(@RequestBody ComentarioDTO comentarioDTO, 
			@PathVariable("id_post") Long idPost, 
			@PathVariable("id_comentario") Long idComentario,
			@LoggedInUser CurrentUser currentUser){
		
		Comentario comentario = modelMapper.map(comentarioDTO, Comentario.class);
		comentario.setUser(currentUser.getUser());
		comentario.setId(idComentario);
		
		Post post = this.postService.atualizarComentario(comentario, idPost);
		PostResponseDTO responseDTO = modelMapper.map(post, PostResponseDTO.class);
		
		return responseDTO;
		
	}


	// Não foi testado
	@DeleteMapping(value = "/{id_post}/comentarios/{id_comentario}")
	@ResponseStatus(HttpStatus.CREATED)
	public void eliminarCometario( @PathVariable("id_comentario") Long idComentario,
											  @LoggedInUser CurrentUser currentUser){
		comentarioService.eliminar(idComentario, currentUser.getUser());
	}


}
