package hr.fer.zemris.math.complex;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

import hr.fer.zemris.math.complex.ComplexLexer;
import hr.fer.zemris.math.complex.TokenType;

public class ComplexLexerTest {
	
	// Testovi za stanje legera SmartLexerState.TAG
	@Test
	public void testiranjeZaPrazanNiz() {
		ComplexLexer lexer = new ComplexLexer("");

		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjeZaNizNull() {
		// must throw!
		new ComplexLexer(null);
	}

	@Test
	public void testiranjeTipaTokenaPrazanNiz() {
		ComplexLexer lexer = new ComplexLexer("");

		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getType());
	}

	@Test(expected = NoSuchElementException.class)
	public void testiranjeRadaNakonEOF() {
		ComplexLexer lexer = new ComplexLexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}

	@Test
	public void testiranjeSamoPraznine() {
		ComplexLexer lexer = new ComplexLexer(" \t\n\r  \n");

		assertEquals(TokenType.EOF, lexer.nextToken().getType());
	}
	
	@Test
	public void testiranjePrimjer1() {
		ComplexLexer lexer = new ComplexLexer("1");

		assertEquals(TokenType.NUMBER, lexer.nextToken().getType());
	}

	@Test
	public void testiranjePrimjer2() {
		ComplexLexer lexer = new ComplexLexer("\t-1 +   i0");

		assertEquals(TokenType.NUMBER, lexer.nextToken().getType());
		assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
		assertEquals(TokenType.IMAGINARY, lexer.nextToken().getType());
		assertEquals(TokenType.NUMBER, lexer.nextToken().getType());
	}
	
	@Test
	public void testiranjePrimjer3() {
		ComplexLexer lexer = new ComplexLexer("i");

		assertEquals(TokenType.IMAGINARY, lexer.nextToken().getType());
	}
	
	@Test
	public void testiranjePrimjer4() {
		ComplexLexer lexer = new ComplexLexer("0.25889 - i1.3445");

		assertEquals(TokenType.NUMBER, lexer.nextToken().getType());
		assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
		assertEquals(TokenType.IMAGINARY, lexer.nextToken().getType());
		assertEquals(TokenType.NUMBER, lexer.nextToken().getType());
	}
		
}
