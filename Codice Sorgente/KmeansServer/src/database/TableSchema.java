package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che modella lo schema della tabella letta dal database.
 * 
 * @author Lorenzo
 *
 */
public class TableSchema {
	/**
	 * Attributo contenente le informazioni relative alla connessione al database.
	 */
	DbAccess db;

	/**
	 * Inner class che modella una colonna della tabella letta.
	 * 
	 * @author Lorenzo
	 *
	 */
	public class Column {
		/**
		 * Attributo che indica il nome della colonna.
		 */
		private String name;
		/**
		 * Attributo che indica il tipo della colonna.
		 */
		private String type;

		/**
		 * Costruttore di classe che inizializza i valori nome e tipo.
		 * 
		 * @param name
		 *            Attributo che indica il nome della colonna.
		 * @param type
		 *            Attributo che indica il tipo della colonna.
		 */
		Column(String name, String type) {
			this.name = name;
			this.type = type;
		}

		/**
		 * Metodo che restituisce il nome della colonna.
		 * 
		 * @return Stringa che indica il nome della colonna.
		 */
		public String getColumnName() {
			return name;
		}

		/**
		 * Metodo che stabilisce se il tipo della colonna è un valore numerico o no.
		 * 
		 * @return Booleano che indica vero se il tipo è numerico, falso altrimenti.
		 */
		public boolean isNumber() {
			return type.equals("number");
		}

		/**
		 * Metodo che restituisce la descrizione della colonna.
		 */
		public String toString() {
			return name + ":" + type;
		}
	}

	/**
	 * Attributo corrispondente alla lista di colonne di cui è formata la tabella
	 * del database.
	 */
	List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Costruttore di classe che instaura una connessione al database ed acquisisce
	 * le informazioni relative alla tabella specificata.
	 * 
	 * @param db
	 *            Contiene le informazioni relative alla connessione al db.
	 * @param tableName
	 *            Nome della tabella di cui si vuole acquisire lo schema.
	 * @throws SQLException
	 *             In caso di errori nell'esecuzione della query.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		this.db = db;

		/**
		 * Mappa dei tipi possibili, usata per convertire i tipi dal db in java.
		 */
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);
		while (res.next()) {
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(
						new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
		}
		db.closeConnection();
		res.close();
	}

	/**
	 * Metodo che restituisce il numero di colonne della tabella.
	 * 
	 * @return Intero che indica il numero di colonne.
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Metodo che restituisce la colonna in posizione specificata.
	 * 
	 * @param index
	 *            Indice di colonna che vogliamo acquisire.
	 * @return Colonna in posizione specificata.
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}
}
