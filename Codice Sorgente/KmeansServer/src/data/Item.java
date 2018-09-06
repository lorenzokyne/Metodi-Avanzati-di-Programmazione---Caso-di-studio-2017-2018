package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe astratta Item che modella un generico item.
 */

public abstract class Item implements Serializable {
	/**
	 * attributo coinvolto nell'item.
	 */
	private Attribute attribute;
	/**
	 * valore assegnato all'attributo.
	 */
	private Object value;

	/**
	 * Metodo che inizializza i valori di classe.
	 * 
	 * @param attribute
	 *            valore da assegnare alla variabile di classe attribute.
	 * @param value
	 *            valore da assegnare alla variabile di classe value.
	 */
	public Item(Attribute attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * Metodo che restituisce attribute.
	 * 
	 * @return variabile di classe attribute.
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Metodo che restituisce value.
	 * 
	 * @return variabile di classe value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Metodo che sovrascrive metodo ereditato dalla superclasse e restuisce la
	 * stringa rappresentante lo stato dell'oggetto.
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * L’implementazione sarà diversa per item discreto e item continuo.
	 * 
	 * @param a
	 *            oggetto da cui calcolare la distanza.
	 * @return double che rappresenta la distanza effettiva.
	 */
	abstract double distance(Object a);

	/**
	 * Metodo che modifica il membro value, assegnandogli il valore restituito da
	 * data.computePrototype(clusteredData,attribute) che definiremo in seguito.
	 * 
	 * @param data
	 *            riferimento ad un oggetto della classe Data.
	 * @param clusteredData
	 *            insieme di indici in data che formano il cluster.
	 */
	public void update(Data data, Set<Integer> clusteredData) {
		value = data.computePrototype(clusteredData, attribute);
	}
}
