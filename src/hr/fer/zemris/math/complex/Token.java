package hr.fer.zemris.math.complex;

/**
 * Pomoćni razred koji se koristi prilikom leksičke analize ulaznog niza
 * primjerkom razreda {@link ComplexLexer}. Razred predstavlja jedan token
 * (leksičku jedinku). Primjerci ovog razreda su nepromjenjivi.
 * 
 * @author Davor Češljaš
 */
public class Token {
	
	/** Vrijednost leksičke jedinke */
	private Object value;
	
	/** Tip leksičke jedinke */
	private TokenType type;

	/**
	 * Konstruktor koji inicijalizira atribute leksičke jedinke
	 *
	 * @param type
	 *            tip leksičke jedinke
	 * @param value
	 *            vrijednost leksičke jedinke
	 */
	public Token(Object value, TokenType type) {
		this.value = value;
		this.type = type;
	}

	/**
	 * Dohvaća vrijednost leksičke jedinke
	 *
	 * @return vrijednost leksičke jedinke
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Dohvaća tip leksičke jedinke
	 *
	 * @return tip leksičke jedinke
	 */
	public TokenType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Token [value=" + value + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Token other = (Token) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
