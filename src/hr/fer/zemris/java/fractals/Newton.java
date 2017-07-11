package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Razred predstavlja program koji crta Newton-Raphsonov fraktal ovisno od
 * unesenim kompleksnim brojevima. Format kojem treba odgovarati kompleksan broj
 * definiran je unutar opisa razreda {@link Complex}, a za parsiranje se koristi
 * metoda {@link Complex#parse(String)}. Po završetku unosa kompleksnih brojeva
 * potrebno je upisati {@value #DONE}. Po završetku upisa poziva se
 * {@link FractalViewer#show(hr.fer.zemris.java.fractals.viewer.IFractalProducer)}
 * te se fraktal iscrtava u novom prozoru.
 * 
 * <pre>
 * Primjer interakcije:
 * 
 * Root 1><b>1</b> 
 * Root 2><b>-1 + i0</b> 
 * Root 3><b>i</b> 
 * Root 4><b>0 -i1</b> 
 * Root 5><b>done</b>
 * 
 * boldano je korisnikov unos
 * </pre>
 * 
 * @see Complex
 * @see FractalViewer
 * @see IFractalProducer
 * 
 * @author Davor Češljaš
 */
public class Newton {

	private static final String DONE = "done";

	/**
	 * Metoda od koje započinje izvođenje programa.
	 *
	 * @param args
	 *           	argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {

		System.out.println("Dobrodošli u Newton-Raphson fraktalni preglednik na temelju iteracija");
		System.out.println("Molim Vas unesite barem dva korijena, jedan po liniji. Upišite 'done' kada ste gotovi:");

		List<Complex> roots = new ArrayList<>();
		int count = 1;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.printf("Korijen %d>", count);
			String line = sc.nextLine().trim();
			if (line.equalsIgnoreCase(DONE)) {
				break;
			}
			try {
				roots.add(Complex.parse(line));
				count++;
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		sc.close();
		System.out.println("Slika fraktala biti će Vam uskoro prikazana. Hvala!");
		FractalViewer
				.show(new NewtonFractalProducer(new ComplexRootedPolynomial(roots.toArray(new Complex[roots.size()]))));
	}
}
