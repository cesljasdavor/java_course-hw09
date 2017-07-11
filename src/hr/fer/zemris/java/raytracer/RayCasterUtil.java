package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.math.DoubleUtil;

/**
 * Razred koji predstavlja biblioteku koja sadrži samo jednu javnu metodu
 * {@link #tracer(Scene, Ray, short[])}. Razred je izgrađen kako se isti kod ne
 * bi ponavljao kroz dva programa. {@link RayCaster} i {@link RayCasterParallel}
 * 
 * @see RayCaster
 * @see RayCasterParallel
 * 
 * @author Davor Češljaš
 */
public class RayCasterUtil {

	/**
	 * Metoda koja vrši bacanje zrake predstavljene sa primjerkom razreda
	 * {@link Ray} <b>ray</b> unutar scene <b>scene</b>
	 *
	 * @param scene
	 *            scena unutar koje se vrši bacanje zrake
	 * @param ray
	 *            zraka koja se baca
	 * @param rgb
	 *            rgb za točku na sceni koju pogađa ova zraka
	 */
	public static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;

		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}

		findPixelColor(closest, scene, ray, rgb);
	}

	/**
	 * Pomoćna metoda koja koja započinje bojanje točke sjecišta zrake i tijela
	 * oblikovanog sučelje {@link GraphicalObject} u sceni s kojim se našlo
	 * presjecište.
	 *
	 * @param closest
	 *            najbliže sjecište zrake od očišta i nekog objekta u sceni
	 * @param scene
	 *            scena unutar koje se vrši bacanje zrake
	 * @param ray
	 *            zraka koja se baca
	 * @param rgb
	 *            rgb za točku na sceni koju pogađa ova zraka
	 */
	private static void findPixelColor(RayIntersection closest, Scene scene, Ray ray, short[] rgb) {
		// ambijentna komponenta
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;

		for (LightSource lightSource : scene.getLights()) {
			Point3D lsPoint = lightSource.getPoint();
			Point3D closestPoint = closest.getPoint();
			Ray lightSourceRay = Ray.fromPoints(lsPoint, closestPoint);
			RayIntersection closestToLightSource = findClosestIntersection(scene, lightSourceRay);

			double distanceLS = closestToLightSource.getDistance();
			double distanceIntersection = closestPoint.sub(lsPoint).norm();

			if (DoubleUtil.DOUBLE_COMPARATOR.compare(distanceLS, distanceIntersection) < 0) {
				continue;
			}

			findLightSourceInfluence(lightSource, lightSourceRay, ray, closest, distanceIntersection, rgb);
		}
	}

	/**
	 * Pomoćna metoda koja za pojedini izvor svjetlosti traži utjecaj na
	 * presjecište <b>closest</b>
	 *
	 * @param lightSource
	 *            izvor svjetlosti
	 * @param lightSourceRay
	 *            zraka od izvora svjetlosti
	 * @param fromEyeRay
	 *            zraka od očišta
	 * @param closest
	 *            najbliže sjecište zrake od očišta i nekog objekta u sceni
	 * @param distanceLS
	 *            udaljenost od izvora svjetlosti
	 * @param rgb
	 *            rgb za točku na sceni koju pogađa ova zraka
	 */
	private static void findLightSourceInfluence(LightSource lightSource, Ray lightSourceRay, Ray fromEyeRay,
			RayIntersection closest, double distanceLS, short[] rgb) {
		Point3D normal = closest.getNormal();
		Point3D vectorLS = lightSourceRay.direction;
		Point3D reflectedVector = getReflectedVector(vectorLS, normal);
		Point3D vectorEye = fromEyeRay.direction;
		if (DoubleUtil.doubleEquals(distanceLS, 0)) {
			return;
		}

		double diffuseCoef = Math.abs(vectorLS.scalarProduct(normal));
		double reflectiveCoef = Math.abs(Math.pow(reflectedVector.scalarProduct(vectorEye), closest.getKrn()));

		rgb[0] += lightSource.getR() * (closest.getKdr() * diffuseCoef + closest.getKrr() * reflectiveCoef);
		rgb[1] += lightSource.getG() * (closest.getKdg() * diffuseCoef + closest.getKrg() * reflectiveCoef);
		rgb[2] += lightSource.getB() * (closest.getKdb() * diffuseCoef + closest.getKrb() * reflectiveCoef);

	}

	/**
	 * Pomoćna metoda koja dohvaća reflektirani vektor od <b>direction</b> i
	 * <b>normal</b>
	 *
	 * @param direction
	 *            vektor koji se reflektira
	 * @param normal
	 *            vektor obzirom na koji se reflektira
	 * @return reflektirani vektor oblikovan sa razredom {@link Point3D}
	 */
	private static Point3D getReflectedVector(Point3D direction, Point3D normal) {
		return normal.scalarMultiply(normal.scalarProduct(direction) * 2).modifySub(direction);
	}

	/**
	 * Pomoćna metoda koja pronalazi najbliže sjecište zrake i elemenata u
	 * sceni. Sjecište je oblikovano sučelje {@link RayIntersection}
	 *
	 * @param scene
	 *            scena unutar koje se baca zraka
	 * @param ray
	 *            zraka koja se baca
	 * @return najbliže sjecište nekog elementa u sceni ili <code>null</code> ukoliko zraka
	 *         nema sjecište niti s jednim objektom u sceni
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		double minDistance = Double.MAX_VALUE;
		RayIntersection closestIntersection = null;
		for (GraphicalObject go : scene.getObjects()) {
			RayIntersection intersection = go.findClosestRayIntersection(ray);
			if (intersection == null) {
				continue;
			}

			double distance = intersection.getDistance();
			if (closestIntersection == null || distance < minDistance) {
				closestIntersection = intersection;
				minDistance = distance;
			}
		}

		return closestIntersection != null ? closestIntersection : null;
	}
}
