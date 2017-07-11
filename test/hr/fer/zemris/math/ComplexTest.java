package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ComplexTest {

	@Test
	public void testiranjeModula() {
		Complex c1 = new Complex(-5, 6);
		assertEquals(c1.module(), 7.8102496, 0.001);
		Complex c2 = Complex.ZERO;
		assertEquals(c2.module(), 0, 0.001);
		Complex c3 = new Complex(2, 2);
		assertEquals(c3.module(), 2.828427124, 0.001);
		Complex c4 = new Complex(-1, -10);
		assertEquals(c4.module(), 10.0498756, 0.001);
	}

	@Test
	public void testiranjeUmnoska() {
		Complex c1 = new Complex(10, 24);
		Complex c2 = new Complex(-3, 7);
		Complex res = c1.multiply(c2);
		assertEquals(res.getRe(), -198, 0.0001);
		assertEquals(res.getIm(), -2, 0.0001);
	}

	@Test
	public void testiranjeDijeljenja() {
		Complex c1 = new Complex(10, -31);
		Complex c2 = new Complex(-3, 7);
		Complex res = c1.divide(c2);
		assertEquals(res.getRe(), -4.258620, 0.0001);
		assertEquals(res.getIm(), 0.3965517, 0.0001);
	}

	@Test(expected = ArithmeticException.class)
	public void dijeljenjeSNulom() {
		Complex c1 = new Complex(10, -31);
		c1.divide(Complex.ZERO);
	}

	@Test
	public void testiranjeZbrajanja() {
		Complex c1 = new Complex(3, 7);
		Complex c2 = new Complex(-5, 8);
		Complex c3 = new Complex(0, -1);
		assertEquals(c1.add(c2), new Complex(-2, 15));
		assertEquals(c3.add(c2), new Complex(-5, 7));
	}

	@Test
	public void testiranjeOduzimanja() {
		Complex c1 = new Complex(3, 7);
		Complex c2 = new Complex(-5, 8);
		Complex c3 = new Complex(0, -1);
		assertEquals(c1.sub(c2), new Complex(8, -1));
		assertEquals(c3.sub(c2), new Complex(5, -9));
	}

	@Test
	public void testiranjePotenciranja() {
		Complex c1 = new Complex(1.57, -2);
		assertEquals(c1.power(5), new Complex(-19.656821,104.4346799));

	}

	@Test(expected = IllegalArgumentException.class)
	public void greskaUPotenciranju() {
		Complex c1 = new Complex(1.57, -2);
		c1.power(-1);
	}

	@Test
	public void provjeraMetodeZaKorijen() {
		Complex c1 = new Complex(1.57, -2);
		List<Complex> roots = new ArrayList<>(
				Arrays.asList(
						new Complex(-0.300310 , 1.331429),
						new Complex(-1.002896,  -0.925791),
						new Complex(1.303207 , -0.405638)));
		List<Complex> myRoots = c1.root(3);
		assertEquals(myRoots, roots);
	}

	@Test(expected = IllegalArgumentException.class)
	public void greskaKorijenNula() {
		Complex c1 = new Complex(1.57, -2);
		c1.root(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void greskaKorijenManjiOdNula() {
		Complex c1 = new Complex(1.57, -2);
		c1.root(-2);
	}

	@Test
	public void negiranje(){
		Complex c = new Complex(5, -7);
		assertEquals(c.negate(), new Complex(-5,7));
	}
	
	@Test
	public void testiranjeUdaljenosti() {
		Complex from = new Complex(0,0);
		Complex to = new Complex(1,7);
		assertEquals(from.distance(to), 7.0710678,0.0001);
	}
}
