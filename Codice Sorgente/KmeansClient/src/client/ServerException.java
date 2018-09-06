package client;

/**
 * Classe che modella i vari fallimenti di connessione al server.
 * 
 * @author Lorenzo
 *
 */
public class ServerException extends Exception {
	/**
	 * Costruttore di classe che stampa il messaggio di errore passato come
	 * argomento.
	 * 
	 * @param text
	 *            Stringa corrispondente al messaggio da visualizzare.
	 */
	public ServerException(String text) {
		System.err.println(text);
	}
}
