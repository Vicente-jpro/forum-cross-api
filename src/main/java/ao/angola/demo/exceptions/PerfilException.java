package ao.angola.demo.exceptions;

public class PerfilException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PerfilException(String errorMessage) {
		super(errorMessage);
	}

}
