package ao.angola.demo.exceptions;

public class ClienteException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ClienteException(String errorMessage) {
		super(errorMessage);
	}

}
