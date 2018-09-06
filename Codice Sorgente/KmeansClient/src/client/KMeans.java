package client;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Classe che si occupa di stabilire la connessione al Server.
 * 
 * @author Fernando.
 *
 */
public class KMeans extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Stringa che corrisponde all'ip al quale connettersi (localhost default).
	 */
	static String ip = "localhost";
	/**
	 * Porta dell'ip al quale connettersi.
	 */
	static int port = 8080;

	/**
	 * Classe che modella il contenitore grafico dei pannelli.
	 * 
	 * @author Fernando.
	 *
	 */
	private class TabbedPane extends JPanel {
		private static final long serialVersionUID = 1L;
		/**
		 * Pannello per l operazione di mine.
		 */
		private JPanelCluster panelDB;
		/**
		 * Pannello per l'operazione di Store From File.
		 */
		private JPanelCluster panelFile;

		/**
		 * Classe che modella i pannelli per minare o caricare dati.
		 * 
		 * @author Fernando.
		 *
		 */
		private class JPanelCluster extends JPanel {
			private static final long serialVersionUID = 1L;
			/**
			 * Text in cui inserire il nome della tabella da cui estrarre i dati da minare.
			 */
			private JTextField tableText = new JTextField(20);
			/**
			 * Text in cui inserire il numero di cluster da calcolare.
			 */
			private JTextField kText = new JTextField(10);
			/**
			 * Text che contiene il nome del file in cui salvare/caricare dati.
			 */
			private JTextField fileText = new JTextField(15);
			/**
			 * Area in cui stampare a video i dati minati/caricati da file.
			 */
			private JTextArea clusterOutput = new JTextArea();
			/**
			 * Bottone di esecuzione dei comandi di mine/Store from file.
			 */
			private JButton executeButton;

			/**
			 * Costruttore del pannello che inizializza i componenti grafici ed aggiunge
			 * l'ascoltatore al corrispettivo bottone.
			 * 
			 * @param buttonName
			 *            Stringa che indica il nome del bottone che verrà visualizzato.
			 * @param a
			 *            Ascoltatore che verrà assegnato al bottone.
			 */
			JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				JPanel upPanel = new JPanel();
				if (buttonName.equals("MINE")) {
					upPanel.add(new JLabel("Table:"));
					upPanel.add(tableText);
					upPanel.add(new JLabel("Numero di cluster:"));
					upPanel.add(kText);
					upPanel.add(new JLabel("Nome file:"));
					upPanel.add(fileText);
				} else {
					upPanel.add(new JLabel("Nome file:"));
					upPanel.add(tableText);
				}
				executeButton = new JButton(buttonName);
				executeButton.addActionListener(a);
				upPanel.setLayout(new GridLayout(3, 2));
				add(upPanel);
				JPanel centerPanel = new JPanel();
				centerPanel.setLayout(new GridLayout(1, 1));
				clusterOutput.setEditable(false);
				JScrollPane scrollingArea = new JScrollPane(clusterOutput);
				centerPanel.add(scrollingArea);
				add(centerPanel);
				JPanel bottomPanel = new JPanel();
				bottomPanel.setLayout(new FlowLayout());
				bottomPanel.add(executeButton);
				add(bottomPanel);
			}

		}

		/**
		 * Classe che modella il pannello per la configurazione dell'ip e port.
		 * 
		 * @author Fernando
		 *
		 */
		private class JConfig extends JPanel {
			private static final long serialVersionUID = 1L;
			/**
			 * Text per settare l'ip.
			 */
			private JTextField ipText = new JTextField(15);
			/**
			 * Text per settare la variabile port.
			 */
			private JTextField portText = new JTextField(10);
			/**
			 * Bottone per salvare la nuova configurazione.
			 */
			private JButton executeButton;

			/**
			 * Costruttore del pannello di configurazione che inizializza i componenti
			 * grafici.
			 */
			JConfig() {
				ipText.setText(ip);
				portText.setText(Integer.toString(port));
				setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				JPanel external = new JPanel();
				external.setLayout(new FlowLayout());
				JPanel upPanel = new JPanel();
				upPanel.setLayout(new GridLayout(2, 2));
				upPanel.add(new JLabel("IP:"));
				upPanel.add(ipText);
				upPanel.add(new JLabel("PORT:"));
				upPanel.add(portText);

				external.add(upPanel);

				JPanel bottomPanel = new JPanel();
				bottomPanel.setLayout(new FlowLayout());
				executeButton = new JButton("Apply");
				executeButton.addActionListener(new ActionListener() {

					@Override
					/**
					 * Ascoltatore del bottone che setta i valori di ip e port con quelli presenti
					 * nei campi di testo.
					 */
					public void actionPerformed(ActionEvent arg0) {
						String temp = "";
						temp = ipText.getText().toString();
						if (temp.equals("")) {
							mostraPopup("Attenzione, compilare tutti i campi");
						} else {
							ip = temp;
							temp = portText.getText().toString();
							if (temp.equals("")) {
								mostraPopup("Attenzione, compilare tutti i campi");
							} else {
								port = Integer.parseInt(temp);
								mostraPopup("Configurazione effettuata!");
							}
						}
					}
				});
				bottomPanel.add(executeButton);
				external.add(bottomPanel);
				add(external);
			}

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
		 * @throws InterruptedException
		 */
		private void learningFromDBAction()
				throws SocketException, IOException, ClassNotFoundException, InterruptedException {
			System.out.println(port);
			int k;
			try {
				k = new Integer(panelDB.kText.getText()).intValue();
				tab.panelDB.clusterOutput.setText("Working ...."); // Storing table from db
				out.writeObject(0);
				out.writeObject(tab.panelDB.tableText.getText());
				String answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					mostraPopup(answer);
					tab.panelDB.clusterOutput.setText("Stopped.");
					return;
				} // learning tree
				System.out.println("Starting learning phase!");
				out.writeObject(1);
				out.writeObject(k);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					mostraPopup(answer);
					tab.panelDB.clusterOutput.setText("Stopped.");
					return;
				}
				int numIterazioni = (int) in.readObject(); // legge il numero di iterate dal server
				if (numIterazioni == -1) {
					mostraPopup("Out of Range!!");
					tab.panelDB.clusterOutput.setText("Stopped.");
					return;
				}
				String clusters = (String) in.readObject(); // acquisisce i cluster dal server
				tab.panelDB.clusterOutput.setText("Num iterazioni: " + numIterazioni + "\nClusters: " + clusters);
				out.writeObject(2);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					mostraPopup(answer);
					tab.panelDB.clusterOutput.setText("Stopped.");
					return;
				}
				out.writeObject(panelDB.fileText.getText());
				if (!answer.equals("OK")) {
					mostraPopup(answer);
					tab.panelDB.clusterOutput.setText("Stopped.");
					return;
				} else {
					mostraPopup("Andato a buon fine");

				}
			} catch (NumberFormatException e) {
				mostraPopup(e.toString());
				return;
			}
		}

		/**
		 * Metodo che gestisce il caricamento dei dati salvati all'interno di un file
		 * nel server.
		 * 
		 * @throws SocketException
		 *             quando la connessione al server non viene stabilita
		 *             correttamente.
		 * @throws IOException
		 *             in presenza di errori nell'invio/ricezione dei dati al server.
		 * @throws ClassNotFoundException
		 *             in caso si riceva un Object di tipo sconosciuto.
		 */
		private void learningFromFileAction() throws SocketException, IOException, ClassNotFoundException {
			String nomeTab = panelFile.tableText.getText();
			try {
				out.writeObject(3);
				out.writeObject(nomeTab);
				String answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					mostraPopup(answer);
					return;
				} else {
					answer = in.readObject().toString();
					tab.panelFile.clusterOutput.setText(answer);
					mostraPopup("Caricamento effettuato");
				}
			} catch (NumberFormatException e) {
			}
		}

		/**
		 * Costruttore che aggiunge i pannelli alla tabella principale con le relative
		 * icone.
		 */
		TabbedPane() {
			super(new GridLayout(1, 1));
			JTabbedPane tabbedPane = new JTabbedPane();
			// copy img in src Directory and bin directory
			java.net.URL imgURL = this.getClass().getResource("img/db.jpg");
			ImageIcon iconDB = new ImageIcon(imgURL);
			panelDB = new JPanelCluster("MINE", new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						connection();
						learningFromDBAction();
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ServerException e1) {
						mostraPopup("Connessione non riuscita");
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			tabbedPane.addTab("DB", iconDB, panelDB);
			imgURL = this.getClass().getResource("img/file.jpg");
			ImageIcon iconFile = new ImageIcon(imgURL);
			panelFile = new JPanelCluster("STORE FROM FILE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						connection();
						learningFromFileAction();
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ServerException e1) {
						mostraPopup("Connessione non riuscita");
					}
				}
			});

			tabbedPane.addTab("FILE", iconFile, panelFile);
			JConfig conf = new JConfig();
			imgURL = this.getClass().getResource("img/config.jpg");
			ImageIcon iconConfig = new ImageIcon(imgURL);
			tabbedPane.addTab("CONFIG", iconConfig, conf);
			// Add the tabbed pane to this panel.
			add(tabbedPane);
			// The following line enables to use scrolling tabs.
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}

	}

	/**
	 * Stream per l'invio dei dati al server.
	 */
	private ObjectOutputStream out;// stream con richieste del client
	/**
	 * Stream per la ricezione dei dati dal server
	 */
	private ObjectInputStream in;
	/**
	 * Pannello che gestisce le varie interfacce utente (MINE, STOREFROMFILE e
	 * CONFIG)
	 */
	private TabbedPane tab = new TabbedPane();

	/**
	 * Costruttore che inizializza le componenti grafiche.
	 */
	public KMeans() {
		ImageIcon ic = new ImageIcon(getClass().getResource("img/kmeans.png"));
		setIconImage(ic.getImage());
		Container cp = getContentPane();
		setSize(400, 300);
		cp.setLayout(new GridLayout(1, 0));
		cp.add(tab);
	}

	/**
	 * Metodo per la gestione della connessione al server.
	 * 
	 * @throws ServerException
	 *             quando la connessione al server non viene stabilita
	 *             correttamente.
	 */
	public void connection() throws ServerException {
		try {
			in = null;
			out = null;
			InetAddress addr = InetAddress.getByName(ip); // ip
			System.out.println("addr = " + addr);
			Socket socket = new Socket(addr, port); // Port
			System.out.println(socket);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			tab.panelDB.clusterOutput.setText("Errore");
		}
		if (in == null || out == null) {
			throw new ServerException("Connessione non riuscita");
		}
	}

	/**
	 * Metodo che crea un popup per la gestione degli errori.
	 * 
	 * @param message
	 *            messaggio da stampare nel popup.
	 */
	public void mostraPopup(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public static void main(String[] args) {
		KMeans k = new KMeans();
		k.show();
		k.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
