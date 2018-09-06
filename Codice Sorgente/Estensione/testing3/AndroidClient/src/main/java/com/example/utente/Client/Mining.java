package com.example.utente.Client;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
* Classe che modella l'interfaccia di richiesta di Mining dati al server e riceve risposta.
* @author Lorenzo.
*/
public class Mining extends Fragment {
    /**
    * stream con richieste del client.
    */
	private ObjectOutputStream out;
	/**
    * stream con risposte del server.
    */
	private ObjectInputStream in;
	/**
	* EditText in cui l'utente sceglierà il nome del file su cui salvare.
    */
	private static EditText filename;
	/**
	* EditText in cui l'utente sceglierà il numero di cluster da calcolare.
    */
	private static EditText ncluster;
	/**
	* EditText in cui verrà mostrato il risultato del minig.
    */
	private static EditText logTab;
	/**
	* EditText in cui l'utente sceglierà il nome della tabella da cui prendere i dati.
    */
	private static EditText tableName;
	/**
	* Socket della connessione al server.
    */
	private static Socket s;
	/**
	* Risultato dell'operazione di mining del server.
    */
	private static String result = "";

	/**
     * Chiamato per avere la user interface istanziata per questo fragment.
     * @param inflater Oggetto di tipo LayoutInflater che può essere usato per riempire le viste nel fragment.
     * @param container Se non-null è il contenitore padre in cui si trova il fragment.
     * @param savedInstanceState Se non-null questo fragment viene ricostruito da uno stato salvato precedente, cioè questo.
     * @return La View per l'interfaccia utente del fragment.
     */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.frag1_layout, container, false);
		operations(view);
		return view;
	}

	/**
     * Metodo che gestisce la connessione al server ed eventualmente mostra all'utente la risposta.
     * @param view La view da cui si gestiscono le parti grafiche (es. bottoni ecc.).
     */
	public void operations(View view) {
        /**
         * Pulsante da premere per inviare la richiesta.
         */
		Button b = (Button) view.findViewById(R.id.button_store_from_file);
		filename = (EditText) view.findViewById(R.id.fileName);
		ncluster = (EditText) view.findViewById(R.id.editText2_k);
		logTab = (EditText) view.findViewById(R.id.logTab);
		tableName = (EditText) view.findViewById(R.id.editText_Frag1);
		b.setOnClickListener(new View.OnClickListener() {
            /**
             * Ascoltatore del pulsante, quando il pulsante viene premuto viene inviata una richiesta al server con i parametri inseriti e si attende risposta.
             * @param view La view da cui si gestiscono le parti grafiche (es. bottoni ecc.).
             */
			@Override
			public void onClick(final View view) {
				try {
					logTab.setText("Working...");
					result = "";
					Thread.sleep(300);
					Thread t = new ThrClick();
					t.start();
					Thread.sleep(2000);
					logTab.setText(result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Metodo che invia richieste al server per la elaborazione e la serializzazione
	 * dei dati.
	 *
	 * @throws SocketException
	 *             gestisce la possibilità che la connessione non sia andata a buon
	 *             fine.
	 * @throws IOException
	 *             in presenza di errori nell'invio/ricezione dei dati al server.
	 * @throws ClassNotFoundException
	 *             in caso si riceva un Object di tipo sconosciuto.
	*/
	private String learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
		int numIterazioni = -1;
		String clusters = "";
		try {
			int k = Integer.parseInt(ncluster.getText().toString());
			out.writeObject(0);
			out.writeObject(tableName.getText().toString());
			String answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				mostraPopup(answer);
				return "";
			}
			out.writeObject(1);
			out.writeObject(k); // legge il numero di iterate dal server
			answer = (String) in.readObject();
			if (!answer.equals("OK")) {
				mostraPopup(answer);
				return "";
			}
			numIterazioni = (int) in.readObject();
			if (numIterazioni == -1) {
				mostraPopup("Out of range!");
				return "";
			}
			clusters = (String) in.readObject();
			out.writeObject(2);
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				mostraPopup("Problem occurred");
				return "";
			}
			out.writeObject(filename.getText().toString());
			if (!answer.equals("OK")) {
				mostraPopup("Problem occurred");
				return "";
			}

		} catch (NumberFormatException e) {
			// JOptionPane.showMessageDialog(this, e.toString());
			return "";
		}
		return ("Num iterazioni: " + numIterazioni + "\nClusters: " + clusters);
	}

	/**
     * Classe che modella il thread su cui avverrà la connessione al server e la trasmissione dei dati.
     */
	class ThrClick extends Thread {
        /**
         * Override del metodo run della classe Thread, stabilisce la connessione al server ed effettua eventualmente la richiesta.
         */
		public synchronized void run() {
			Looper.prepare();
			try {
				InetAddress addr = InetAddress.getByName(MainActivity.ip);
				s = new Socket(addr, MainActivity.port);
				out = new ObjectOutputStream(s.getOutputStream());
				in = new ObjectInputStream(s.getInputStream());
				result = learningFromDBAction();
				s.close();
			} catch (IOException e) {
				mostraPopup("Connessione al server fallita.");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (s != null)
						s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
     * Mostra un popup con il messaggio di errore specificato.
     * @param errText Il messaggio che si vuole mostrare.
     */
	public void mostraPopup(String errText) {
		PopClass p = new PopClass(errText);
		p.show(getFragmentManager(), "example");
	}
}
