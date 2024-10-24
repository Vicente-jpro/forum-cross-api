package ao.angola.demo.controllers;

import ao.angola.demo.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ao.angola.demo.entities.UserModel;
import ao.angola.demo.exceptions.SenhaInvalidaException;
import ao.angola.demo.exceptions.UsuarioException;
import ao.angola.demo.security.jwt.JwtService;
import ao.angola.demo.service.EmailService;
import ao.angola.demo.service.UsuarioServiceImpl;
import ao.angola.demo.util.CurrentUser;
import ao.angola.demo.util.TokenUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    
    private final EmailService emailService;
    
    @Value("${security.url.account.reset}")
    private String urlAccountReset;

    @Value("${security.url.account.confirmation.send}")
    private String urlAccountConfirmation;
    
    
  /*      	  
    @Operation("Save a user and send email to confirm account.")
    @ApiResponses({
    	@ApiResponse( code = 201, message = "User saved sussefully."),
    	@ApiResponse( code = 401, message = "")
    })
    */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO save( @RequestBody @Valid UserDTO userDTO ) throws MessagingException{
    	 UserModel user = new UserModel();
    	 
    	boolean isPasswordEqual = user.isPasswordEquals(userDTO.getPassword(), userDTO.getPasswordConfirmed());
        
    	if(isPasswordEqual) {
	    	String senhaCriptografada = passwordEncoder.encode(userDTO.getPassword());
	        
	        userDTO.setPassword(senhaCriptografada);
	        user = modelMapper.map(userDTO, UserModel.class);
	        String token = jwtService.gerarToken(user);
	        
	        user.setActivated(false);
	        user.setTokenConfirmedAccount(token);
	        
	        UserModel userSaved = new UserModel();
	     
            userSaved = usuarioService.salvar(user);
    
	        //Send email to the user with this address.
	        emailService.sendEmailWithLink(userSaved.getEmail(), "CONFIRM ACCOUNT INSTRUCTION",
	        		urlAccountConfirmation+userSaved.getTokenConfirmedAccount());
	        
	        userDTO.setId(userSaved.getId());
	        UserResponseDTO useResponseDto = modelMapper.map(userDTO, UserResponseDTO.class);
	        return useResponseDto;
        }
        log.error("Confirmed password is diferent: {}", userDTO.getEmail());
    	throw new UsuarioException("Password is diferent: "+ userDTO.getEmail());
        
    }
    
    /*
    @ApiOperation("Resend account confirmed email to confirm account.")
    @ApiResponses({
    	@ApiResponse( code = 201, message = "User saved sussefully."),
    	@ApiResponse( code = 401, message = "")
    }) */
    @PostMapping(path="/account/confirmed/resend", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void accountConfirmedResend( @RequestBody UserEmailDTO userEmail ) throws MessagingException{
    
    	UserModel usuario = new UserModel();
        usuario.setEmail(userEmail.getEmail());
        
	    UserModel usuarioAutenticado = usuarioService.autenticarEmail(usuario);
	    String token = jwtService.gerarToken(usuarioAutenticado);
	    TokenDTO tokenReceived = new TokenDTO(usuario.getEmail(), token);
	    
	    usuarioAutenticado.setTokenConfirmedAccount(token);
	    this.usuarioService.salvar(usuarioAutenticado);
	    
	    //Send email to the user with this address.
	    emailService.sendEmailWithLink(usuarioAutenticado.getEmail(), "CONFIRM ACCOUNT INSTRUCTION",
	    		urlAccountConfirmation+tokenReceived.getToken());
	    
	    System.out.println(urlAccountConfirmation+tokenReceived.getToken());
  
        
    }
    
    /*
    @ApiOperation("Confirme account created")
    @ApiResponses({
    	@ApiResponse( code = 200, message = "Account confirmated successfully."),
    	@ApiResponse( code = 401, message = "Can not confirme your account. Token does not exit.")
    })*/
    @PostMapping("/account/confirmed")
    public void accountConfirm(@RequestParam("token") String token){
    
    	UserModel user = this.usuarioService.findByTokenConfirmAccount(token);
    	if(user != null) {
    	   user.setActivated(true);
     	   this.usuarioService.salvar(user);
    	}else {	
    	log.error("Password is diferent");
    	throw new UsuarioException("Password is diferent.");
    	}
    }
    
/*
    @ApiOperation("Authenticate the user and return a token to access API resourses.")
    @ApiResponses({
    	@ApiResponse( code = 200, message = "User authenticated successfully."),
    	@ApiResponse( code = 401, message = "Can invalide credential or you need to verificate your account to access.")
    })
    */
    @PostMapping(path ="/auth", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public TokenDTO autenticar(@RequestBody @Valid CredenciaisDTO credenciais){
        try{
            
        	UserModel usuario = new UserModel();
                    usuario.setEmail(credenciais.getEmail());
                    usuario.setPassword(credenciais.getPassword());
            
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getEmail(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    /*
    @ApiOperation("Verify if user exist and send the reset password instructions.")
    @ApiResponses({
    	@ApiResponse( code = 200, message = "Instruction sent successfully."),
    	@ApiResponse( code = 401, message = "Cannot send the email instructions to create a new password.")
    })
    */
    @PostMapping(path = "/password/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void passowrdNew(@RequestBody @Valid UserEmailDTO userEmail) throws MessagingException{
        try{
            
        	UserModel usuario = new UserModel();
                    usuario.setEmail(userEmail.getEmail());
                
            UserModel usuarioAutenticado = usuarioService.autenticarEmail(usuario);
            String token = jwtService.gerarToken(usuarioAutenticado);
            TokenDTO tokenReceived = new TokenDTO(usuario.getEmail(), token);
            
            usuarioAutenticado.setTokenResetPassword(token);
            this.usuarioService.salvar(usuarioAutenticado);
            
            //Send email to the user with this address.
            emailService.sendEmailWithLink(usuarioAutenticado.getEmail(), "PASSWORD RESET INSTRUCTION",
            		urlAccountReset+tokenReceived.getToken());
            
            System.out.println(urlAccountReset+tokenReceived.getToken());
            
        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
    

    /*
    @ApiOperation("Verify if user TokenResetPassword exist and change the password.")
    @ApiResponses({
    	@ApiResponse( code = 200, message = "Instruction sent successfully."),
    	@ApiResponse( code = 401, message = "Cannot send the email instructions to create a new password.")
    })
    */
    @PostMapping(path ="/password/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void passowrdReset(@RequestBody UserPasswordRestDTO userPasswordRestDTO, @RequestParam("token") String token){
    
    	UserModel user = this.usuarioService.findByTokenResetPassword(token);
    	boolean tokenEquals = user.isTokenEquals(user.getTokenResetPassword(), token);
    	
    	if(tokenEquals) {
    		boolean passwordEqual = 
    				user.isPasswordEquals(
    						userPasswordRestDTO.getNewPassword(), 
    						userPasswordRestDTO.getConfirmePassword());
    		
    		if(passwordEqual) {
    		   String senhaCriptografada = passwordEncoder.encode(userPasswordRestDTO.getNewPassword()); 
    		   
    		   user.setTokenResetPassword(TokenUtil.NO_TOKEN_GENERATED);
    		   user.setPassword(senhaCriptografada);

    		   this.usuarioService.salvar(user);
    		}else {
    			log.error("Password is diferent");
    			throw new UsuarioException("Password is diferent.");
    		}
    	}

    }
    
    @GetMapping("/current_user")
	public CurrentUser getAuthenticatedUser(Authentication authentication) {
		CurrentUser user = modelMapper.map(authentication.getPrincipal(), CurrentUser. class);
		if (user != null)
			return user;
		throw new UsernameNotFoundException("You need to loggin before authenticate.");
	}

    @GetMapping
    public List<UserProfileResponseDTO> findAllProfiles(){

        return usuarioService.findAllProfiles()
                             .stream()
                             .map( userPerfil -> {
                                 UserProfileResponseDTO userProfileResponseDTO =
                                         modelMapper.map(userPerfil, UserProfileResponseDTO.class);

                                 return userProfileResponseDTO;
                             }).collect(Collectors.toList());
    }



}
