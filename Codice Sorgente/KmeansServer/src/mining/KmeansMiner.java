package mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;

/**
 * Classe che include l'implementazione dell’algoritmo kmeans.
 * 
 * @author Fernando.
 *
 */
public class KmeansMiner implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Attributo rappresentante l'insieme di cluster.
	 */
	private ClusterSet C;

	/**
	 * Metodo che inizializza C
	 * 
	 * @param k
	 *            numero di cluster da generare.
	 * @throws OutOfRangeSampleSize
	 *             Se il numero inserito eccede il numero massimo di cluster
	 *             generabili.
	 */
	public KmeansMiner(int k) throws OutOfRangeSampleSize {
		C = new ClusterSet(k);
	}

	/**
	 * Metodo che carica la variabile di classe C da file.
	 * 
	 * @param fileName
	 *            nome del file da cui caricare C
	 * @throws FileNotFoundException
	 *             gestiamo la possibilità di un filename che non esiste.
	 * @throws IOException
	 *             In caso di problemi con lo scambio dati con il server.
	 * @throws ClassNotFoundException
	 *             In caso si riceva un oggetto sconosciuto dal server.
	 */
	public KmeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream inFile = new FileInputStream(fileName);
		ObjectInputStream inStream = new ObjectInputStream(inFile);
		C = (ClusterSet) inStream.readObject();
		inStream.close();
	}

	/**
	 * Metodo per la restituzione della variabile di classe C.
	 * 
	 * @return variabile di classe C.
	 */
	public ClusterSet getC() {
		return C;
	}

	/**
	 * Metodo che serializza nel file C
	 * 
	 * @param fileName
	 *            nome del file i cui salvare C.
	 * @throws FileNotFoundException
	 *             gestiamo la possibilità che il nome del file non sia valido.
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream outFile = new FileOutputStream(fileName);
		ObjectOutputStream outStream = new ObjectOutputStream(outFile);
		outStream.writeObject(C);
		outStream.close();
	}

	/**
	 * Esegue l’algoritmo k-means eseguendo i passi dello pseudo-codice: 1. Scelta
	 * casuale di centroidi per k clusters 2. Assegnazione di ciascuna riga della
	 * matrice in data al cluster avente centroide più vicino all'esempio. 3.
	 * Calcolo dei nuovi centroidi per ciascun cluster 4. Ripete i passi 2 e 3.
	 * finché due iterazioni consecuitive non restituiscono.
	 * 
	 * @param data
	 *            riferimento ad un oggetto di tipo Data.
	 * @return numero di iterazioni eseguite.
	 * @throws OutOfRangeSampleSize
	 *             gestiamo la possibilità che il valore di centroidi che si
	 *             vogliono generare non è valido.
	 */
	public int kMeans(Data data) throws OutOfRangeSampleSize {
		int numberOfIterations = 0;
		// STEP 1
		C.initializeCentroids(data);
		boolean changedCluster = false;
		do {
			numberOfIterations++;
			// STEP 2
			changedCluster = false;
			for (int i = 0; i < data.getNumberOfExamples(); i++) {
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster = C.currentCluster(i);
				boolean currentChange = nearestCluster.addData(i);
				if (currentChange) {
					changedCluster = true;
				}
				// rimuovo la tupla dal vecchio cluster
				if (currentChange && oldCluster != null)
					// il nodo va rimosso dal suo vecchio cluster
					oldCluster.removeTuple(i);
			}
			// STEP 3
			C.updateCentroids(data);
		} while (changedCluster);
		return numberOfIterations;
	}

	/**
	 * Metodo che restituisce una Stringa contenente lo stato di C.
	 */
	public String toString() {
		return C.toString();
	}
}
