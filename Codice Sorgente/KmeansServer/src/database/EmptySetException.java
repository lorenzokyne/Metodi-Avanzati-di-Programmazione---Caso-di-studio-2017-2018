package database;

/**
 * Classe che modella l'errore nel caso il set sia vuoto.
 * 
 * @author Lorenzo
 *
 */
public class EmptySetException extends Exception {
	/**
	 * Costruttore che stampa a video un messaggio.
	 */
	public EmptySetException() {
		System.err.println("EmptySet");
	}
}