package ao.angola.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long>{

}
