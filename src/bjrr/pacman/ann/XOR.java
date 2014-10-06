/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package bjrr.pacman.ann;

import java.io.IOException;

import bjrr.pacman.ann.feedforwardbackpropagation.Backpropagation;
import bjrr.pacman.ann.feedforwardbackpropagation.Layer;
import bjrr.pacman.ann.feedforwardbackpropagation.Network;
import bjrr.pacman.ann.feedforwardbackpropagation.SerializeObject;

/**
 * Chapter 5: The Feedforward Backpropagation Neural Network
 * 
 * XOR: Learn the XOR pattern with a feedforward neural network that
 * uses backpropagation.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class XOR {
	
	private static final String networkDataFileName = "XOR_ANN.save";

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) throws IOException, ClassNotFoundException {
		
		Network network = new Network();
		network.addLayer( new Layer(2) );
		network.addLayer( new Layer(3) );
		network.addLayer( new Layer(1) );
		network.initializeWithRandomWeights();

		// train the neural network
		Backpropagation train = new Backpropagation(network, XOR_INPUT, XOR_IDEAL, 0.9);

		int epoch = 1;

		do {
			train.performTrainingIteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 5000) && (train.getError() > 0.001));

		
//		Network network = 
//				(Network) SerializeObject.load("XOR_ANN.save");
		
		// test the neural network
		System.out.println("Neural Network Results:");
		for (int i = 0; i < XOR_IDEAL.length; i++) {
			final double actual[] = network.computeOutputs(XOR_INPUT[i]);
			System.out.println(XOR_INPUT[i][0] + "," + XOR_INPUT[i][1]
					+ ", actual=" + actual[0] + ",ideal=" + XOR_IDEAL[i][0]);
		}
		
		
		SerializeObject.save(networkDataFileName, network);
	}
}
