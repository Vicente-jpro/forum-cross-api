package ao.angola.demo.exceptions;

public class PostException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PostException(String errorMessage) {
		super(errorMessage);
	}

}
