package hr.fer.zemris.math.complex;

/**
 * Enumeracija koja predstavlja tip tokena koji je primjerak razreda
 * {@link Token}. Moguće vrijednosti:
 * <ul>
 * <li>{@link TokenType#NUMBER}</li>
 * <li>{@link TokenType#IMAGINARY}</li>
 * <li>{@link TokenType#EOF}</li>
 * <li>{@link TokenType#OPERATOR}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public enum TokenType {

	/** Tip tokena koji predstavlja broj. Broj odgovara tipu {@link Double} */
	NUMBER,
	/** Tip tokena koji predstavlja imaginarnu jedinicu */
	IMAGINARY,
	/** Tip tokena koji predstavlja operator ('+' ili '-') */
	OPERATOR,
	/** Tip tokena koji predstavlja oznaku kraja niza */
	EOF
}
