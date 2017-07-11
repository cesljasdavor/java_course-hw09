package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Razred predstavlja kompleksni polinom. Razred je nepromijenjiv. Format ovog
 * kompleksnog broja je umnožak suma, odnosno:
 * <p>
 * <i>(z - z0) *(z-z1)*...*(z-zn-1)*(z-zn) </i>
 * <p>
 * Razred nudi jedan konstruktor {@link #ComplexRootedPolynomial(Complex...)}
 * koji prihvaća varijablan broj kompleksnih korijena, koji su primjerci razreda
 * {@link Complex}. Popis metoda dan je u nastavku:
 * <ul>
 * <li>{@link #apply(Complex)}</li>
 * <li>{@link #toComplexPolynom()}</li>
 * <li>{@link #indexOfClosestRootFor(Complex, double)}</li>
 * <li>{@link #toString()} - nadjačana metoda</li>
 * </ul>
 * 
 * @see Complex
 * 
 * @author Davor Češljaš
 * 
 */
public class ComplexRootedPolynomial {

	/**
	 * Članska varijabla koja predstavlja korijene: <i>z0, z1, ... , zn-1,zn</i>
	 * <b>tim redom</b>
	 */
	private Complex[] roots;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor prima
	 * varijabilni broj kompleksnih korijena, koji su primjerci razreda
	 * {@link Complex} te ih interno pohranjuje.
	 *
	 * @param roots
	 *            varijabilni broj kompleksnih korijena, koji su primjerci
	 *            razreda {@link Complex}
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		if (roots == null) {
			throw new IllegalArgumentException("Korijeni polinoma ne smiju biti null");
		}

		this.roots = roots;
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
		Optional<Complex> result = Arrays.stream(roots).map(root -> z.sub(root)).reduce((z1, z2) -> z1.multiply(z2));
		if (!result.isPresent()) {
			throw new IllegalArgumentException("Ne mogu dobiti rezultat za " + z);
		}
		return result.get();
	}

	/**
	 * Metoda koja dohvaća poziciju u polju korijena od onog korijena koji je
	 * najbliže točki u kompleksnoj ravnini <b>z</b>, ako je udaljenost od
	 * najmanjeg korijena do točke <b>z</b> manja od predanog argumenta
	 * <b>treshold</b>. Ako to nije slučaj metoda vraća <b>-1</b>
	 *
	 * @param z
	 *            točka u kompleksnoj ravnini do koje se računa udaljenost
	 * @param treshold
	 *            granica koja definira kolika najveća udaljenost smije biti od
	 *            korijena do <b>z</b>
	 * @return pozicija u polju korijena do korijena koji je u kompleksnoj
	 *         ravnini najmanje udaljen od <b>z</b> ili -1 ukoliko je ta
	 *         udaljenost veća od <b>treshold</b>
	 * 
	 * @see Complex#distance(Complex)
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int minIndex = 0;
		double minDistance = roots[0].distance(z);
		for (int i = 1; i < roots.length; i++) {
			double distance = roots[i].distance(z);
			if (distance < minDistance) {
				minDistance = distance;
				minIndex = i;
			}
		}

		return minDistance <= treshold ? minIndex : -1;
	}

	/**
	 * Metoda koja vrši konverziju iz primjeraka ovog razreda u primjerke
	 * razreda {@link ComplexPolynomial}. Prilikom te konverzije stvara se novi
	 * primjerak razreda {@link ComplexPolynomial} i pri tome se niti na koji
	 * način ne mijenja ovaj primjerak razreda {@link ComplexRootedPolynomial}
	 *
	 * @return primjerak razreda {@link ComplexPolynomial} nastao konverzijom iz
	 *         ovog primjerka razreda {@link ComplexRootedPolynomial}
	 */
	public ComplexPolynomial toComplexPolynom() {
		// oblik je 1*z1 +(- rooti) * z0 od svakog od ovih
		ComplexPolynomial result = createComplexPolynomialForRoot(roots[0]);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(createComplexPolynomialForRoot(roots[i]));
		}
		return result;
	}

	/**
	 * Pomoćna metoda koja od korijena ovog primjerka razreda
	 * {@link ComplexRootedPolynomial} stvara primjerak razreda
	 * {@link ComplexPolynomial}. Metoda se koristi prilikom konverzije metodom
	 * {@link #toComplexPolynom()}. 
	 *
	 * @param root
	 *            korijen koji je potrebno pretvoriti u {@link ComplexPolynomial}
	 * @return primjerak razreda {@link ComplexPolynomial} nastao iz korijena <b>root</b>
	 */
	private ComplexPolynomial createComplexPolynomialForRoot(Complex root) {
		return new ComplexPolynomial(root.negate(), Complex.ONE);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("*", "f(z) = ", "");

		for (Complex complex : roots) {
			sj.add(String.format("(z - %s)", complex));
		}

		return sj.toString();
	}
}
