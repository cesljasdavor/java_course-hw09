package hr.fer.zemris.math;

import static hr.fer.zemris.math.DoubleUtil.doubleEquals;

/**
 * Razred koji predstavlja trodimenzionalni vektor. Primjerci ovog razreda su
 * nepromijenjivi. Razred nudi sljedeće metode koje prilikom izvršavanja
 * operacija ,ukoliko uopće vraćaju primjerak razreda {@link Vector3},stvaraju
 * novi primjerak razreda {@link Vector3} , ne mijenjajući time trenutni
 * primjerak:
 * <ul>
 * <li>{@link #norm()}</li>
 * <li>{@link #normalized()}</li>
 * <li>{@link #add(Vector3)}</li>
 * <li>{@link #sub(Vector3)}</li>
 * <li>{@link #dot(Vector3)}</li>
 * <li>{@link #cross(Vector3)}</li>
 * <li>{@link #scale(double)}</li>
 * <li>{@link #cosAngle(Vector3)}</li>
 * <li>{@link #toArray()}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class Vector3 {

	/** Konstanta koja predstavlja potenciju 2 */
	private static final int SQUARE = 2;

	/** Članska varijabla koja predstavlja x-koordinatu ovog vektora */
	private double x;

	/** Članska varijabla koja predstavlja y-koordinatu ovog vektora */
	private double y;

	/** Članska varijabla koja predstavlja z-koordinatu ovog vektora */
	private double z;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstrukotra x, y i z koordinate ovog primjerka razreda postavljaju se na
	 * predane parametre <b>x</b>, <b>y</b> i <b>z</b>
	 *
	 * @param x
	 *            vrijednost na koju se postavlja x-koordinata ovog vektora
	 * @param y
	 *            vrijednost na koju se postavlja y-koordinata ovog vektora
	 * @param z
	 *            vrijednost na koju se postavlja z-koordinata ovog vektora
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Metoda koja računa i dohvaća normu ovog vektora.
	 *
	 * @return norma ovog vekotra
	 */
	public double norm() {
		return Math.sqrt(Math.pow(x, SQUARE) + Math.pow(y, SQUARE) + Math.pow(z, SQUARE));
	}

	/**
	 * Metoda koja računa i vraća novi primjerak razreda {@link Vector3} koji
	 * predstavlja normalizirani vektor ovog primjerka. Važno je naglasiti da se
	 * pri tome niti na koji način ne mijenja ovaj primjerak razreda
	 * {@link Vector3}
	 *
	 * @return novi primjerak razreda {@link Vector3} koji predstavlja
	 *         normalizirani vektor ovog primjerka.
	 */
	public Vector3 normalized() {
		double norm = norm();
		if (doubleEquals(norm, 0)) {
			throw new ArithmeticException("Norma ovog vekora jednaka je nuli!");
		}
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Metoda koja računa i vraća novi primjerak razreda {@link Vector3} koji
	 * predstavlja vektorski zbroj ovog primjerka razreda {@link Vector3} i
	 * predanog primjerka razreda {@link Vector3} <b>other</b>. Važno je
	 * naglasiti da se pri tome niti na koji način ne mijenja ovaj primjerak
	 * razreda {@link Vector3} niti predani primjerak <b>other</b>
	 *
	 * @param other
	 *            primjerak ovog razreda koji predstavlja drugi pribrojnik
	 * @return novi primjerak razreda {@link Vector3} koji predstavlja zbroj
	 *         ovog primjerka i predanog primjerka <b>other</b> razreda
	 *         {@link Vector3}.
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Metoda koja računa i vraća novi primjerak razreda {@link Vector3} koji
	 * predstavlja vektorsku razliku ovog primjerka razreda {@link Vector3} i
	 * predanog primjerka razreda {@link Vector3} <b>other</b>. Važno je
	 * naglasiti da se pri tome niti na koji način ne mijenja ovaj primjerak
	 * razreda {@link Vector3} niti predani primjerak <b>other</b>
	 *
	 * @param other
	 *            primjerak ovog razreda koji predstavlja umanjenik
	 * @return novi primjerak razreda {@link Vector3} koji predstavlja razliku
	 *         ovog primjerka i predanog primjerka <b>other</b> razreda
	 *         {@link Vector3}.
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	/**
	 * Metoda koja računa i vraća skalarni produkt ovog primjerka razreda
	 * {@link Vector3} i predanog primjerka razreda {@link Vector3}
	 * <b>other</b>. Važno je naglasiti da se pri tome niti na koji način ne
	 * mijenja ovaj primjerak razreda {@link Vector3} niti predani primjerak
	 * <b>other</b>
	 *
	 * @param other
	 *            primjerak ovog razreda koji predstavlja drugi vektor u
	 *            skalarnom produktu
	 * @return vrijednost skalarnog produkta između ovog primjerka i primjerka
	 *         <b>other</b> razreda {@link Vector3}
	 */
	public double dot(Vector3 other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Metoda koja računa i vraća novi primjerak razreda {@link Vector3} koji
	 * predstavlja vektorski produkt ovog primjerka razreda {@link Vector3} i
	 * predanog primjerka razreda {@link Vector3} <b>other</b>. Važno je
	 * naglasiti da se pri tome niti na koji način ne mijenja ovaj primjerak
	 * razreda {@link Vector3} niti predani primjerak <b>other</b>
	 *
	 * @param other
	 *            primjerak ovog razreda koji predstavlja drugi vektor u
	 *            vektorskom produktu
	 * @return novi primjerak razreda {@link Vector3} koji predstavlja vektorski
	 *         umnožak ovog primjerka i predanog primjerka <b>other</b> razreda
	 *         {@link Vector3}.
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(this.y * other.z - other.y * this.z, other.x * this.z - this.x * other.z,
				this.x * other.y - other.x * this.y);
	}

	/**
	 * Metoda koja računa i vraća novi primjerak razreda {@link Vector3} koji
	 * predstavlja ovaj primjerak razreda {@link Vector3} koji je skaliran sa
	 * faktorom <b>s</b>. Važno je naglasiti da se pri tome niti na koji način
	 * ne mijenja ovaj primjerak razreda {@link Vector3} niti predani primjerak
	 * <b>other</b>
	 *
	 * @param other
	 *            broj s kojim se skalira
	 * @return novi primjerak razreda {@link Vector3} koji predstavlja ovaj
	 *         primjerak razreda {@link Vector3} koji je skaliran sa faktorom
	 *         <b>s</b>.
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Metoda koja računa i vraća vrijednost kosinusa kuta koji zatvaraju ovaj
	 * primjerka razreda {@link Vector3} i predani primjerka razreda
	 * {@link Vector3} <b>other</b>. Važno je naglasiti da se pri tome niti na
	 * koji način ne mijenja ovaj primjerak razreda {@link Vector3} niti predani
	 * primjerak <b>other</b>
	 *
	 * @param other
	 *            primjerak ovog razreda koji predstavlja vektor naspram kojeg
	 *            se računa kosinus kuta koji zatvaraju on i trenutni primjerak
	 *            razreda {@link Vector3}
	 * @return vrijednost kosinusa kuta koji zatvaraju ovaj primjerka razreda
	 *         {@link Vector3} i predani primjerka razreda {@link Vector3}
	 *         <b>other</b>.
	 */
	public double cosAngle(Vector3 other) {
		double denominator = this.norm() * other.norm();
		if (doubleEquals(denominator, 0)) {
			throw new ArithmeticException("Nazivnik je jednak nuli");
		}
		return dot(other) / denominator;
	}

	/**
	 * Metoda koja dohvaća x-koordinatu ovog primjerka razreda
	 *
	 * @return x-koordinatu ovog primjerka razreda
	 */
	public double getX() {
		return x;
	}

	/**
	 * Metoda koja dohvaća y-koordinatu ovog primjerka razreda
	 *
	 * @return y-koordinatu ovog primjerka razreda
	 */
	public double getY() {
		return y;
	}

	/**
	 * Metoda koja dohvaća z-koordinatu ovog primjerka razreda
	 *
	 * @return z-koordinatu ovog primjerka razreda
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Metoda koja <b>x</b>,<b>y</b> i <b>z</b> koordinate ovog vekotra pretvara
	 * u polje <b>double</b> vrijednosti veličine 3. Na prvom mjestu u polju
	 * nalazi se <b>x</b>, na drugom <b>y</b> i na trećem<b>z</b> koordinata
	 * ovog primjerka razreda {@link Vector3}
	 *
	 * @return polje <b>double</b> vrijednosti veličine 3. Na prvom mjestu u
	 *         polju nalazi se <b>x</b>, na drugom <b>y</b> i na trećem<b>z</b>
	 *         koordinata ovog primjerka razreda {@link Vector3}
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
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
		Vector3 other = (Vector3) obj;
		if (!doubleEquals(this.x, other.x))
			return false;
		if (!doubleEquals(this.y, other.y))
			return false;
		if (!doubleEquals(this.z, other.z))
			return false;
		return true;
	}

}
