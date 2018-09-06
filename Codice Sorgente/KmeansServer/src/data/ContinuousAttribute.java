package data;

/**
 * Classe che estende la classe Attribute e modella un attributo continuo
 * (numerico).
 * 
 * @author Fernando.
 *
 */
public class ContinuousAttribute extends Attribute {
	/**
	 * Rappresenta l'estremo minimo dell'intervallo di valori (dominio) che
	 * l'attributo può reamente assumere.
	 */
	private double max;
	/**
	 * Rappresenta l'estremo massimo dell'intervallo di valori (dominio) che
	 * l'attributo può reamente assumere.
	 */
	private double min;

	/**
	 * Metodo che invoca il costruttore della classe madre e inizializza i membri
	 * aggiunti per estensione.
	 * 
	 * @param name
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe name della classe Attribute.
	 * @param index
	 *            valore da passare come parametro a super per assegnarlo alla
	 *            variabile di classe index della classe Attribute.
	 * @param min
	 *            valore da assegnare alla variabile di classe min.
	 * @param max
	 *            valore da assegnare alla variabile di classe max.
	 */
	ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min = min;
		this.max = max;
	}

	/**
	 * Metodo che calcola e restituisce il valore normalizzato del parametro passato
	 * in input.
	 * 
	 * @param v
	 *            valore dell'attributo da normalizzare.
	 * @return valore normalizzato.
	 */
	public double getScaledValue(double v) {
		double a = ((v - min) / (max - min));
		return a;
	}

	/**
	 * Metodo che stampa il valore di min e max.
	 */
	public void getminmax() {
		System.out.println("Min: " + min + "\nMax: " + max);
	}
}
