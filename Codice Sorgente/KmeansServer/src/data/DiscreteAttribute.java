package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe che estende la classe Attribute e rappresenta un attributo discreto
 * (categorico).
 * 
 * @author Fernando
 *
 * @param <T>
 *            prametro generico che rappresenta il tipo di un attributo
 *            categorico
 */
public class DiscreteAttribute<T> extends Attribute implements Iterable<String> {
	/**
	 * TreeSet di oggetti String uno per ciascun valore del dominio discreto
	 * memorizzati in modo ordinato.
	 */
	private TreeSet<String> values;

	/**
	 * Metodo che invoca il costruttore della classe madre e inizializza il membro
	 * values con il parametro in input.
	 * 
	 * @param name
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe name della classe Attribute.
	 * @param index
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe index della classe Attribute.
	 * @param values
	 *            valore da assegnare alla variabile di classe values.
	 */
	public DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Metodo che restituisce la dimensione di values.
	 * 
	 * @return intero che rappresenta la grandezza di values.
	 */
	public int GetNumberOfDistinctValues() {
		return values.size();
	}

	/**
	 * Metodo che restituisce l'iteratore di values.
	 */
	public Iterator<String> iterator() {
		Iterator<String> a = values.iterator();
		return a;
	}

	/**
	 * Metodo che controlla se l' v appartiene al TreeSet values.
	 * 
	 * @param v
	 *            stringa su cui effettuare il controllo di appartenenza.
	 * @return true se v appartiene a values false altrimenti.
	 */

	public boolean appartiene(String v) {
		if (values.contains(v))
			return true;
		return false;
	}

	/**
	 * Metodo che determina il numero di volte che il valore v compare in
	 * corrispondenza dell'attributo corrente (indice di colonna) negli esempi
	 * memorizzati in data e indicizzate (per riga) da idList.
	 * 
	 * @param data
	 *            riferimento ad un oggetto Data,
	 * @param idList
	 *            riferimento ad un oggetto ArraySet (che mantiene l'insieme degli
	 *            indici di riga di alcune tuple memorizzate in data).
	 * @param v
	 *            valore discreto sul quale effettuare il controllo di frequenza.
	 * @return numero di occorrenze del valore discreto (intero).
	 */
	public int frequency(Data data, Set<Integer> idList, String v) {
		int cont = 0;
		Object temp[] = idList.toArray();
		if (appartiene(v)) {
			for (int i = 0; i < temp.length; i++) {
				if (((String) (data.getAttributeValue((int) temp[i], getIndex()))).equals(v)) {
					cont++;
				}
			}
		}
		return cont;
	}
}
