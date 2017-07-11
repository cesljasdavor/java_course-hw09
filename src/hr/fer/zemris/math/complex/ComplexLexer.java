package hr.fer.zemris.math.complex;

import java.util.NoSuchElementException;

/**
 * Razred predstavlja leksički analizator koji leksički analizira ulaz
 * pokušavajući iz njega dobiti <b>kompleksan broj</b>. Leksičke jedinke koje
 * ovaj razred generira predstavljene su razredom {@link Token}. Ovaj leksički
 * analizator koristi {@link ComplexParser} prilikom parsiranja kompleksnog
 * broja. Format koji se može leksički analizirati je <i>a + ib</i> gdje i
 * predstavlja imaginarnu jedinicu i gdje se <i>a</i> ili <i>b</i> mogu
 * izostaviti u slučaju da su jednaki 0.
 * 
 * @see Token
 * @see ComplexParser
 * 
 * @author Davor Češljaš
 */
public class ComplexLexer {

	/** Konstanta koja predstavlja znak '-'. */
	public static final char MINUS = '-';

	/** Konstanta koja predstavlja znak '+'. */
	public static final char PLUS = '+';

	/** Konstanta koja predstavlja znak '.' */
	private static final char DOT = '.';

	/** Konstanta koja predstavlja znak 'i' - imaginarnu jedinicu */
	private static final char IMAGINARY_UNIT = 'i';

	/**
	 * Konstantan primjerak razreda {@link Token} koji predstavlja token za
	 * imaginarnu jedinicu
	 */
	public static final Token IMAGINARY = new Token(IMAGINARY_UNIT, TokenType.IMAGINARY);

	/**
	 * Konstantan primjerak razreda {@link Token} koji predstavlja token za kraj
	 * niza
	 */
	public static final Token EOF = new Token(null, TokenType.EOF);

	/** Ulazni niz znakova koji se leksički analizira */
	private char[] data;

	/** Trenutni pozicija u ulaznom nizu znakova */
	private int currentIndex;

	/** Zadnje izvađeni token */
	private Token currentToken;

	/**
	 * Konstruktor koji iz ulaznog primjerka razreda {@link String}
	 * inicijalizira ulazni niz znakova te postavlja.
	 *
	 * @param input
	 *            ulazni primjerak razreda {@link String} koji se leksički
	 *            analizira
	 * @throws IllegalArgumentException
	 *             ukoliko je kao <b>input</b> predan <b>null</b>
	 * 
	 */
	public ComplexLexer(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Leksička analiza ne može biti izvedena nad null");
		}

		this.data = input.toCharArray();
	}

	/**
	 * Analizator iz predanog ulaznog teksta pokušava izvaditi sljedeći token.
	 * Način vađenja sljedećeg tokena ovisi o stanju leksičkog analizatora.
	 * Metoda ujedino ažurira trenutni token. Izvađeni token sada je ponovo
	 * moguće dohvatiti pozivom metode {@link #getToken()}
	 *
	 * @return sljedeći token iz ulaznog niza
	 */
	public Token nextToken() {
		extractToken();
		return currentToken;
	}

	/**
	 * Pomoćna metoda koja ovisno o stanju vrši vađenje sljedećeg tokena . Ako
	 * uspije izvađeni token će biti postavljen kao trenutni token
	 */
	private void extractToken() {
		if (currentToken != null && currentToken.getType() == TokenType.EOF) {
			throw new NoSuchElementException("Nemam više tokena!");
		}

		skipWhitespaces();

		if (isEOF()) {
			currentToken = EOF;
		} else if (data[currentIndex] == IMAGINARY_UNIT) {
			currentToken = IMAGINARY;
			currentIndex++;
		} else if (isOperator(data[currentIndex]) && !Character.isDigit(data[currentIndex + 1])) {
			currentToken = new Token(data[currentIndex++], TokenType.OPERATOR);
		} else {
			extractNumber();
		}
	}

	/**
	 * Provjerava je li znak operator
	 * 
	 * @see TokenType#OPERATOR
	 *
	 * @param c
	 *            znak koji provjeravamo
	 * @return <b>true</b> ukoliko je <b>c</b> operator, <b>false</b> inače
	 */
	private boolean isOperator(char c) {
		return c == PLUS || c == MINUS;
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link TokenType#NUMBER}, a koji predstavlja broj.
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko se naredni niz znakova ne može parsirati u broj
	 *             korištenjem metode {@link Double#parseDouble(String)}
	 */
	private void extractNumber() {
		StringBuilder sb = new StringBuilder();

		int factor = 1;
		char c = data[currentIndex];
		if (isOperator(c)) {
			if (data[currentIndex] == MINUS) {
				factor = -factor;
			}
			currentIndex++;
		}

		while (!isEOF()) {
			c = data[currentIndex];
			if (!Character.isDigit(c) && c != DOT) {
				break;
			}
			sb.append(c);
			currentIndex++;
		}

		try {
			currentToken = new Token(Double.parseDouble(sb.toString()) * factor, TokenType.NUMBER);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Ne mogu parsirati predani broj '" + sb.toString() + "'");
		}
	}

	/**
	 * Pomoćna metoda koja ispituje jesmo li došli do kraja ulaznog niza.
	 *
	 * @return <b>true</b> ukoliko smo došli do kraj niza <b>false</b> inače
	 */
	private boolean isEOF() {
		return currentIndex >= data.length;
	}

	/**
	 * Pomoćna metoda koja se koristi za preskakanje praznina u ulaznom nizu.
	 */
	private void skipWhitespaces() {
		while (!isEOF() && isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Pomoćna metoda koja ispituje je li predani znak praznina. Kao praznine se
	 * podrazumjevaju znakovi :
	 * <ul>
	 * <li>'\t'</li>
	 * <li>'\r'</li>
	 * <li>'\n'</li>
	 * <li>' '</li>
	 * </ul>
	 * 
	 *
	 * @param c
	 *            znak koji se provjerava
	 * @return <b>true </b> ukoliko je <b>c</b> praznina, inače vraća
	 *         <b>false</b>
	 */
	private boolean isWhitespace(char c) {
		return c == '\n' || c == '\t' || c == '\r' || c == ' ';
	}
}
