package ao.angola.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ao.angola.demo.entities.UserModel;

public interface UsuarioRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);
    UserModel findByTokenResetPassword(String token);
    UserModel findByTokenConfirmedAccount(String token);
    
}
