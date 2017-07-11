package hr.fer.zemris.java.fractals;

import java.util.concurrent.ThreadFactory;

/**
 * Razred koji implementira sučelje {@link ThreadFactory} i koji preko metode
 * {@link #newThread(Runnable)} stvara nove demonske opisnike dretvi koji su modelirani
 * razredom {@link Thread} kojima predaje primjerak sučelja
 * {@link Runnable}
 * 
 * @see ThreadFactory
 * @see Thread
 * @see Runnable
 * 
 * @author Davor Češljaš
 */
public class DaemonicThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread daemon = new Thread(r);
		daemon.setDaemon(true);
		return daemon;
	}

}
