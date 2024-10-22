package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Post;
import ao.angola.demo.entities.UserModel;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Post findByIdAndUser(Long idPost, UserModel user);
}
