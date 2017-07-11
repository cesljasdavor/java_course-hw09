package hr.fer.zemris.math;

import static hr.fer.zemris.math.DoubleUtil.doubleEquals;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.math.complex.ComplexParser;

/**
 * Razred predstavlja jedan kompleksan broj u formatu <i>a + ib</i>. Razred je
 * nepromijenjiv. Razred nudi sljedeće operacije nad kompleksnim brojem, koje
 * vraćaju <b>novi</b> (ili više njih) kompleksan broj:
 * <ul>
 * <li>{@link #negate()}</li>
 * <li>{@link #add(Complex)}</li>
 * <li>{@link #sub(Complex)}</li>
 * <li>{@link #multiply(Complex)}</li>
 * <li>{@link #divide(Complex)}</li>
 * <li>{@link #power(int)}</li>
 * <li>{@link #root(int)}</li>
 * </ul>
 * 
 * Razred također sadrži 2 konstrukotra:
 * <ul>
 * <li>{@link #Complex()} - vraća 0+ i0</li>
 * <li>{@link #Complex(double, double)} - vraća predani a + ib</li>
 * </ul>
 * 
 * Kompleksan broj može se također parsirati i iz primjerka razreda
 * {@link String} ukoliko je foramt korektan. Tu funkcionalnost pruža metoda
 * {@link #parse(String)}
 * 
 * @author Davor Češljaš
 */
public class Complex {

	/** Konstantan primjerak ovog razreda koji predstavlja 0 + i0 */
	public static final Complex ZERO = new Complex(0, 0);

	/** Konstantan primjerak ovog razreda koji predstavlja 1 + i0 */
	public static final Complex ONE = new Complex(1, 0);

	/** Konstantan primjerak ovog razreda koji predstavlja -1 + i0 */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/** Konstantan primjerak ovog razreda koji predstavlja 0 + i1 */
	public static final Complex IM = new Complex(0, 1);

	/** Konstantan primjerak ovog razreda koji predstavlja 0 - i1 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/** Konstanta koja predstavlja potenciju 2 */
	private static final int SQUARE = 2;

	/** Članska varijabla koja predstavlja realan dio kompleksnog broja */
	private double re;

	/** Članska varijabla koja predstavlja imaginaran dio kompleksnog broja */
	private double im;

	/**
	 * Konstruktor koji se koristi za inicijalizaciju primjerka ovog razreda.
	 * Nakon inicijalizacije realni i imaginaran dio kompleksnog broja
	 * postavljeni su na 0
	 */
	public Complex() {
	}

	/**
	 * Konstruktor koji se koristi za inicijalizaciju primjerka ovog razreda.
	 * Nakon inicijalizacije realan dio kompleksnog broja postavljen je na
	 * <b>re</b>, a imaginarni dio na <b>im</b>
	 *
	 * @param re
	 *            realni dio kompleksnog broja
	 * @param im
	 *            imaginarni dio kompleksnog broja
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Metoda računa modul kompleksnog broja predstavljenog ovim primjerkom
	 * razreda. Metoda neće niti na koji način promijeniti primjerak ovog
	 * razreda
	 *
	 * @return modul kompleksnog broja
	 */
	public double module() {
		return Math.sqrt(Math.pow(re, SQUARE) + Math.pow(im, SQUARE));
	}

	/**
	 * Metoda vraća novi primjerak ovog razreda koji predstavlja umnožak ovog
	 * primjerka razreda {@link Complex} i primjerka razreda {@link Complex}
	 * <b>c</b>. Metoda niti na koji način neće promijeniti ovaj primjerak
	 * razreda.
	 *
	 * @param c
	 *            množitelj ovog primjerka razreda {@link Complex}
	 * @return novi kompleksni broj koji je umnožak ovog kompleksnog broja i
	 *         parametra <b>c</b>
	 */
	public Complex multiply(Complex c) {
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Metoda vraća novi primjerak ovog razreda koji predstavlja količnik ovog
	 * primjerka razreda {@link Complex} i primjerka razreda {@link Complex}
	 * <b>c</b>. Metoda niti na koji način neće promijeniti ovaj primjerak
	 * razreda.
	 *
	 * @param c
	 *            djelitelj ovog primjerka razreda {@link Complex}
	 * @return novi kompleksni broj koji je količnik ovog kompleksnog broja i
	 *         parametra <b>c</b>
	 * @throws ArithmeticException
	 *             ukoliko se pokuša dijeliti s nulom
	 *
	 */
	public Complex divide(Complex c) {
		if (c.equals(ZERO)) {
			throw new ArithmeticException("Dijeljenje s 0!");
		}
		double numeratorReal = re * c.re + im * c.im;
		double numeratorImaginary = im * c.re - re * c.im;
		double denominator = Math.pow(c.re, SQUARE) + Math.pow(c.im, SQUARE);

		return new Complex(numeratorReal / denominator, numeratorImaginary / denominator);

	}

	/**
	 * Metoda vraća novi primjerak ovog razreda koji predstavlja zbroj ovog
	 * primjerka razreda {@link Complex} i primjerka razreda {@link Complex}
	 * <b>c</b>. Metoda niti na koji način neće promijeniti ovaj primjerak
	 * razreda.
	 *
	 * @param c
	 *            pribrojnik ovog primjerka razreda {@link Complex}
	 * @return novi kompleksni broj koji je zbroj ovog kompleksnog broja i
	 *         parametra <b>c</b>
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}

	/**
	 * Metoda vraća novi primjerak ovog razreda koji predstavlja razliku ovog
	 * primjerka razreda {@link Complex} i primjerka razreda {@link Complex}
	 * <b>c</b>. Metoda niti na koji način neće promijeniti ovaj primjerak
	 * razreda.
	 *
	 * @param c
	 *            umanjitelj ovog primjerka razreda {@link Complex}
	 * @return novi kompleksni broj koji je razliku ovog kompleksnog broja i
	 *         parametra <b>c</b>
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}

	/**
	 * Metoda koja vraća novi primjerak ovog razreda koji predstavlja negirani
	 * trenutni primjerak. U prijevodu vraća broj <i>-re -i*im</i> Metoda niti
	 * na koji način neće promijeniti ovaj primjerak razreda.
	 * 
	 * @return novi primjerak ovog razreda koji predstavlja negirani trenutni
	 *         primjerak.
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Metoda vraća novi primjerak ovog razreda koji predstavlja potenciju
	 * <b>n</b> ovog primjerka razreda {@link Complex}. Metoda niti na koji
	 * način neće promijeniti ovaj primjerak razreda.
	 *
	 * @param n
	 *            potencija ovog primjerka razreda {@link Complex}
	 * @return novi kompleksni broj koji je potencija <b>n</b>-ta potencija ovog
	 *         primjerka razreda
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Eksponent ne može biti manji od nule");
		}

		double powMagnitude = Math.pow(module(), n);
		double angle = getAngle();
		return new Complex(powMagnitude * Math.cos(n * angle), powMagnitude * Math.sin(n * angle));

	}

	/**
	 * Metoda koja vraća udaljenost između ovog primjerka razreda
	 * {@link Complex} i predanog primjerka razreda {@link Complex} <b>c</b>.
	 * Metoda niti na koji način neće promijeniti ovaj primjerak razreda.
	 *
	 * @param c
	 *            primjerak razreda {@link Complex} do kojeg se računa
	 *            udaljenost
	 * @return udaljenost između ovog primjerka razreda {@link Complex} i
	 *         predanog primjerka razreda {@link Complex} <b>c</b>
	 */
	public double distance(Complex c) {
		return Math.sqrt(Math.pow(this.re - c.re, SQUARE) + Math.pow(this.im - c.im, SQUARE));
	}

	/**
	 * Korjenuje trenutni kompleksni broj sa <b>n</b> i rezultate vraća u
	 * {@link List} primjeraka razreda {@link Complex}. Korijen <b>n > 0</b>
	 *
	 * @param n
	 *            korijen kompleksnog broja
	 * 
	 * @return {@link List} svih <b>n</b> korijena ovog kompleksnog broja
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko je korijen <b>n <= 0 </b>
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Korijen ne može biti " + n);
		}

		List<Complex> roots = new ArrayList<>();
		double rootMagnitude = Math.pow(module(), 1.0 / n);
		for (int k = 0; k < n; k++) {
			roots.add(new Complex(rootMagnitude * getRootAngle(true, k, n), rootMagnitude * getRootAngle(false, k, n)));
		}

		return roots;
	}

	/**
	 * Pomoćna metoda koja računa i dohvaća kut kompleksnog broja iz pretvorbe u
	 * polarni oblika.
	 *
	 * @return kut kompleksnog broja
	 */
	private double getAngle() {
		double angle = Math.atan2(im, re);
		return angle < 0 ? (angle + 2 * Math.PI) : angle;
	}

	/**
	 * Pomoćna metoda za računanje kuta korijena. Koristi je {@link #root(int)}.
	 * Moguće je računati realni ili imaginarni dio
	 *
	 * @param real
	 *            <code><b>true</b></code> ako tražimo realan dio korijena,
	 *            <code><b>false</b></code>inače
	 * @param rootNumber
	 *            broj korijena
	 * @param root
	 *            koji se korijen računa (n u metodi {@link #root(int)})
	 * @return kut korijena
	 */
	private double getRootAngle(boolean real, int rootNumber, int root) {
		double argument = (getAngle() + 2 * rootNumber * Math.PI) / root;
		return real ? Math.cos(argument) : Math.sin(argument);
	}

	/**
	 * Parsira predani argument <b>s</b> u primjerak razreda {@link Complex}.
	 * Ukoliko nije moguće parsirati predani argument, metoda baca
	 * {@link IllegalArgumentException}. Primjeri ulaza:
	 * 
	 * <pre>
	 * "3.51", "-3.17", "-i2.71", "i", "1", "-2.71-i3.15"
	 * </pre>
	 * 
	 * @param s
	 *            {@link String} koji se pokušava parsirati
	 * @return primjerak razreda {@link Complex} koji je isparsiran
	 *
	 * @throws IllegalArgumentException
	 *             ako metoda {@link String#isEmpty()} nad <b>s</b> vrati
	 *             <code><b>true</b></code> ili ako broj nije u gore opisanom
	 *             formatu.
	 */
	public static Complex parse(String input) {
		return new ComplexParser(input).getResult();
	}

	/**
	 * Metoda dohvaća realan dio ovog kompleksnog broja
	 *
	 * @return realan dio ovog kompleksnog broja
	 */
	public double getRe() {
		return re;
	}

	/**
	 * Metoda dohvaća imaginaran dio ovog kompleksnog broja
	 *
	 * @return imaginaran dio ovog kompleksnog broja
	 */
	public double getIm() {
		return im;
	}

	@Override
	public String toString() {
		return String.format("(%f %s %fi)", re, im >= 0 ? "+" : "", im);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(im);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(re);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (!doubleEquals(this.im, other.im))
			return false;
		if (!doubleEquals(this.re, other.re))
			return false;
		return true;
	}

}
