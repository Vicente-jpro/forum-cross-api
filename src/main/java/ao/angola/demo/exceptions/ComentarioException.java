package ao.angola.demo.exceptions;

public class ComentarioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ComentarioException(String errorMessage) {
		super(errorMessage);
	}
	
}
