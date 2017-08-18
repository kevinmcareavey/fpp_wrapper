package fpp_wrapper.exception;

public class UnsolvableException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7828659020875852982L;

	public UnsolvableException() {
		super("problem proven unsolvable");
	}

}
