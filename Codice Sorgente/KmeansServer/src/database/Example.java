package database;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe che modella una transazione letta dalla base di dati.
 * @author Marco
 *
 */
public class Example implements Comparable<Example> {
	/**
	 * Lista di oggetti corrispondente agli attributi della transazione letta.
	 */
	private List<Object> example = new ArrayList<Object>();
/**
 * Metodo che aggiunge un oggetto alla transazione corrente.
 * @param o oggetto da aggiungere alla transazione corrente.
 */
	public void add(Object o) {
		example.add(o);
	}
	/**
	 * Metodo che legge un oggetto della transazione corrente in posizione i.
	 * @param i indice della lista.
	 * @return oggetto in posizione i.
	 */
	public Object get(int i) {
		return example.get(i);
	}
	/**
	 * Metodo che confronta la transazione corrente con quella passata come parametro.
	 * @param ex transazione passata come parametro.
	 */
	public int compareTo(Example ex) {
		int i = 0;
		for (Object o : ex.example) {
			if (!o.equals(this.example.get(i)))
				return ((Comparable) o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
/**
 * Metodo che formatta la stampa personalizzata.
 */
	public String toString() {
		String str = "";
		for (Object o : example)
			str += o.toString() + " ";
		return str;
	}

}