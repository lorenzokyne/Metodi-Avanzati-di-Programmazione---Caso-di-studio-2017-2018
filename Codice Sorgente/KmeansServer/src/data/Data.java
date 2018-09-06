package data;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import database.DbAccess;
import database.EmptySetException;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;

/**
 * Classe che modella l'insieme di tuple acquisite dal database.
 * 
 * @author Lorenzo
 *
 */
public class Data {

	/**
	 * Lista di Example (transizioni) letti dal database.
	 */
	private List<database.Example> data;
	/**
	 * Intero che indica il numero di tuple presenti nella tabella.
	 */
	private int numberOfExamples;
	/**
	 * Lista di attribute che descrivono la tabella.
	 */
	private List<Attribute> explanatorySet;

	/**
	 * Costruttore che si occupa di caricare i dati di addestramento da una tabella
	 * della base di dati.
	 * 
	 * @param table
	 *            Indica il nome della tabella da cui caricare i dati.
	 * @throws SQLException
	 *             Si verifica quando c'è un errore nel caricamento dei dati dal
	 *             database.
	 * @throws EmptySetException
	 *             Nel caso il set acquisito sia vuoto.
	 * @throws NoValueException
	 *             Nel caso un valore nel result set sia assente.
	 */
	public Data(String table) throws SQLException, EmptySetException, NoValueException {

		explanatorySet = new LinkedList<Attribute>();

		TreeSet<String> outLookValues = new TreeSet<String>();
		TreeSet<String> temperatureValues = new TreeSet<String>();
		TreeSet<String> humidityValues = new TreeSet<String>();
		TreeSet<String> windValues = new TreeSet<String>();
		TreeSet<String> playTennisValues = new TreeSet<String>();
		windValues.add("weak");
		windValues.add("strong");
		playTennisValues.add("no");
		playTennisValues.add("yes");
		humidityValues.add("normal");
		humidityValues.add("high");
		temperatureValues.add("cool");
		temperatureValues.add("mild");
		temperatureValues.add("hot");
		outLookValues.add("overcast");
		outLookValues.add("rain");
		outLookValues.add("sunny");
		/**
		 * Attributo di tipo DbAccess per la connessione al database.
		 */
		DbAccess db = new DbAccess();
		TableSchema tb = new TableSchema(db, table);
		TableData t = new TableData(db);
		for (int i = 0; i < tb.getNumberOfAttributes(); i++) {
			String nomecol = tb.getColumn(i).getColumnName().toLowerCase();
			if (tb.getColumn(i).isNumber()) {
				QUERY_TYPE min, max;
				min = QUERY_TYPE.MIN;
				max = QUERY_TYPE.MAX;
				double valmin = (double) t.getAggregateColumnValue(table, tb.getColumn(i), min);
				double valmax = (double) t.getAggregateColumnValue(table, tb.getColumn(i), max);
				explanatorySet.add(new ContinuousAttribute(nomecol, i, valmin, valmax));
			} else {
				explanatorySet.add(new DiscreteAttribute<String>(nomecol, i,
						(TreeSet) t.getDistinctColumnValues(table, tb.getColumn(i))));
			}
		}
		data = t.getDistinctTransazioni(table);
		numberOfExamples = data.size();
	}

	/**
	 * Metodo che restituisce il numero di tuple presenti.
	 * 
	 * @return Intero che indica numero di tuple presenti in data.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	/**
	 * Restituisce il numero di colonne della tabella.
	 * 
	 * @return Intero che indica numero di colonne presenti in data.
	 */
	public int getNumberOfAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Metodo per leggere un valore della tabella nella posizione specificata.
	 * 
	 * @param exampleIndex
	 *            Indice di riga.
	 * @param attributeIndex
	 *            Indice di colonna.
	 * @return Oggetto rappresentante l'attributo presente nella tabella nella
	 *         posizione specificata.
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}

	/**
	 * Metodo che legge il valore dell'Attribute in posizione specificata.
	 * 
	 * @param index
	 *            Indica l'indice di cui si vuole conoscere l'Attribute.
	 * @return Attribute presente in posizione specificata nella tabella.
	 */
	public Attribute getAttribute(int index) {
		return explanatorySet.get(index);
	}

	/**
	 * Metodo che acquisice lo schema degli attributi.
	 * 
	 * @return Lista di Attribute costituenti la tabella.
	 */
	public List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}

	/**
	 * Metodo che genera un array di k interi rappresentanti gli indici di riga in
	 * data per le tuple inizialmente scelte come centroidi.
	 * 
	 * @param k
	 *            Numero di cluster da generare.
	 * @return Array di indici di riga in data.
	 * @throws OutOfRangeSampleSize
	 *             Se il numero inserito eccede il numero massimo di cluster
	 *             generabili.
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		if (k <= 0 || k > getNumberOfExamples()) {
			throw new OutOfRangeSampleSize();
		} else {
			int centroidIndexes[] = new int[k];
			// choose k random different centroids in data.
			Random rand = new Random();
			rand.setSeed(System.currentTimeMillis());
			for (int i = 0; i < k; i++) {
				boolean found = false;
				int c;
				do {
					found = false;
					c = rand.nextInt(getNumberOfExamples());
					// verify that centroid[c] is not equal to a centroide
					// already stored in CentroidIndexes
					for (int j = 0; j < i; j++)
						if (compare(centroidIndexes[j], c)) {
							found = true;
							break;
						}
				} while (found);
				centroidIndexes[i] = c;
			}
			return centroidIndexes;
		}
	}

	/**
	 * Metodo che confronta i valori della tabella negli indici specificati.
	 * 
	 * @param i
	 *            Indice corrispondente al primo valore.
	 * @param j
	 *            Indice corrispondente al secondo valore.
	 * @return Restituisce vero se i valori in posizioni specificate nella tabella
	 *         sono uguali, falso altrimenti.
	 */
	private boolean compare(int i, int j) {
		for (int k = 0; k < getNumberOfAttributes(); k++) {
			if (!getAttributeValue(i, k).equals(getAttributeValue(j, k))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Metodo per la lettura della riga specificata dalla tabella.
	 * 
	 * @param index
	 *            Indice di riga della tabella.
	 * @return Restituisce la tupla corrispondente alla posizione specificata.
	 */
	public Tuple getItemSet(int index) {
		Tuple temp = new Tuple(getNumberOfAttributes());
		for (int i = 0; i < getNumberOfAttributes(); i++) {
			Object item = explanatorySet.get(i);
			if (item.getClass().getName().equals("data.ContinuousAttribute")) {
				temp.add(new ContinuousItem((ContinuousAttribute) explanatorySet.get(i),
						(Double) data.get(index).get(i)), i);
			} else {
				temp.add(new DiscreteItem((DiscreteAttribute) explanatorySet.get(i), (String) data.get(index).get(i)),
						i);
			}
		}
		return temp;
	}

	/**
	 * Usa l'RTTI per determinare se attribute riferisce una istanza di
	 * ContinuousAttribute o di DiscreteAttribute.
	 * 
	 * @param idList
	 *            Set di indici corrispondenti alle righe della matrice che
	 *            costituiscono il cluster.
	 * @param attribute
	 *            Attribute su cui vogliamo effettuare il calcolo.
	 * @return Oggetto corrispondente al valore prototipo calcolato.
	 */
	public Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if (attribute.getClass().getName().equals("data.ContinuousAttribute")) {
			return computePrototype(idList, (ContinuousAttribute) attribute);
		} else if (attribute.getClass().getName().equals("data.DiscreteAttribute")) {
			return computePrototype(idList, (DiscreteAttribute<String>) attribute);
		}
		return null;
	}

	/**
	 * Determina il valore che occorre più frequentemente per attribute nel
	 * sottoinsieme di dati individuato da idList nel caso attribute sia un
	 * attributo discreto.
	 * 
	 * @param idList
	 *            Set di indici corrispondenti alle righe della matrice che
	 *            costituiscono il cluster.
	 * @param attribute
	 *            Attribute discreto su cui vogliamo effettuare il calcolo.
	 * @return Restitusce una stringa corrispondente al valore che occorre più
	 *         frequentemente.
	 */
	public String computePrototype(Set<Integer> idList, DiscreteAttribute<String> attribute) {
		String temp = "";
		double max = 0;
		double app = 0;
		for (Object s : attribute) {
			app = attribute.frequency(this, idList, (String) s);
			if (app > max) {
				max = app;
				temp = (String) s;
			}
		}
		return temp;
	}

	/**
	 * Determina il valore che occorre più frequentemente per attribute nel
	 * sottoinsieme di dati individuato da idList nel caso attribute sia un
	 * attributo continuo.
	 * 
	 * @param idList
	 *            Set di indici corrispondenti alle righe della matrice che
	 *            costituiscono il cluster.
	 * @param attribute
	 *            Attribute continuo su cui vogliamo effettuare il calcolo.
	 * @return Restitusce una stringa corrispondente al valore che occorre più
	 *         frequentemente.
	 */
	public Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double somma = 0.0;
		int nValues = 0;
		Iterator<Integer> i = idList.iterator();
		while (i.hasNext()) {
			somma = somma + (double) data.get(i.next()).get(attribute.getIndex());
			nValues++;
		}
		return somma / nValues;
	}

	/**
	 * Stampa la tabella.
	 */
	public String toString() {
		String appoggio = "";
		for (int i = 0; i < getNumberOfAttributes(); i++) {
			appoggio += getAttribute(i) + ",";
		}
		appoggio = appoggio + "\n";
		for (int i = 0; i < getNumberOfExamples(); i++) {
			appoggio = appoggio + (i + 1) + ":";
			for (int j = 0; j < getNumberOfAttributes(); j++) {
				appoggio = appoggio + getAttributeValue(i, j) + ",";
			}
			appoggio = appoggio + "\n";
		}
		return appoggio;
	}
}