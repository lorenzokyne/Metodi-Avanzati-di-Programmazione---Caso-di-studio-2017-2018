package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import data.Data;
import data.Tuple;

/**
 * Classe che modella un Cluster.
 * 
 * @author Fernando.
 *
 */
public class Cluster implements Serializable {
	/**
	 * Attributo che indica il centroide del cluster.
	 */
	private Tuple centroid;
	/**
	 * Set di indici della tabella di cui è formato il claster
	 */
	private Set<Integer> clusteredData;

	/**
	 * Metodo che inizializza i valori di classe.
	 * 
	 * @param centroid
	 *            valore da assegnare alla variabile di classe centroid.
	 */
	Cluster(Tuple centroid) {
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();
	}

	/**
	 * Funzione che restituisce centroid variabile di classe.
	 * 
	 * @return variabile di classe centroid.
	 */
	Tuple getCentroid() {
		return centroid;
	}

	/**
	 * Metodo che calcola un nuovo centroide attraverso motodo update.
	 * 
	 * @param data
	 *            riferimento ad un oggetto di tipo data.
	 */
	void computeCentroid(Data data) {
		for (int i = 0; i < centroid.getLength(); i++) {
			centroid.get(i).update(data, clusteredData);
		}

	}

	/**
	 * Metodo che aggiunge id a variabile di classe di Cluster.
	 * 
	 * @param id
	 *            indice in cui inserire.
	 * @return true se la tupla ha cambiato Cluster.
	 */
	boolean addData(int id) {
		return clusteredData.add(id);
	}

	/**
	 * Metodo che controlla se in clusteredData contiene l'indice id.
	 * 
	 * @param id
	 *            indice su cui effettuare il controllo.
	 * @return true se contiene id false altrimenti.
	 */
	// verifica se una transazione è clusterizzata nell'array corrente
	boolean contain(int id) {
		return clusteredData.contains(id);
	}

	/**
	 * Metodo che elimina id da clusteredData.
	 * 
	 * @param id
	 *            oggetto da eliminare.
	 */
	void removeTuple(int id) {
		clusteredData.remove(id);

	}

	/**
	 * Metodo che sovrascrive metodo ereditato dalla superclasse e restuisce la
	 * lista dei controidi.
	 */
	public String toString() {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i);
		str += ")";
		return str;

	}

	/**
	 * Metodo che sovrascrive metodo ereditato dalla superclasse e restuisce la
	 * stringa rappresentante lo stato dell'oggetto.
	 * 
	 * @param data
	 *            Indica la tabella di cui vogliamo visualizzare i cluster.
	 * @return Stringa contenente i centroidi della tabella data in input.
	 */
	public String toString(Data data) {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + " ";
		str += ")\nExamples:\n";
		Object array[] = clusteredData.toArray();
		for (int i = 0; i < array.length; i++) {
			str += "[";
			for (int j = 0; j < data.getNumberOfAttributes(); j++)
				str += data.getAttributeValue((int) array[i], j) + " ";
			str += "] dist=" + getCentroid().getDistance(data.getItemSet((int) array[i])) + "\n";

		}
		str += "\nAvgDistance=" + getCentroid().avgDistance(data, array);
		return str;
	}
}
