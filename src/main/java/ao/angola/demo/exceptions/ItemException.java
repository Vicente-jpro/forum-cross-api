package ao.angola.demo.exceptions;

public class ItemException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ItemException(String errorMessage){
		super(errorMessage);
	}

}
