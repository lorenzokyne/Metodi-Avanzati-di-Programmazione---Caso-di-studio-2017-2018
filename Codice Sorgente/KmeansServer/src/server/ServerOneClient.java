package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import data.Data;
import data.OutOfRangeSampleSize;
import database.EmptySetException;
import database.NoValueException;
import mining.KmeansMiner;

/**
 * Classe che gestisce le richieste del client.
 * 
 * @author Lorenzo
 *
 */
public class ServerOneClient extends Thread {
	/**
	 * Attributo di tipo Socket su cui verrà effettuata la connessione.
	 */
	private Socket socket;
	/**
	 * Attributo di tipo ObjectInputStream che acquisisce dati sottoforma di oggetti
	 * dal client.
	 */
	private ObjectInputStream in;
	/**
	 * Attributo di tipo ObjectOutputStream che invia dati sottoforma di oggetti al
	 * client.
	 */
	private ObjectOutputStream out;
	/**
	 * Attributo di tipo KmeansMiner che conterrà dati riguardanti i cluster
	 * acquisiti secondo le richieste.
	 */
	private KmeansMiner kmeans;

	/**
	 * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il
	 * thread.
	 * 
	 * @param s
	 *            Indica il socket che ha effettuato la richiesta di connessione al
	 *            server.
	 * @throws IOException
	 *             Eccezione che si verifica nel caso non sia possibile
	 *             ricevere/inviare dati al server.
	 */
	public ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		start();
	}

	/**
	 * Overriding del metodo run() della superclasse che gestisce le richieste del
	 * client.
	 */
	public void run() {
		try {
			while (!socket.isClosed() && socket.isConnected()) {
				try {
					int operazione = (Integer) in.readObject();
					switch (operazione) {
					case (0):
						acquisizioneTabella();
						break;
					case (3):
						letturaClusterdaFile();
						break;
					}
				} catch (SocketException e) {
					socket.close();
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Client chiuso");
		} catch (IOException e) {
			System.out.println("Client chiuso");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo che gestisce l'acquisizione dei dati dalla tabella di database e la
	 * successiva serializzazione: Mining.
	 * 
	 * @throws IOException
	 *             In caso di problemi con lo scambio dati con il client.
	 */
	private void acquisizioneTabella() throws IOException {
		char exit = 'y';
		try {
			String nomeTabella = (String) in.readObject();
			System.out.println(nomeTabella);
			try {
				Data data = new Data(nomeTabella);
				out.writeObject("OK");
				if (((Integer) in.readObject()).equals(1)) {
					int numeroIterazioni = (Integer) in.readObject();
					kmeans = new KmeansMiner(numeroIterazioni);
					out.writeObject("OK");
					try {
						int num = kmeans.kMeans(data);
						out.writeObject(num); // Invia il numero di iterate effettuate al client.
						out.writeObject(kmeans.toString());
						Integer x = ((Integer) in.readObject());
						if (x.equals(2)) {
							out.writeObject("OK");
						} else {
							return;
						}
						String fileName = (String) in.readObject();
						kmeans.salva(fileName);
					} catch (OutOfRangeSampleSize e) {
						out.writeObject(-1);
					}
				} else {
					return;
				}
			} catch (SQLException e) {
				out.writeObject("Tabella non trovata!");
			} catch (EmptySetException e) {
				out.writeObject("Set vuoto!");
			} catch (NoValueException e1) {
				out.writeObject("Valore assente.");
			} catch (OutOfRangeSampleSize e1) {
				// TODO Auto-generated catch block
				out.writeObject("Out of range!");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metodo che realizza la deserializzazione dei cluster richiesti dal client.
	 */
	private void letturaClusterdaFile() {
		try {
			String nomeFile = (String) in.readObject();
			kmeans = new KmeansMiner(nomeFile);
			if (kmeans != null) {
				out.writeObject("OK");
				out.writeObject(kmeans.toString());
			}
		} catch (ClassNotFoundException | IOException e) {
			try {
				out.writeObject("File non trovato.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
