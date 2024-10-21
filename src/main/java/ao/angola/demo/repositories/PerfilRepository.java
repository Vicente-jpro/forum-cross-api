package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Perfil;
import ao.angola.demo.entities.UserModel;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{
	Perfil findByUser(UserModel user);
}
