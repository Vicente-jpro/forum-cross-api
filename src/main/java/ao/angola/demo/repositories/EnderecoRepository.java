package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Endereco;
import ao.angola.demo.entities.UserModel;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	Endereco findByUser(UserModel user);
}
