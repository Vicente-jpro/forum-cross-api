package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}
