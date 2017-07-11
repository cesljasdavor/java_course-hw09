package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred koji predstavlja program koji demonstrira bacanje zrake(engl. Ray
 * casting). Više o pojmu i načinu bacanja zrake možete pročitati
 * <a href ="https://en.wikipedia.org/wiki/Ray_casting" > na poveznici </a>
 * Program koristi {@link RayTracerViewer} za prikaz slike po završetku
 * izračuna. Izračun se vrši putem razreda koji implementira sučelje
 * {@link IRayTracerProducer}.
 * 
 * @see RayTracerViewer
 * @see IRayTracerProducer
 * 
 * @author Davor Češljaš
 */
public class RayCaster {

	/**
	 * Metoda od koje započinje izvođenje programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Metoda tvornica koja stvara primjerak razreda koji implementira sučelje
	 * {@link IRayTracerProducer} koji se koristi za izračun unutar
	 * {@link RayTracerViewer}.. Ovaj primjerak razreda koristi se za izračun
	 * doprinosa crvene, zelene i plave (RGB) za svaku točku scene prilikom rada
	 * ovog programa
	 *
	 * @return primjerak razreda koji implementira sučelje
	 *         {@link IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp))).modifyNormalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).modifyNormalize();

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
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
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

}
