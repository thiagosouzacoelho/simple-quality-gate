package br.com.sqg.exception;

public class KeyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 2400944182131986639L;

	public KeyRegisteredException(String key) {
		super(key);
	}

}
