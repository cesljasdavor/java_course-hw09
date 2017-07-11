package hr.fer.zemris.math.complex;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.math.Complex;

/**
 * Razred koji predstavlja sintaksni analizator. Razred sadrži primjerak razreda
 * {@link ComplexLexer} te nad njim poziva {@link ComplexLexer#nextToken()} i
 * gradi generativno stablo.Razred započinje parsiranje prilikom inicijalizacije
 * (poziva konstruktora). Rezultat je moguće dohvatiti pomoću metode
 * {@link #getResult()}. Rezultat je primjerak razreda {@link Complex}
 * 
 * @see ComplexLexer
 * @see Complex
 * 
 * @author Davor Češljaš
 */
public class ComplexParser {

	/**
	 * {@link List} svih primjeraka razreda {@link Token} koji je dobiven
	 * pozivom {@link ComplexLexer#nextToken()}
	 */
	private List<Token> tokens;

	/** Primjerak razreda {@link Complex} koji predstavlja rezultat */
	private Complex result;

	/** Faktor koji se mijenja nailaskom na pojednini operator */
	private int factor;

	/**
	 * Zastavica koja ukazuje parsira li se broj kao imaginaran ili realan dio
	 * kompleksnog broja
	 */
	private boolean imaginary;

	/**
	 * Konstruktor koji stvara primjerak razreda {@link ComplexLexer} i predaje
	 * mu predani tekst <b>input</b>. Nakon uspješnog stvaranja leksičkog
	 * analizatora kreće sintaksna analiza unutar koje se gradi generativno
	 * stablo.
	 *
	 * @param input
	 *            tekst koji je potrebno parsirati u generativno stablo
	 * 
	 * @throws SmartScriptParserException
	 *             ukoliko iz predanog teksta <b>input</b> nije moguće stvoriti
	 *             generativno stablo
	 */
	public ComplexParser(String input) {
		if (input == null || input.trim().isEmpty()) {
			throw new IllegalArgumentException("Ne mogu parsirati kompleksan broj iz null niti iz praznog niza!");
		}

		setTokens(new ComplexLexer(input));
		factor = 1;
		// neutralni element za zbrajanje
		result = Complex.ZERO;
		parse();
	}

	/**
	 * Pomoćna metoda koja stvara i puni {@link #tokens} sa svime što vrati
	 * {@link ComplexLexer#nextToken()}. Metoda prosljeđuje sve iznimke od
	 * leksičkog analizatora predstavljenog primjerkom razreda
	 * {@link ComplexLexer}
	 *
	 * @param lexer
	 *            leksički analizator nad kojim se poziva
	 *            {@link ComplexLexer#nextToken()} dok se ne dođe do
	 *            {@link ComplexLexer#EOF}
	 */
	private void setTokens(ComplexLexer lexer) {
		tokens = new ArrayList<>();
		while (true) {
			Token token = lexer.nextToken();
			tokens.add(token);
			if (token.equals(ComplexLexer.EOF))
				break;
		}
	}

	/**
	 * Metoda koja dohvaća kompleksni broj koji je rezultat parsiranja
	 *
	 * @return kompleksni broj koji je rezultat parsiranja
	 */
	public Complex getResult() {
		return result;
	}

	/**
	 * Pomoćna metoda od koje kreće parsiranje tokena spremljenih u {@link List}
	 * {@link #tokens}. Po završetku ove metode u {@link #result} je spremljen
	 * rezultat parsiranja.
	 * 
	 * @throws IllegalArgumentException
	 *             ako je format predanog ulaznog niza krivi
	 */
	private void parse() {
		// zadnji token je sigruno EOF i njega ne trebam gledati
		for (int i = 0, len = tokens.size() - 1; i < len; i++) {
			Token token = tokens.get(i);
			TokenType type = token.getType();
			if (type.equals(TokenType.OPERATOR)) {
				parseOperator(i, token);
			} else if (token.equals(ComplexLexer.IMAGINARY)) {
				parseImaginary(i, len);
			} else if (type.equals(TokenType.NUMBER)) {
				// krivi poredak imaginarne jedinice i broja koji mora baciti
				// iznimku
				checkIfNextIsImaginary(i + 1);
				parseNumber(token);
			}
		}
	}

	/**
	 * Metoda koja parsira primjerak razreda {@link Token} tipa
	 * {@link TokenType#IMAGINARY} ovisno o okruženju u kojem se nalazi
	 *
	 * @param index
	 *            trenutni index unutra {@link #tokens}
	 * @param len
	 *            predstavlja duljinu {@link List#size()} od {@link #tokens}
	 */
	private void parseImaginary(int index, int len) {
		if (index + 1 < len && tokens.get(index + 1).getType().equals(TokenType.NUMBER)) {
			imaginary = true;
		} else {
			result = result.add(new Complex(0, factor));
			factor = 1;
		}
	}

	/**
	 * Metoda koja parsira primjerak razreda {@link Token} tipa
	 * {@link TokenType#OPERATOR} ovisno o okruženju u kojem se nalazi
	 *
	 * @param index
	 *            trenutni index unutra {@link #tokens}
	 * @param token
	 *            primjerak razreda {@link Token} koji je tipa
	 *            {@link TokenType#OPERATOR}
	 */
	private void parseOperator(int index, Token token) {
		if (index != 0 && tokens.get(index - 1).getType().equals(TokenType.OPERATOR)) {
			throw new IllegalArgumentException("Ne mogu parsirati dva operatora jedan iza drugog!");
		}
		char operator = (Character) token.getValue();
		if (operator == ComplexLexer.MINUS) {
			factor = -factor;
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li sljedeći token u listi
	 * {@link #tokens} {@link ComplexLexer#IMAGINARY}. Ukoliko jest metoda baca
	 * {@link IllegalArgumentException} jer je format kompleksnog broja krivi
	 *
	 * @param nextIndex
	 *            pozicija sljedećeg tokena u {@link List}i {@link #tokens}
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko je sljedeći token {@link ComplexLexer#IMAGINARY}
	 */
	private void checkIfNextIsImaginary(int nextIndex) {
		if (nextIndex == tokens.size()) {
			return;
		}
		if (tokens.get(nextIndex).equals(ComplexLexer.IMAGINARY)) {
			throw new IllegalArgumentException("Krivi format mora biti 'i*b', a ne 'b*i' (b je dani broj)!");
		}
	}

	/**
	 * Metoda koja parsira primjerak razreda {@link Token} tipa
	 * {@link TokenType#NUMBER} ovisno o okruženju u kojem se nalazi
	 *
	 * @param token
	 *            {@link Token} tipa {@link TokenType#NUMBER} koji se parsira
	 */
	private void parseNumber(Token token) {
		double value = (Double) token.getValue();
		value *= factor;
		// postavi rezultat
		result = imaginary ? result.add(new Complex(0, value)) : result.add(new Complex(value, 0));

		// resetiraj stanje
		factor = 1;
		imaginary = false;
	}
}
