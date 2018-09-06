package database;
/**
 * Classe che modella l'assenza di un valore all'interno di un resultset.
 * @author Marco
 *
 */

public class NoValueException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
 *  Metodo che stampa il messaggio di errore.
 */
	public NoValueException() {
		System.err.println("Assenza del valore nel resultSet");
	}
}
