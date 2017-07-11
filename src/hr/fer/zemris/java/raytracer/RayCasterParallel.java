package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred koji predstavlja program koji demonstrira bacanje zrake(engl. Ray
 * casting). Više o pojmu i načinu bacanja zrake možete pročitati
 * <a href ="https://en.wikipedia.org/wiki/Ray_casting" > na poveznici </a>
 * Program koristi {@link RayTracerViewer} za prikaz slike po završetku
 * izračuna. Izračun se vrši putem razreda koji implementira sučelje
 * {@link IRayTracerProducer}. Jedina razlika između ovog programa i programa
 * {@link RayCaster} je u tome što se kao razred koji implementira sučelje
 * {@link IRayTracerProducer} koristi {@link RayCasterParallelProducer}
 * 
 * @see RayTracerViewer
 * @see IRayTracerProducer
 * @see RayCasterParallelProducer
 * 
 * @author Davor Češljaš
 */
public class RayCasterParallel {

	/**
	 * Metoda od koje započinje izvođenje programa.
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(new RayCasterParallelProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}
}
