package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3Test {

	@Test
	public void testiranjeNorme() {
		Vector3 vect = new Vector3(5,  7, 9);
		assertEquals(vect.norm(), 12.4498996,0.00001);
	}
	
	@Test(expected = ArithmeticException.class)
	public void normalizacijaVektoraDijeljenjeSNulom() throws Exception {
		new Vector3(0,  0, 0).normalized();
	}
	
	@Test
	public void testiranjeNormiziranogVektora() {
		Vector3 vect = new Vector3(5,  7, 9);
		double norm = 12.4498996;
		assertEquals(vect.normalized(),new Vector3(5/norm, 7/norm, 9/norm));
	}
	
	@Test
	public void testiranjeZbrajanja() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		Vector3 vec2 = new Vector3(7, 66, 27);
		assertEquals(vec1.add(vec2), new Vector3(12, 73, 36));
	}

	@Test
	public void testiranjeOduzimanja() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		Vector3 vec2 = new Vector3(7, 66, 27);
		assertEquals(vec1.sub(vec2), new Vector3(-2, -59, -18));
	}
		
	@Test
	public void testiranjeSkalarnogProdukta() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		Vector3 vec2 = new Vector3(2, 4, 3);
		assertEquals(vec1.dot(vec2), 65, 0.00001);
	}
	
	@Test
	public void testiranjeVektorskogProdukta() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		Vector3 vec2 = new Vector3(2, 4, 3);
		assertEquals(vec1.cross(vec2), new Vector3(-15, 3, 6));
	}
	
	@Test
	public void testiranjeSkaliranjaVektora() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		double scale = 5;
		assertEquals(vec1.scale(scale), new Vector3(25, 35, 45));
	}
	
	@Test
	public void testiranjeKosinusaKuta() {
		Vector3 vec1 = new Vector3(5, 7, 9);
		Vector3 vec2 = new Vector3(2, 4, 3);
		assertEquals(vec1.cosAngle(vec2), 0.96950155, 0.0001);
	}
	

}
