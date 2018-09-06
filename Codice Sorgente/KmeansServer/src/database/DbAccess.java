package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che realizza l'accesso alla base di dati.
 * 
 * @author Marco
 *
 */
public class DbAccess {
	/**
	 * Driver per la connessione al Database.
	 */
	private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	/**
	 * Attributo che indica ilprotocollo con cui ci connettiamo alla base di dati
	 */
	private final String DBMS = "jdbc:mysql";
	/**
	 * Attributo che contiene l’identificativo del server su cui risiede la base di
	 * dati.
	 */
	private final String SERVER = "localhost";
	/**
	 * Attributo che contiene il nome della base di dati.
	 */
	private final String DATABASE = "mapdb";
	/**
	 * Porta su cui il DBMS MySQL accetta le connessioni
	 */
	private final String PORT = "3306";
	/**
	 * Credenziale "User" per l'accesso alla base di dati.
	 */
	private final String USER_ID = "MapUser";
	/**
	 * Credenziale "Password" per l'accesso alla base di dati.
	 */
	private final String PASSWORD = "map";
	/**
	 * Attributo utilizzato per la connessione alla base di dati.
	 */
	private Connection conn;

	/**
	 * Metodo che impartisce al class loader l’ordine di caricare il driver mysql,
	 * inizializza la connessione riferita da conn.
	 * 
	 * @throws DatabaseConnectionException
	 * eccezzione sollevata in caso di fallimento nella connessione al
	 * database.
	 */
	public void initConnection() throws DatabaseConnectionException {
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
			String temp = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
			temp += "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			try {
				conn = DriverManager.getConnection(temp, USER_ID, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseConnectionException();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Metodo che restituisce la connessione al database.
	 * 
	 * @return conn connessione al database.
	 */
	public Connection getConnection() {
		try {
			initConnection();
		} catch (DatabaseConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Metodo che chiude la connessione al database.
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
