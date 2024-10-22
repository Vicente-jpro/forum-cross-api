package ao.angola.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Post;
import ao.angola.demo.entities.UserModel;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query(value = "select users.id as id_user, comentarios.id as id_comentario, p.* from posts p"
			+ "	inner join users "
			+ "	on users.id = p.user_id "
			+ "	left join comentarios "
			+ "	on comentarios.user_id = users.id ", 
			nativeQuery = true)
	List<Post> findAllPosts();
	
	Post findByIdAndUser(Long idPost, UserModel user);
	List<Post> findByApprovedTrue();
	List<Post> findByApprovedFalse();
}