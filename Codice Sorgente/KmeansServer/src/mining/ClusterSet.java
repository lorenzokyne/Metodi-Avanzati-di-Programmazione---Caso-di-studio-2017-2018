package mining;

import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

/**
 * Classe che rappresenta un insieme di Cluster.
 * 
 * @author Fernando.
 *
 */
public class ClusterSet implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Vettore di Cluster.
	 */
	private Cluster C[];
	/**
	 * posizione valida per la memorizzazione di un nuovo cluster in C.
	 */
	private int i = 0;

	/**
	 * Metodo che crea l'oggetto di tipo C.
	 * 
	 * @param k
	 *            Numero di cluster da generare.
	 * @throws OutOfRangeSampleSize Se il numero inserito eccede il numero massimo di cluster generabili.
	 */
	public ClusterSet(int k) throws OutOfRangeSampleSize {
		if (k > 0)
			C = new Cluster[k];
		else
			throw new OutOfRangeSampleSize();
	}

	/**
	 * Metodo che inserisce un nuovo Cluster in C.
	 * 
	 * @param c
	 *            Cluster da inserire.
	 */
	public void add(Cluster c) {
		C[i] = c;
		i++;
	}

	/**
	 * Metodo che restituisce un Cluster corrispondente ad i.
	 * 
	 * @param i
	 *            indice della posizione di C corrispondente ad un Cluster.
	 * @return il Cluster in posizione i di C (vettore di Cluster).
	 */
	public Cluster get(int i) {
		return C[i];
	}

	/**
	 * Metodo che sceglie i centroidi, crea un cluster per ogni centroide e lo
	 * memorizza in C.
	 * 
	 * @param data
	 *            riferimento ad un oggetto di tipo Data.
	 * @throws OutOfRangeSampleSize
	 *             quando il valore di centroidi che si vogliono generare non è
	 *             valido.
	 */
	public void initializeCentroids(Data data) throws OutOfRangeSampleSize {
		int centroidIndexes[] = data.sampling(C.length);
		for (int i = 0; i < centroidIndexes.length; i++) {
			Tuple centroidI = data.getItemSet(centroidIndexes[i]);
			add(new Cluster(centroidI));
		}
	}

	/**
	 * Metodo che calcola la distanza tra la tupla riferita da tuple ed il centroide
	 * di ciascun cluster in C.
	 * 
	 * @param tuple
	 *            veriabile da cui calcolare la distanza.
	 * @return restituisce il cluster più vicino.
	 */
	public Cluster nearestCluster(Tuple tuple) {
		double min = get(0).getCentroid().getDistance(tuple);
		Cluster app = get(0);
		for (int j = 1; j < i; j++) {
			double temp = get(j).getCentroid().getDistance(tuple);
			if (temp <= min) {
				min = temp;
				app = get(j);
			}
		}
		return app;
	}

	/**
	 * Metodo che cerca il Cluster contenente la tupla con indice id,se presente.
	 * 
	 * @param id
	 *            variabile che rappresenta l'indice in cui cercare.
	 * @return il Cluster contenente la tupla con indice id.
	 */
	public Cluster currentCluster(int id) {
		for (int i = 0; i < C.length; i++) {
			if (C[i].contain(id)) {
				return C[i];
			}
		}
		return null;
	}

	/**
	 * Metodo che calcola il nuovo centroide per ciascun cluster in C.
	 * 
	 * @param data
	 *            riferimento ad un oggetto di tipo Data.
	 */
	public void updateCentroids(Data data) {
		for (int i = 0; i < C.length; i++) {
			C[i].computeCentroid(data);
		}
	}

	/**
	 * Metodo che crea una stringa fatta da ciascun centroide.
	 * 
	 * @return restituisce una stringa descritta in precedenza.
	 */
	public String toString() {
		String temp = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null)
				temp += C[i].getCentroid() + "\n";
		}
		return temp;
	}

	/**
	 * Metodo che crea una stringa che descriva lo stato di ciascun cluster in C.
	 * 
	 * @param data
	 *            riferimento ad un oggetto di tipo Data.
	 * @return restituisce una stringa descritta in precedenza.
	 */
	public String toString(Data data) {
		String str = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null) {
				str += i + ":" + C[i].toString(data) + "\n";
			}
		}
		return str;
	}
}
