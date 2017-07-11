package hr.fer.zemris.math;

/**
 * Razred predstavlja demonstracijski primjer koji demonstrira rad s primjercima
 * razreda {@link Vector3}
 * 
 * @see Vector3
 * 
 * @author Davor Češljaš
 */
public class Vector3Demo {

	/**
	 * Metoda od koje započinje izvođenje ovog programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste.
	 */
	public static void main(String[] args) {
		Vector3 i = new Vector3(1, 0, 0);
		Vector3 j = new Vector3(0, 1, 0);
		Vector3 k = i.cross(j);

		Vector3 l = k.add(j).scale(5);
		Vector3 m = l.normalized();
		System.out.println(i);
		System.out.println(j);
		System.out.println(k);
		System.out.println(l);
		System.out.println(l.norm());
		System.out.println(m);
		System.out.println(l.dot(j));
		System.out.println(i.add(new Vector3(0, 1, 0)).cosAngle(l));
	}
}
