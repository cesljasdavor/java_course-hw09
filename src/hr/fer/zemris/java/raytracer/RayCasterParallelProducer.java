package hr.fer.zemris.java.raytracer;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred koji implementira sučelje {@link IRayTracerProducer}. Primjerci ovog
 * razreda koriste se u prgoramu {@link RayCasterParallel}. Ovaj razred sav
 * izračun sjecišta i boja vrši višedretveno(paralelno).
 * 
 * @see IRayTracerProducer
 * @see RayCasterParallel
 * 
 * @author Davor Češljaš
 */
public class RayCasterParallelProducer implements IRayTracerProducer {

	@Override
	public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
			int height, long requestNo, IRayTracerResultObserver observer) {
		short[] red = new short[width * height];
		short[] green = new short[width * height];
		short[] blue = new short[width * height];

		Point3D zAxis = view.sub(eye).normalize();
		Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp))).modifyNormalize();
		Point3D xAxis = zAxis.vectorProduct(yAxis).modifyNormalize();

		Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2)).add(yAxis.scalarMultiply(vertical / 2));

		Scene scene = RayTracerViewer.createPredefinedScene();

		final int maxRows = 16;
		Predicate<Integer> predicate = t -> t <= maxRows;

		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(new ColoringJob(eye, xAxis, yAxis, screenCorner, red, green, blue, scene, 0, height, width, height,
				vertical, horizontal, predicate));
		pool.shutdown();

		observer.acceptResult(red, green, blue, requestNo);
	}

	/**
	 * Razred koji nasljeđuje apstraktan razreda {@link RecursiveAction} i
	 * implementira jedinu apstraktnu metodu {@link #compute()}
	 * 
	 * @see RecursiveAction
	 * 
	 * @author Davor Češljaš
	 */
	private static class ColoringJob extends RecursiveAction {
		
		// čemu pojedine privatne varijable služe opisano u konstruktoru
		
		private static final long serialVersionUID = 1L;
		private Point3D eye;
		private Point3D xAxis;
		private Point3D yAxis;
		private Point3D screenCorner;
		private short[] red;
		private short[] green;
		private short[] blue;
		private Scene scene;
		private int yMin;
		private int yMax;
		private int width;
		private int height;
		private double vertical;
		private double horizontal;
		private Predicate<Integer> predicate;

		/**
		 * Konstruktor koji inicijalizira primjerak ovog razreda.
		 *
		 * @param eye
		 *            očište
		 * @param xAxis
		 *            jedinični vektor osi x
		 * @param yAxis
		 *            jedinični vektor osi y
		 * @param screenCorner
		 *            točka koja predstavlja točku (0,0) relativno na vektor OG
		 * @param red
		 *            utjecaj crvene boje na točke
		 * @param green
		 *            utjecaj zelene boje na točke
		 * @param blue
		 *            utjecaj plave boje na točke
		 * @param scene
		 *            scena u kojoj se vrši bacanje zrake
		 * @param yMin
		 *            visina od koje posao izračunava
		 * @param yMax
		 *            visina do koje posao izračunava
		 * @param width
		 *            širina rastera
		 * @param height
		 *            visina rastera
		 * @param vertical
		 *            vertikalna visina promatranog prostora
		 * @param horizontal
		 *            horizontalna širina promatranog prostora
		 * @param predicate
		 *            predikat kojim se testira je li posao potrebno još granati
		 *            ili je posao spreman za izračun
		 */
		public ColoringJob(Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner, short[] red, short[] green,
				short[] blue, Scene scene, int yMin, int yMax, int width, int height, double vertical,
				double horizontal, Predicate<Integer> predicate) {
			this.eye = Objects.requireNonNull(eye);
			this.xAxis = Objects.requireNonNull(xAxis);
			this.yAxis = Objects.requireNonNull(yAxis);
			this.screenCorner = Objects.requireNonNull(screenCorner);
			this.red = Objects.requireNonNull(red);
			this.green = Objects.requireNonNull(green);
			this.blue = Objects.requireNonNull(blue);
			this.scene = Objects.requireNonNull(scene);
			this.predicate = Objects.requireNonNull(predicate);
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
			this.vertical = vertical;
			this.horizontal = horizontal;
		}

		@Override
		protected void compute() {
			if (predicate.test(height / (yMax - yMin + 1))) {
				computeDirect();
				return;
			}
			invokeAll(
					new ColoringJob(eye, xAxis, yAxis, screenCorner, red, green, blue, scene, yMin,
							yMin + (yMax - yMin) / 2, width, height, vertical, horizontal, predicate),
					new ColoringJob(eye, xAxis, yAxis, screenCorner, red, green, blue, scene,
							yMin + (yMax - yMin) / 2 + 1, yMax, width, height, vertical, horizontal, predicate));
		}

		/**
		 * Metoda za izračun utjecaj RGB na točke u rasponu {@link #yMin} -
		 * {@link #yMax}
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin * width;
			for (int y = yMin; y < yMax; y++) {
				// negiran !
				Point3D yPart = yAxis.scalarMultiply(-((double) y / (height - 1)) * vertical);
				Point3D cornerMinusYPart = yPart.modifyAdd(screenCorner);

				for (int x = 0; x < width; x++) {
					Point3D xPart = xAxis.scalarMultiply(((double) x / (width - 1)) * horizontal);
					Point3D screenPoint = cornerMinusYPart.add(xPart);
					Ray ray = Ray.fromPoints(eye, screenPoint);

					RayCasterUtil.tracer(scene, ray, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}

		}
	}
}
