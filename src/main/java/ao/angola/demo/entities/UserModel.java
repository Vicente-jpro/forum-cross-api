package ao.angola.demo.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;
    
    @Column(name = "name")
    @NotEmpty(message = "Username can not be empty.")
    private String name;
    
    @Column(name = "email", unique = true, updatable = true)
    @NotEmpty(message = "email can not be empty.")
    private String email;
    
    @Column(name = "passwrd")
    @NotEmpty(message = "Password can not be empty.")
    private String password;
    
    @Column(name = "admin")
    private boolean admin;
    
    @Column(name = "activated")
    private boolean activated;
    
    @Column(name = "token_reset_password")
    private String tokenResetPassword;
    
    @Column(name = "token_confirmed_account")
    private String tokenConfirmedAccount;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Perfil perfil;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
    
    
    public boolean isTokenEquals(String token1, String token2) {
    	return token1.equalsIgnoreCase(token2);
    }
    
    public boolean isPasswordEquals(String password1, String password2) {
    	return password1.equalsIgnoreCase(password2);
    }
}
