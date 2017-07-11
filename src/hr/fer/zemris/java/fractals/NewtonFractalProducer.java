package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Razred koji predstavlja implementaciju sučelja {@link IFractalProducer} za
 * Newton-Raphsonov fraktal. Razred implementira višedretveno punjenje polja
 * <b>short</b> vrijednosti koji se predaje metodi
 * {@link IFractalResultObserver#acceptResult(short[], short, long)}. Za više
 * detalja o Newton-Raphsonovom fraktalu i kako se on crta molimo Vas da
 * kliknete na
 * <a href = "https://en.wikipedia.org/wiki/Newton_fractal">poveznicu</a>
 * 
 * @see IFractalProducer
 * @see IFractalResultObserver
 * 
 * @author Davor Češljaš
 */
public class NewtonFractalProducer implements IFractalProducer {

	/**
	 * Konstanta koja predstavlja podrazumjevanu vrijednost granice
	 * konvergencije
	 */
	private static final double DEFAULT_CONVERGENCE_TRESHOLD = 0.0001;

	/**
	 * Konstanta koja predstavlja podrazumjevanu vrijednost granice minimalne
	 * udaljenosti od korijena polinoma predstavljenog sa primjerkom razreda
	 * {@link ComplexRootedPolynomial} do određene točke u kompleksnoj ravnini
	 */
	private static final double DEFAULT_ROOT_TRESHOLD = 0.0002;

	/** Članska varijabla koja predstavlja funkciju u obliku polinoma */
	private final ComplexRootedPolynomial polynomial;

	/**
	 * Članska varijabla koja predstavlja derivaciju funkcije u obliku polinoma
	 */
	private final ComplexPolynomial derived;

	/** Članska varijabla koja predstavlja granicu konvergencije */
	private final double convergenceTreshold;

	/**
	 * Članska varijabla koja predstavlja granicu minimalne udaljenosti od
	 * korijena polinoma predstavljenog sa primjerkom razreda
	 * {@link ComplexRootedPolynomial} do određene točke u kompleksnoj ravnini
	 */
	private final double rootTreshold;

	/**
	 * Članska varijabla koja predstavlja thread-pool kojem se predaju zadaci
	 * izračuna predstavljeni razredom {@link ComputationJob}
	 */
	private ExecutorService pool;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstruktora pohranjuje se predani argument <b>polynomial</b> kao polinom
	 * ovog primjerka razreda te se računa derivacija tog polinoma. Za
	 * {@link #convergenceTreshold} i {@link #rootTreshold} postavljaju se
	 * {@value #DEFAULT_CONVERGENCE_TRESHOLD} i {@value #DEFAULT_ROOT_TRESHOLD}
	 *
	 * @param polynomial
	 *            polinom koji se pohranjuje te kojem se računa derivacija (koja
	 *            se također pohranjuje)
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko je predani argument <code>null</code>
	 */
	public NewtonFractalProducer(ComplexRootedPolynomial polynomial) {
		this(polynomial, DEFAULT_CONVERGENCE_TRESHOLD, DEFAULT_ROOT_TRESHOLD);
	}

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstruktora pohranjuje se predani argument <b>polynomial</b> kao polinom
	 * ovog primjerka razreda te se računa derivacija tog polinoma. Pohranjuju
	 * se također i predani argumenti <b>convergenceTreshold</b> i
	 * <b>rootTreshold</b> unutar {@link #convergenceTreshold} i
	 * {@link #rootTreshold}
	 *
	 * @param polynomial
	 *            polinom koji se pohranjuje te kojem se računa derivacija (koja
	 *            se također pohranjuje)
	 * @param convergenceTreshold
	 *            vrijednost koje se pohranjuje kao granica konvergencije
	 * @param rootTreshold
	 *            vrijednost koja se pohranjuje kao granica minimalne
	 *            udaljenosti od korijena polinoma predstavljenog sa primjerkom
	 *            razreda {@link ComplexRootedPolynomial} do određene točke u
	 *            kompleksnoj ravnini
	 * @throws IllegalArgumentException
	 *             ukoliko je predani argument <code>null</code>
	 */
	public NewtonFractalProducer(ComplexRootedPolynomial polynomial, double convergenceTreshold, double rootTreshold) {
		if (polynomial == null) {
			throw new IllegalArgumentException("Polinom ne smije biti null!");
		}

		this.polynomial = polynomial;
		this.derived = polynomial.toComplexPolynom().derive();
		this.convergenceTreshold = convergenceTreshold;
		this.rootTreshold = rootTreshold;
		this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
				new DaemonicThreadFactory());
	}

	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
			IFractalResultObserver observer) {
		short[] data = new short[width * height];
		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		// 8 zadano u zadataku (eksterniziraj)
		final int numberOfLanes = availableProcessors * 8;
		int numberOfYInLane = height / numberOfLanes;

		int maxIter = 4096;
		List<Future<Void>> results = new ArrayList<>();
		for (int i = 0; i < numberOfLanes; i++) {
			int yMin = i * numberOfYInLane;
			int yMax = (i + 1) * numberOfYInLane - 1;
			if (i == numberOfLanes - 1) {
				yMax = height - 1;
			}
			ComputationJob job = new ComputationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, maxIter,
					data);
			results.add(pool.submit(job));
		}

		for (Future<Void> job : results) {
			try {
				job.get();
			} catch (InterruptedException | ExecutionException e) {
			}
		}

		short m = (short) (polynomial.toComplexPolynom().order() + 1);
		observer.acceptResult(data, m, requestNo);
	}

	/**
	 * The Class ComputationJob.
	 */
	private class ComputationJob implements Callable<Void> {

		/** minimalni iznos realne komponente */
		private double reMin;

		/** maksimalni iznos realne komponente */
		private double reMax;

		/** minimalni iznos imaginarne komponente */
		private double imMin;

		/** maksimalni iznos imaginarne komponente */
		private double imMax;

		/** širina rastera na kojem se vizualizira fraktal */
		private int width;

		/** visina rastera na kojem se vizualizira fraktal */
		private int height;

		/** početna y vrijednost od koje ovaj posao kreće */
		private int yMin;

		/** završna y vrijednost na kojoj ovaj posao staje */
		private int yMax;

		/** Maksimalni broj iteracija prije odluke o knovergenciji */
		private int maxIter;

		/**
		 * Polje podataka koje se puni izračunatim podacima, od mjesta
		 * {@link #yMin} * {@link #width} do mjesta {@link #yMax} *
		 * {@link #width} isključivo
		 */
		private short[] data;

		/**
		 * Konstruktor koji inicijalizira primjerak ovog razreda. Sve predane
		 * vrijednosti interno se pohranjuju
		 *
		 * @param reMin
		 *            minimalni iznos realne komponente
		 * @param reMax
		 *            maksimalni iznos realne komponente
		 * @param imMin
		 *            minimalni iznos imaginarne komponente
		 * @param imMax
		 *            maksimalni iznos imaginarne komponente
		 * @param width
		 *            širina rastera na kojem se vizualizira fraktal
		 * @param height
		 *            visina rastera na kojem se vizualizira fraktal
		 * @param yMin
		 *            početna y vrijednost od koje ovaj posao kreće
		 * @param yMax
		 *            završna y vrijednost na kojoj ovaj posao staje
		 * @param maxIter
		 *            Maksimalni broj iteracija prije odluke o knovergenciji
		 * @param data
		 *            Polje podataka koje se puni izračunatim podacima, od
		 *            mjesta {@link #yMin} * {@link #width} do mjesta
		 *            {@link #yMax} * {@link #width} isključivo
		 */
		public ComputationJob(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int maxIter, short[] data) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.maxIter = maxIter;
			this.data = data;
		}

		@Override
		public Void call() {
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Complex c = mapToComplexPlain(x, y);
					int index = computeIteration(c);
					data[x + y * width] = (short) (index + 1);
				}
			}

			return null;
		}

		/**
		 * Pomoćna metoda koja predane parametre <b>x</b> i <b>y</b> mapira u
		 * kompleknu ravninu te iz njih stvara primjerak razreda
		 * {@link Complex}, koji predstavlja jednu točku unutar kompleksne
		 * ravnine
		 *
		 * @param x
		 *            x-koordinata
		 * @param y
		 *            y-koordinata
		 * @return primjerak razreda {@link Complex}, koji predstavlja jednu
		 *         točku unutar kompleksne ravnine
		 */
		private Complex mapToComplexPlain(int x, int y) {
			double re = ((double) x / width) * (reMax - reMin) + reMin;
			double im = ((double) (height - 1 - y) / height) * (imMax - imMin) + imMin;
			return new Complex(re, im);
		}

		/**
		 * Metoda koja vrši izračun Newton-Raphsonove iteracije za točku
		 * kompleksne ravnine <b>c</b> koja je primjerak razreda {@link Complex}
		 *
		 * @param c
		 *            točku kompleksne ravnine <b>c</b> koja je primjerak
		 *            razreda {@link Complex}
		 * @return indeks najbližeg korijena polinoma ili -1 ukoliko je
		 *         udaljenost veća od {@link NewtonFractalProducer#rootTreshold}
		 */
		private int computeIteration(Complex c) {
			Complex zn = c;
			int iteration = 0;
			double distance;
			do {
				Complex numerator = polynomial.apply(zn);
				Complex denominator = derived.apply(zn);
				if (denominator.equals(Complex.ZERO)) {
					break;
				}
				Complex fraction = numerator.divide(denominator);
				Complex zn1 = zn.sub(fraction);
				distance = zn1.distance(zn);
				zn = zn1;
				iteration++;
			} while (distance > convergenceTreshold && iteration < maxIter);

			return polynomial.indexOfClosestRootFor(zn, rootTreshold);
		}
	}

}
