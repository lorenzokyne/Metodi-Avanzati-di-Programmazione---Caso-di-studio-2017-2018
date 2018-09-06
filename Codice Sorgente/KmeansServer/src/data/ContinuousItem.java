package data;

/**
 * Classe che estende la classe Item e modella una coppia (Attributo continuo -
 * valore numerico).
 * 
 * @author Marco
 *
 */
public class ContinuousItem extends Item {
	/**
	 * Metodo che richiama il costruttore della super classe.
	 * 
	 * @param attribute
	 *            valore da assegnare alla variabile di classe attribute.
	 * @param value
	 *            valore da assegnare alla variabile di classe value.
	 */
	public ContinuousItem(Attribute attribute, Double value) {
		super(attribute, value);
	}

	@Override
	/**
	 * Determina la distanza (in valore assoluto) tra il valore scalato memorizzato
	 * nello item corrente e quello scalato associato al parametro a.
	 * 
	 * @param a
	 *            oggetto che rappresenta il valore scalato da cui vogliamo
	 *            calcolare la distanza.
	 */
	double distance(Object a) {
		ContinuousAttribute app = ((ContinuousAttribute) getAttribute());
		double temp = Math.abs(
				app.getScaledValue((double) getValue()) - app.getScaledValue((double) ((ContinuousItem) a).getValue()));
		return temp;
	}
}
