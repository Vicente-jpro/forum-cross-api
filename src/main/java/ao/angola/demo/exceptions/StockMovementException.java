package ao.angola.demo.exceptions;

public class StockMovementException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public StockMovementException (String errorMessage) {
		super(errorMessage);
	}

}
