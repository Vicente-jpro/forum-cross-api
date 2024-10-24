package ao.angola.demo.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ao.angola.demo.entities.UserModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UserModel, Integer> {

    @Query(value = "select * from users u" +
            " left join perfils pf " +
            " on pf.user_id = u.id_user ",
            nativeQuery = true)
    List<UserModel> findAllProfiles();




    Optional<UserModel> findByEmail(String email);
    UserModel findByTokenResetPassword(String token);
    UserModel findByTokenConfirmedAccount(String token);
    
}
