package ao.angola.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ao.angola.demo.dto.AccountCredentialsVO;
import ao.angola.demo.dto.UserDTO;
import ao.angola.demo.dto.UserResponseDTO;
import ao.angola.demo.exceptions.UserException;
import ao.angola.demo.model.User;
import ao.angola.demo.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthServices authServices;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
		if (checkIfParamsIsNotNull(data))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.signin(data);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh token for authenticated user and returns a token")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable("username") String username,
			@RequestHeader("Authorization") String refreshToken) {
		if (checkIfParamsIsNotNull(username, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.refreshToken(username, refreshToken);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}


    @PostMapping(path="/new", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Save a user and send email to confirm account.")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO save( @RequestBody UserDTO userDTO ) {
    	 User user = new User();
    	 user = modelMapper.map(userDTO, User.class);
    	 
    	boolean isPasswordEqual = user.isPasswordEquals(userDTO.getPassword(), userDTO.getConfirmedPassword());
        
    	if(isPasswordEqual) {
	    	String senhaCriptografada = passwordEncoder.encode(userDTO.getPassword());
	        
	    	user.setPassword(senhaCriptografada);
	        
	        User userSaved = new User();
	     
            userSaved = authServices.salvar(user);
    
	        userDTO.setId(userSaved.getId());
	        UserResponseDTO useResponseDto = modelMapper.map(userDTO, UserResponseDTO.class);
	        return useResponseDto;
        }
        log.error("Confirmed password is diferent.");
    	throw new UserException("Password is diferent: "+ userDTO.getUserName());
        
    }
    

	private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() ||
				username == null || username.isBlank();
	}

	private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
		return data == null || data.getUsername() == null || data.getUsername().isBlank()
				 || data.getPassword() == null || data.getPassword().isBlank();
	}
}
