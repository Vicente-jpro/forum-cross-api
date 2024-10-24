package ao.angola.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import ao.angola.demo.security.jwt.JwtAuthFilter;
import ao.angola.demo.security.jwt.JwtService;
import ao.angola.demo.service.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration{

    @Autowired
    private UsuarioServiceImpl usuarioService;
    
    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }
  
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioService)
            .passwordEncoder(passwordEncoder());
    }
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                     .requestMatchers("/swagger-ui/**")
                        .permitAll()
                     .requestMatchers("/items/**")
                        .hasAnyRole("USER", "ADMIN")
                     .requestMatchers("/stock_movements/**")
                        .hasAnyRole("USER", "ADMIN")
                     .requestMatchers("/posts/**")
                        .hasAnyRole("USER", "ADMIN")
                     //.requestMatchers("/user/auth").permitAll()
                     .requestMatchers(HttpMethod.POST, "/users/**")
                        .permitAll()
                     .anyRequest().authenticated()

                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

/*
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
    */
}
