package data;

/**
 * Classe DiscreteItem che estende la classe Item e rappresenta una coppia
 * (Attributo discreto - valore discreto).
 * 
 * @author Fernando.
 *
 */
public class DiscreteItem extends Item {
	/**
	 * Metodo che invoca il costruttore della classe madre.
	 * 
	 * @param attribute
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe attribute della classe Item.
	 * @param value
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe value della classe Item.
	 */
	public DiscreteItem(DiscreteAttribute<String> attribute, String value) {
		super(attribute, value);
	}

	/**
	 * Metodo che controlla se il value di Item è uguale a Object a.
	 * 
	 * @param a
	 *            oggetto sul quale effettuare il controllo.
	 * @return 0 se value della classe Item è uguale alla variabile a ,1 altrimenti.
	 */
	public double distance(Object a) {
		if (getValue().equals(((DiscreteItem) a).getValue())) {
			return 0;
		} else {
			return 1;
		}
	}
}
