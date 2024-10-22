package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{

}
