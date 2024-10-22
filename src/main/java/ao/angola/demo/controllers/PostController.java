package ao.angola.demo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ao.angola.demo.dto.PostDTO;
import ao.angola.demo.dto.PostResponseDTO;
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
	private final ModelMapper modelMapper;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponseDTO salvar(@RequestBody @Valid PostDTO postDTO, @LoggedInUser CurrentUser currentUser) {

		Post post = modelMapper.map(postDTO, Post.class);
		UserModel user = currentUser.getUser();

		post.setUser(user);
		post.setApproved(false);
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
		Post post = postService.findByIdAndUser(idPost, user);
		
		Post novoPost = modelMapper.map(postDTO, Post.class);
		novoPost.setId(post.getId());
		novoPost.setUser(user);
		
		Post postAtualizado = this.postService.atualizar(novoPost, idPost);		
		PostResponseDTO postResponseDTO = modelMapper.map(postAtualizado, PostResponseDTO.class);
	
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
	
	
	@GetMapping("/upproved")
	@ResponseStatus(HttpStatus.OK)
	public List<Post> findByApprovedTrue(){
		return postService.findByApprovedTrue();
	}
	
	@GetMapping("/unpproved")
	@ResponseStatus(HttpStatus.OK)
	public List<Post> findByApprovedFalse(){
		return postService.findByApprovedFalse();
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Post> findAll(){
		return postService.findAll();
	}
	

}
