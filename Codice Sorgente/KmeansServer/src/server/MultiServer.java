package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che implementa la connessione dei client al server. Il server attende
 * una richiesta di connessione e, se la connessione è accettata, istanzia un
 * oggetto di tipo ServerOneClient.
 * 
 * @author Lorenzo
 *
 */
public class MultiServer {
	/**
	 * Numero di porta su cui verrà hostato il server, di default è la 8080.
	 */
	private int PORT = 8080;

	public MultiServer() {
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Costruttore di classe, inizializza la porta con il valore passato come
	 * parametro ed invoca la run().
	 * 
	 * @param port
	 *            Indica la porta su cui hostare il server.
	 */
	public MultiServer(int port) {
		PORT = port;
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di
	 * richiesta di connessioni da parte del client. Ad ogni nuova richiesta
	 * connessione si istanzia ServerOneClient.
	 * 
	 * @throws IOException
	 *             In caso di errori di scambio dati con il client.
	 */
	private void run() throws IOException {
		ServerSocket ss = new ServerSocket(PORT);
		Socket socket = null;
		try {
			while (true) {
				socket = ss.accept();
				System.out.println("Connessione accettata! " + socket);
				try {
					new ServerOneClient(socket);
				} catch (IOException e) {
					socket.close();
				}
			}
		} finally {
			ss.close();
		}
	}

	/**
	 * istanzia un oggetto di tipo MultiServer
	 * 
	 * @param args
	 *            Indica la porta su cui vogliamo eseguire il server.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			new MultiServer();
		} else {
			new MultiServer(Integer.parseInt(args[0]));
		}
	}
}
