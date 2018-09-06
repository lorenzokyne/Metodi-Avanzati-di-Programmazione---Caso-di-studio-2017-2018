package data;

import java.io.Serializable;

/**
 * La classe Tuple rappresenta una tupla come sequenza di coppie
 * attributo-valore.
 * 
 * @author Marco
 *
 */
public class Tuple implements Serializable {
	/**
	 * Array di oggetti Item.
	 */
	private Item[] tuple;

	/**
	 * Metodo che costruisce l'oggetto riferito da tuple.
	 * 
	 * @param size
	 *            rappresenta il numero di item che costituirà la tupla.
	 */
	public Tuple(int size) {
		tuple = new Item[size];
	}

	/**
	 * Metodo che restituisce la lunghezza di tuple.
	 * 
	 * @return tuple.lenght.
	 */
	public int getLength() {
		return tuple.length;
	}

	/**
	 * Metodo che restituisce lo item in posizione i.
	 * 
	 * @param i
	 *            indice che indica la posizione.
	 * @return item in posizione i.
	 */
	public Item get(int i) {
		return tuple[i];
	}

	/**
	 * Metodo che memorizza c in tuple nella posizione i.
	 * 
	 * @param c
	 *            indica lo item da memorizzare.
	 * @param i
	 *            indice che indica la posizione.
	 */
	public void add(Item c, int i) {
		tuple[i] = c;
	}

	/**
	 * Metodo che determina la distanza tra la tupla riferita da obj e la tupla
	 * corrente (riferita da this). La distanza è ottenuta come la somma delle
	 * distanze tra gli item in posizioni eguali nelle due tuple.
	 * 
	 * @param obj
	 *            indica la tupla dal quale calcolare la distanza.
	 * @return temp indica la distanza.
	 */
	public double getDistance(Tuple obj) {
		double temp = 0;
		if (getLength() == obj.getLength()) {
			for (int i = 0; i < getLength(); i++) {
				temp = temp + (get(i).distance(obj.get(i)));
			}
		}
		return temp;
	}

	/**
	 * Metodo che restituisce la media delle distanze tra la tupla corrente e quelle
	 * ottenibili dalle righe della matrice in data aventi indice in clusteredData.
	 * 
	 * @param data
	 *            indica la tabella a cui far riferimento.
	 * @param clusteredData
	 *            array che contiene gli indici delle tuple da cui calcolare la
	 *            distanza.
	 * @return Media delle distanze tra la tupla corrente e quelle in clusteredData.
	 */
	public double avgDistance(Data data, Object clusteredData[]) {
		double p = 0.0, sumD = 0.0;
		for (int i = 0; i < clusteredData.length; i++) {
			double d = getDistance(data.getItemSet((Integer) clusteredData[i]));
			sumD += d;
		}
		p = sumD / clusteredData.length;
		return p;
	}

	/**
	 * Metodo che fornisce una stampa con una formattazione personalizzata.
	 */
	public String toString() {
		String temp = new String();
		for (int i = 0; i < tuple.length; i++) {
			temp += get(i).toString() + " ";
		}
		return temp;
	}
}
