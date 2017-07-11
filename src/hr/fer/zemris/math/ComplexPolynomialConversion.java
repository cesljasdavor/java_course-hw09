package hr.fer.zemris.math;

/**
 * Razreda koji predstavlja demonstracijski program koji demonstrira konverziju
 * i primjerka razreda {@link ComplexRootedPolynomial} u primjerak razreda
 * {@link ComplexPolynomial}, koristeći metodu
 * {@link ComplexRootedPolynomial#toComplexPolynom()}
 * 
 * @see Complex
 * @see ComplexPolynomial
 * @see ComplexRootedPolynomial
 * 
 * @author Davor Češljaš
 */
public class ComplexPolynomialConversion {

	/**
	 * Metoda od koje započinje izvođenje programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		ComplexRootedPolynomial rooted = new ComplexRootedPolynomial(new Complex(1, 0), new Complex(2, 0),
				new Complex(3, 0));

		System.out.println(rooted.toComplexPolynom().toString());
		System.out.println(new Complex(2345.550, -255.1).divide(new Complex(2, -3)));
	}
}
