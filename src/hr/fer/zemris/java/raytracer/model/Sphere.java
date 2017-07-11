package hr.fer.zemris.java.raytracer.model;

import hr.fer.zemris.java.raytracer.RayCaster;
import hr.fer.zemris.java.raytracer.RayCasterParallel;
import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred koji implementira sučelje {@link GraphicalObject}. Razred predstavlja
 * sferu u sceni prilikom iscrtavanja razredom {@link RayTracerViewer}. Ova
 * implementacija koristi se kroz programe {@link RayCaster} i
 * {@link RayCasterParallel}.
 * 
 * @see GraphicalObject
 * @see RayTracerViewer
 * @see RayCaster
 * @see RayCasterParallel
 * 
 * @author Davor Češljaš
 */
public class Sphere extends GraphicalObject {

	/**
	 * Članska varijabla koja je primjerak razreda {@link Point3D} i predstavlja
	 * središte sfere
	 */
	private Point3D center;

	/** Članska varijabla koja predstavlja radijus sfere */
	private double radius;

	/** @see RayIntersection#getKdr() */
	private double kdr;

	/** @see RayIntersection#getKdg() */
	private double kdg;

	/** @see RayIntersection#getKdb() */
	private double kdb;

	/** @see RayIntersection#getKrr() */
	private double krr;

	/** @see RayIntersection#getKrg() */
	private double krg;

	/** @see RayIntersection#getKrb() */
	private double krb;

	/** @see RayIntersection#getKrn() */
	private double krn;

	/**
	 * Konstruktor koji inicijalizira primjerak ovo razreda . Unutar
	 * konstruktora pohranjuju se sve predane vrijednosti parametara. <b>Za
	 * značenje koeficijenata korisnik se navodi na dokumentaciju razreda
	 * {@link RayIntersection}</b>
	 *
	 * @param center
	 *            primjerak razreda {@link Point3D} koji predstavlja središte
	 *            sfere
	 * @param radius
	 *            vrijednost koja predstavlja radijus sfere
	 * @param kdr
	 * @param kdg
	 * @param kdb
	 * @param krr
	 * @param krg
	 * @param krb
	 * @param krn
	 * 
	 * @see RayIntersection
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		// predpostavak je da je a = 1 jer je d - normiziran, ako nije rezultat
		// neće biti dobar
		Point3D startMinusCenter = ray.start.sub(center);
		double b = 2 * ray.direction.scalarProduct(ray.start.sub(center));
		double c = startMinusCenter.scalarProduct(startMinusCenter) - radius * radius;
		double determinant = b * b - 4 * c;
		// kompleksno rješenje
		if (determinant < 0) {
			return null;
		}

		return determinIntersection(determinant, b, ray);
	}

	/**
	 * Pomoćna metoda koja se koristi kako bi se odredilo presjecište zrake
	 * modelirane razredom {@link Ray} <b>ray</b> i ovog primjerka razreda
	 *
	 * @param determinant
	 *            determinanta izračunata unutar
	 *            {@link #findClosestRayIntersection(Ray)}
	 * @param b
	 *            konstanta b izračunata unutar
	 *            {@link #findClosestRayIntersection(Ray)}
	 * @param ray
	 *            primjerak razreda {@link Ray} s kojim se traži presjecište
	 * @return primjerak razreda koji implementira sučelje
	 *         {@link RayIntersection}, a koji modelira presjecište zrake
	 *         <b>ray</b> i ove sfere ili <code>null</code> ukoliko presjecište
	 *         ne postoji ili je iza očišta
	 */
	private RayIntersection determinIntersection(double determinant, double b, Ray ray) {
		double closestLambda;
		boolean outer;
		if (determinant == 0 && (-b) > 0) {
			closestLambda = -b;
			outer = true;
		} else {
			double root = Math.sqrt(determinant);
			double lambda1 = (-b + root) / 2;
			double lambda2 = (-b - root) / 2;
			if (lambda1 > 0 && lambda2 > 0) {
				closestLambda = Math.min(lambda1, lambda2);
				outer = true;
			} else if (lambda1 > 0 || lambda2 > 0) {
				closestLambda = Math.max(lambda1, lambda2);
				outer = false;
			} else {
				return null;
			}
		}

		Point3D directionVector = ray.direction.scalarMultiply(closestLambda);
		return new RaySphereIntersection(ray.start.add(directionVector), directionVector.norm(), outer);
	}

	/**
	 * Razred koji implementira sučelje {@link RayIntersection}. Ovaj razred
	 * predstavlja sjecište zrake i sfere modelirane razredom {@link Sphere}.
	 * 
	 * @see RayIntersection
	 * 
	 * @author Davor Češljaš
	 */
	private class RaySphereIntersection extends RayIntersection {

		/** Normala na površinu sfere modelirane razredom {@link Sphere} */
		private Point3D normal;

		/**
		 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor
		 * pohranjuje točku sjecište <b>point</b>, udaljenost od očišta do
		 * presjecišta <b>distance</b> te je li presjecište vanjsko ili
		 * unutarnje <b>outer</b>
		 * 
		 * @param point
		 *            točka sjecišta modelirana sa razredom {@link Point3D}
		 * @param distance
		 *            udaljenost od očišta do presjecišta
		 * @param outer
		 *            <code>true</code> ako je presjecište vanjsko,
		 *            <code>false</code> inače
		 */
		public RaySphereIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
			normal = this.getPoint().sub(center).modifyNormalize();
		}

		@Override
		public Point3D getNormal() {
			return normal;
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}

	}
}
