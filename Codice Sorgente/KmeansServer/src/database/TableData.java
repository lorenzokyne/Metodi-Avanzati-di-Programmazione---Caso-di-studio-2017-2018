package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import database.TableSchema.Column;

/**
 * Classe che modella una tabella del database e le operazioni su di essa.
 * 
 * @author Lorenzo
 *
 */
public class TableData {
	/**
	 * Attributo contenente le informazioni relative alla connessione al database.
	 */
	static DbAccess db;

	/**
	 * Costruttore inizia il valore di db con quello passato come parametro.
	 * 
	 * @param db
	 *            Attributo contenente le informazioni relative alla connessione al
	 *            database.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione per
	 * estrarre le tuple distinte da tale tabella. Per ogni tupla del resultset, si
	 * crea un oggetto, 4 istanza della classe Example, il cui riferimento va
	 * incluso nella lista da restituire.
	 * 
	 * @param table
	 *            Stringa rappresentante il nome della tabella del database da
	 *            acquisire.
	 * @return Lista di transazioni distinte memorizzate nella tabella.
	 * @throws SQLException
	 *             In presenza di errori nella esecuzione della query.
	 * @throws EmptySetException
	 *             Nel caso il resultSet sia vuoto.
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
		List<Example> temp = new ArrayList<Example>();
		Connection conn = db.getConnection();
		DatabaseMetaData meta = conn.getMetaData();
		Statement s = conn.createStatement();
		String query = "SELECT * " + "FROM " + table;
		ResultSet res = s.executeQuery(query);
		boolean pieno = res.next();
		if (!pieno) {
			throw new EmptySetException();
		}
		while (pieno) {
			TableSchema tb = new TableSchema(db, table);
			Example app = new Example();
			try {
				for (int i = 0; i < tb.getNumberOfAttributes(); i++) {
					Object obj;
					if (tb.getColumn(i).isNumber()) {
						obj = (res.getDouble(tb.getColumn(i).getColumnName()));
					} else {
						obj = (res.getString(tb.getColumn(i).getColumnName()));
					}
					app.add(obj);
				}
				temp.add(app);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pieno = res.next();
		}
		return temp;
	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti
	 * ordinati di column e popolare un insieme da restituire.
	 * 
	 * @param table
	 *            Nome della tabella.
	 * @param column
	 *            Nome della colonna nella tabella.
	 * @return Insieme di valori distinti ordinati in modalità ascendente che
	 *         l’attributo identificato da nome column assume nella tabella
	 *         identificata dal nome table.
	 * @throws SQLException
	 *             In presenza di errori nella esecuzione della query.
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<Object> temp = new TreeSet<Object>();
		Connection conn = db.getConnection();
		Statement s = conn.createStatement();
		String query = "SELECT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName();
		ResultSet res = s.executeQuery(query);
		while (res.next()) {
			if (column.isNumber()) {
				temp.add(res.getDouble(column.getColumnName()));
			} else {
				temp.add(res.getString(column.getColumnName()));
			}
		}
		return temp;
	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre il valore aggregato
	 * (valore minimo o valore massimo) cercato nella colonna di nome column della
	 * tabella di nome table.
	 * 
	 * @param table
	 *            Nome di tabella.
	 * @param column
	 *            Nome della colonna.
	 * @param aggregate
	 *            Operatore SQL di aggregazione (min,max).
	 * @return Aggregato cercato.
	 * @throws SQLException
	 *             In presenza di errori nella esecuzione della query.
	 * @throws NoValueException
	 *             Se il resultset è vuoto o il valore calcolato è pari a null.
	 */
	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
			throws SQLException, NoValueException {
		Double temp = null;
		try {
			db.initConnection();
			Connection conn = db.getConnection();
			conn.isValid(10);
			Statement s = conn.createStatement();
			String query = "SELECT " + aggregate.toString() + "(" + column.getColumnName() + ")as "
					+ column.getColumnName() + " FROM " + table;
			ResultSet res = s.executeQuery(query);
			if (res.next()) {
				temp = res.getDouble(column.getColumnName());
			} else {
				throw new NoValueException();
			}
		} catch (DatabaseConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return temp;
	}
}
