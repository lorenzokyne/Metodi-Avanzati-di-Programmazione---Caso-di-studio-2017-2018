package data;

/**
 * Classe per modellare una eccezione controllata da considerare qualora il
 * numero k di cluster inserito da tastiera è maggiore rispetto al numero di
 * centroidi generabili dall'insieme di transazioni.
 * 
 * @author Marco
 *
 */
public class OutOfRangeSampleSize extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Metodo che crea ed espelle un oggetto istanza di OutRangeSampleSize se
	 * k{@literal <}=0 or k{@literal >}distinctTuples. Altrimenti si procede con
	 * implementazione definite in precedenza.
	 */
	public void notValidNumber() {
		System.out.println("Valore non valido");
	}
}
