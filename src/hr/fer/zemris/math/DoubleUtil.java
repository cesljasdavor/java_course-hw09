package hr.fer.zemris.math;

import java.util.Comparator;

/**
 * Pomoćni razreda koji predstavlja biblioteku metoda i primjeraka razreda koji
 * implementiraju sučelje {@link Comparator}. Biblioteka je izgrađena zbog
 * nesavršenosti IEEE-754 standarda zbog koje je otežano uspoređivati primjerke
 * razreda {@link Double}, ako i primitivni tip <b>double</b>
 * 
 * @see Double
 * @see Comparator
 * 
 * @author Davor Češljaš
 */
public class DoubleUtil {

	/**
	 * Konstanta koja predstavlja koeficijent od kojeg razlika dvije
	 * <b>double</b> vrijednosti mora biti manja kako bi se predpostavilo da su
	 * te dvije vrijednosti jednake. Koeficijent se koristi i unutar
	 * {@link #DOUBLE_COMPARATOR}
	 */
	private static final double DIFF = 1E-5;

	/**
	 * Konstanta koja predstavlja primjerak razreda koji implementira sučelje
	 * {@link Comparator}, a koji se koristi za usporedbu dva primjerak razreda
	 * {@link Double}
	 * 
	 * @see Comparator
	 * @see Double
	 */
	public static final Comparator<Double> DOUBLE_COMPARATOR = (o1, o2) -> {
		if (o1 - o2 > DIFF)
			return 1;
		if (doubleEquals(o1, o2))
			return 0;
		return -1;
	};

	/**
	 * Metoda koja se koristi kako bi se ispitalo jesu li vrijednosti
	 * <b>firstNumber</b> i <b>secondNumber</b> jednake. Za usporedbu se koristi
	 * faktor {@value #DIFF}
	 *
	 * @param firstNumber
	 *            prvi broj koji se uspoređuje
	 * @param secondNumber
	 *            drugi broj koji se uspoređuje
	 * @return <b>true</b> ako su prema opisu ove metode vrijednosti
	 *         <b>firstNumber</b> i <b>secondNumber</b> jednake, <b>false</b>
	 *         inače
	 */
	public static boolean doubleEquals(double firstNumber, double secondNumber) {
		return Math.abs(firstNumber - secondNumber) < DIFF;
	}

}
