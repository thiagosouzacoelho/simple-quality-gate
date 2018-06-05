package br.com.sqg.exception;

public class InvalidBlocksException extends RuntimeException {

	private static final long serialVersionUID = -2220531212941860172L;
	
	private int amountOfOpenningBlocks;
	private int amountOfClosingBlocks;
	
	public InvalidBlocksException(int amountOfOpenningBlocks, int amountOfClosingBlocks) {
		this.amountOfOpenningBlocks = amountOfOpenningBlocks;
		this.amountOfClosingBlocks = amountOfClosingBlocks;
	}

	@Override
	public String getMessage() {
		return "Wrong openning/closing blocks ratio: " + this.amountOfOpenningBlocks + "/" + this.amountOfClosingBlocks;
	}

}
