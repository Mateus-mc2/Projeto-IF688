package util.exceptions;

public class InvalidPathException extends Exception {
	private static final long serialVersionUID = 4210334209809178306L;
	
	public InvalidPathException() {
		super("O caminho especificado não existe!");
	}
}
