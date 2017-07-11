package hr.fer.zemris.math;

import java.util.StringJoiner;

/**
 * Razred predstavlja kompleksni polinom. Razred je nepromijenjiv. Format ovog
 * kompleksnog broja je suma umnožaka, odnosno:
 * <p>
 * <i>zn * z^n + zn-1*z^n-1 + ... + z1 * z + z0</i>
 * <p>
 * Razred nudi jedan konstruktor {@link #ComplexPolynomial(Complex...)} koji
 * prihvaća varijablan broj kompleksnih faktora, koji su primjerci razreda
 * {@link Complex}. Popis metoda dan je u nastavku:
 * <ul>
 * <li>{@link #order()}</li>
 * <li>{@link #multiply(ComplexPolynomial)}</li>
 * <li>{@link #derive()}</li>
 * <li>{@link #apply(Complex)}</li>
 * <li>{@link #toString()} - nadjačana metoda</li>
 * </ul>
 * 
 * @see Complex
 * 
 * @author Davor Češljaš
 * 
 */
public class ComplexPolynomial {

	/**
	 * Članska varijabla koja predstavlja faktore: <i>z0, z1, ... , zn-1,zn</i>
	 * <b>tim redom</b>
	 */
	private Complex[] factors;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor prima
	 * varijabilni broj kompleksnih faktora, koji su primjerci razreda
	 * {@link Complex} te ih interno pohranjuje.
	 *
	 * @param factors
	 *            varijabilni broj kompleksnih faktora, koji su primjerci
	 *            razreda {@link Complex}
	 */
	public ComplexPolynomial(Complex... factors) {
		if (factors == null) {
			throw new IllegalArgumentException("Faktori ne smiju biti null!");
		}

		this.factors = factors;
	}

	/**
	 * Metoda koja vraća red polinoma. Red polinoma je najveća potencija koju
	 * sadrži ovaj kompleksni polinom
	 *
	 * @return red polinoma
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Metoda koja množi dva polinoma u obliku sume produkata. Metoda vraća novi
	 * primjerak ovog razreda. Metoda ne modificira predani niti ovaj primjerak
	 * ovog razreda.
	 * 
	 *
	 * @param primjerak
	 *            ovog razreda koji predstavlja množitelja ovog primjerka ovog
	 *            razreda
	 * 
	 * @return novi primjerak ovog razreda koji predstavlja umnožak ovog
	 *         primjerka i predanog primjerka <b>p</b>
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] multiplied = fillArrayWithZeros(this.factors.length + p.order());

		for (int i = 0; i < this.factors.length; i++) {
			for (int j = 0; j < p.factors.length; j++) {
				multiplied[i + j] = multiplied[i + j].add(this.factors[i].multiply(p.factors[j]));
			}
		}

		return new ComplexPolynomial(multiplied);
	}

	/**
	 * Pomoćna metoda koja stvara novo polje primjeraka razreda {@link Complex}
	 * te ga puni sa {@link Complex#ZERO}. Punjenje se radi zbog toga što je
	 * kompleksna nula neutralni element za zbroj kompleksnih brojeva
	 *
	 * @param size
	 *            potrebna veličina polja primjeraka razreda {@link Complex}
	 * @return polje primjeraka razreda {@link Complex} veličine <b>size</b>
	 *         napunjeno sa {@link Complex#ZERO}
	 */
	private Complex[] fillArrayWithZeros(int size) {
		Complex[] ones = new Complex[size];
		for (int i = 0; i < size; i++) {
			ones[i] = Complex.ZERO;
		}
		return ones;
	}

	/**
	 * Metoda koja derivira ovaj primjerak ovog razreda. Metoda vraća novi
	 * primjerak ovog razreda. Metoda niti na koji način ne modificira ovaj
	 * primjerak ovog razreda.
	 *
	 * @return novi primjerak ovog razreda koji predstavlja derivaciju ovog
	 *         primjerka
	 */
	public ComplexPolynomial derive() {
		Complex[] derived = new Complex[factors.length - 1];
		for (int i = 1; i < factors.length; i++) {
			derived[i - 1] = factors[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(derived);
	}

	/**
	 * Metoda koja u polinom uvrštava predanu vrijednost <b>z</b> te vraća
	 * rezultat koji je primjerak razreda {@link Complex} i koji predstavlja
	 * funkciju f(z) u točki <b>z</b>
	 *
	 * @param z
	 *            vrijednost z funkcije f(z) koju je potrebno uvrstiti
	 * @return rezultat koji je primjerak razreda {@link Complex} i koji
	 *         predstavlja f(z) u točki <b>z</b>
	 */
	public Complex apply(Complex z) {
		Complex result = factors[0];
		for (int i = 1; i < factors.length; i++) {
			result = result.add(factors[i].multiply(z.power(i)));
		}

		return result;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("+", "f(z) = ", "");
		for (int i = (factors.length - 1); i >= 0; i--) {
			sj.add(String.format("(%s * z^%d)", factors[i], i));
		}

		return sj.toString();
	}

}
