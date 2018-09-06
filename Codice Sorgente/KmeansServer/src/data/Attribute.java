package data;

import java.io.Serializable;

/**
 * classe che modella la entità dell'attributo.
 * 
 * @author Fernando.
 *
 */
public abstract class Attribute implements Serializable {
	/**
	 * Nome simbolico dell'attributo.
	 */
	private String name;
	/**
	 * identificativo numerico dell'attributo.
	 */
	private int index;

	/**
	 * Metodo che inizializza i valori dei membri name,index.
	 * 
	 * @param name
	 *            Nome da assegnare alla variabile name.
	 * @param index
	 *            Index da assegnare alla variabile di classe index.
	 */
	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * Metodo che restituisce il valore di name.
	 * 
	 * @return volore di name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Metodo che restituisce il valore di index.
	 * 
	 * @return volore di index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Metodo che sovrascrive metodo ereditato dalla superclasse e restuisce la
	 * stringa rappresentante lo stato dell'oggetto.
	 */
	public String toString() {
		return name;
	}
}
