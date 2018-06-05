package br.com.sqg.exception;

public class NotStartOrEndWithParenthesesException extends RuntimeException {

	private static final long serialVersionUID = 2659871112115022177L;

	
	public NotStartOrEndWithParenthesesException(String parentheses) {
		super(parentheses);
	}
}
