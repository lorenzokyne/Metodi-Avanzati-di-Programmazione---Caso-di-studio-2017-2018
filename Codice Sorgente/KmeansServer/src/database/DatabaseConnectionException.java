package database;

/**
 * Classe che modella il fallimento della connessione al database.
 * 
 * @author Marco
 */

public class DatabaseConnectionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo che stampa il messaggio di errore.
	 */
	public DatabaseConnectionException() {
		System.err.println("Connesione fallita!");
	}
}
