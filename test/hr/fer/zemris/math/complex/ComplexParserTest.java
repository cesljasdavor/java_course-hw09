package hr.fer.zemris.math.complex;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.math.Complex;

public class ComplexParserTest {

	@Test(expected = IllegalArgumentException.class)
	public void predanNull() {
		new ComplexParser(null);
	}

	@Test
	public void testiranjePrimjer1() {
		ComplexParser parser = new ComplexParser("1");

		assertEquals(parser.getResult(), new Complex(1, 0));
	}

	@Test
	public void testiranjePrimjer2() {
		ComplexParser parser = new ComplexParser("\t-1 +   i0");

		assertEquals(parser.getResult(), new Complex(-1, 0));
	}
	
	@Test
	public void testiranjePrimjer3() {
		ComplexParser parser = new ComplexParser("i");

		assertEquals(parser.getResult(), new Complex(0, 1));
	}
	
	@Test
	public void testiranjePrimjer4() {
		ComplexParser parser = new ComplexParser("  -i");

		assertEquals(parser.getResult(), new Complex(0, -1));
	}
	
	@Test
	public void testiranjePrimjer5() {
		ComplexParser parser = new ComplexParser("0.25889 - i1.3445");

		assertEquals(parser.getResult(), new Complex(0.25889, -1.3445));
	}
	
	@Test
	public void testiranjePrimjer6() {
		ComplexParser parser = new ComplexParser("0.25889 - i1.3445 + \t 78.299  +   i 350.22");

		assertEquals(parser.getResult(), new Complex(78.557890, 348.875500));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjePrimjer7() {
		//i na krivom mjestu
		new ComplexParser("15.27 + 2.88i");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testiranjePrimjer8() {
		//dva operatora jedan iz drugog
		new ComplexParser("15.27 + +  -i2.88");
	}
	
}
