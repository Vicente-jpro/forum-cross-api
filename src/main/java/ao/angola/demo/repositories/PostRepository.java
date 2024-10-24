package ao.angola.demo.repositories;

import java.util.List;

import ao.angola.demo.enums.StatusAprovacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Post;
import ao.angola.demo.entities.UserModel;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	
	@Query(value = "select * from posts p"
			+ "	inner join users "
			+ "	on users.id_user = p.user_id "
			+ "	left join comentarios "
			+ "	on comentarios.id_user = users.id "
			+ " where comentarios.id = :id_comentario and p.id = :id_post ",
			nativeQuery = true)
	Post findByIdAndCommentId( @Param("id_comentario") Long idComentario, @Param("id_post")  Long idPost);
	Post findByIdAndUser(Long idPost, UserModel user);
	List<Post> findByStatusAprovacao(StatusAprovacao statusAprovacao);

}
