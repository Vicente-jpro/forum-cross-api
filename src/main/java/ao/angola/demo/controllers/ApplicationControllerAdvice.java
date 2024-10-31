package ao.angola.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import ao.angola.demo.exceptions.*;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ao.angola.demo.util.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @Setter
    public static int errorCode = 0;

    @ExceptionHandler(PostException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlePostException(PostException ex){
        String mensagemErro = ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.BAD_REQUEST.value());
        return new ApiErrors(mensagemErro, errorCode);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrors handleSenhaInvalidaException(SenhaInvalidaException ex){
        String mensagemErro = ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.FORBIDDEN.value());
        return new ApiErrors(mensagemErro, errorCode);
    }

    @ExceptionHandler(UsuarioException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleUsuarioException( UsuarioException ex ){
        String mensagemErro = ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.NOT_FOUND.value());
        return new ApiErrors(mensagemErro, errorCode);

    }
    
    @ExceptionHandler(ComentarioException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleComentarioException( ComentarioException ex ){
        String mensagemErro = ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.NOT_FOUND.value());
        return new ApiErrors(mensagemErro, errorCode);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleDatabaseOperationException( DatabaseOperationException ex ){
        String mensagemErro = ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.NOT_FOUND.value());
        return new ApiErrors(mensagemErro, errorCode);
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlePessoaException(NumberFormatException ex){
        String mensagemErro = "Invalid URI "+ex.getMessage();
        errorCode = setDefaultError(errorCode, HttpStatus.BAD_REQUEST.value());
        return new ApiErrors(mensagemErro, errorCode);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidException( MethodArgumentNotValidException ex ){
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrors(errors, HttpStatus.BAD_REQUEST.value());
    }

    private int setDefaultError(int errorCode, int defaultErrorCode){
        if (errorCode == 0){
            return defaultErrorCode;
        }
        return errorCode;
    }
}
