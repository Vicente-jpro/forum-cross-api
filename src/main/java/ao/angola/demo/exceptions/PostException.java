package ao.angola.demo.exceptions;

import ao.angola.demo.controllers.ApplicationControllerAdvice;
import ao.angola.demo.util.ErrorStatusCode;

import java.io.Serializable;

public class PostException extends RuntimeException implements Serializable, ErrorStatusCode {

	private static final long serialVersionUID = 1L;

	public PostException(String errorMessage, int code) {
		super(errorMessage);
		setErrorCode(code);
	}

	public PostException(String errorMessage) {
		super(errorMessage);
	}



	@Override
	public void setErrorCode(int errorCode){
		ApplicationControllerAdvice.errorCode = errorCode;
	}
}
