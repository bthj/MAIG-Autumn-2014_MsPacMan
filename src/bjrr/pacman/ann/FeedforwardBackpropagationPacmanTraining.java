package bjrr.pacman.ann;

import java.io.IOException;

import bjrr.pacman.ann.dataRecording.DataSaverLoader;
import bjrr.pacman.ann.dataRecording.DataTuple;
import bjrr.pacman.ann.feedforwardbackpropagation.Backpropagation;
import bjrr.pacman.ann.feedforwardbackpropagation.Layer;
import bjrr.pacman.ann.feedforwardbackpropagation.Network;
import bjrr.pacman.ann.feedforwardbackpropagation.SerializeObject;

public class FeedforwardBackpropagationPacmanTraining {
	
	
	public static double[] getTrainingInputFromData( DataTuple oneTuple ) {
		double[] oneTrainingInput = new double[12];

		oneTrainingInput[0] = oneTuple.normalizeLevel( oneTuple.currentLevel );
		oneTrainingInput[1] = oneTuple.normalizePosition( oneTuple.pacmanPosition );
		oneTrainingInput[2] = oneTuple.normalizeNumberOfPills( oneTuple.numOfPillsLeft );
		oneTrainingInput[3] = oneTuple.normalizeNumberOfPowerPills( oneTuple.numOfPowerPillsLeft );
		
		oneTrainingInput[4] = oneTuple.normalizeBoolean( oneTuple.isBlinkyEdible );
		oneTrainingInput[5] = oneTuple.normalizeBoolean( oneTuple.isInkyEdible );
		oneTrainingInput[6] = oneTuple.normalizeBoolean( oneTuple.isPinkyEdible );
		oneTrainingInput[7] = oneTuple.normalizeBoolean( oneTuple.isSueEdible );
		
		if( oneTuple.blinkyDist > 0 ) {
			oneTrainingInput[8] = oneTuple.normalizeDistance( oneTuple.blinkyDist );
		} else {
			oneTrainingInput[8] = 1;
		}
		if( oneTuple.inkyDist > 0 ) {
			oneTrainingInput[9] = oneTuple.normalizeDistance( oneTuple.inkyDist );
		} else {
			oneTrainingInput[9] = 1;
		}
		if( oneTuple.pinkyDist > 0 ) {
			oneTrainingInput[10] = oneTuple.normalizeDistance( oneTuple.pinkyDist );
		} else {
			oneTrainingInput[10] = 1;
		}
		if( oneTuple.sueDist > 0 ) {
			oneTrainingInput[11] = oneTuple.normalizeDistance( oneTuple.sueDist );
		} else {
			oneTrainingInput[11] = 1;
		}
/**
		
		The following inputs result in a much slower decrease in error rate:
		
		// current level
		oneTrainingInput[0] = oneTuple.normalizeLevel( oneTuple.currentLevel );
		
		// pacman position
		oneTrainingInput[1] = oneTuple.normalizePosition( oneTuple.pacmanPosition );
		
		oneTrainingInput[2] = oneTuple.normalizeNumberOfPills( oneTuple.numOfPillsLeft );
		oneTrainingInput[3] = oneTuple.normalizeNumberOfPowerPills( oneTuple.numOfPowerPillsLeft );
		
		// is last direction same as previously chosen?
		oneTrainingInput[4] = oneTuple.normalizeBoolean( oneTuple.lastDirectionSame );
		
		// distance to closest pill
		oneTrainingInput[5] = oneTuple.normalizeDistance( oneTuple.distanceToClosestPill );
		
		// distance to closest power pill
		oneTrainingInput[6] = oneTuple.normalizeDistance( oneTuple.distanceToClosestPowerPill );
		
		// distance to closest ghost
		oneTrainingInput[7] = oneTuple.normalizeDistance( oneTuple.distanceToClosestGhost );
			
		// is closest ghost edible?
		oneTrainingInput[8] = oneTuple.normalizeBoolean( oneTuple.isClosestGhostEdible );
		
		// is closest ghost moving in same direction as pacman
		oneTrainingInput[9] = oneTuple.normalizeBoolean( oneTuple.isClosestGhostMovingInSameDirectionAsPacman );
		
		// number of power pills left
		oneTrainingInput[10] = oneTuple.normalizeNumberOfPills( oneTuple.numOfPillsLeft );
		
		// number of power pills left
		oneTrainingInput[11] = oneTuple.normalizeNumberOfPowerPills( oneTuple.numOfPowerPillsLeft );
*/
		
		return oneTrainingInput;
	}
	

	public static void main(String[] args) throws IOException {
		
		
		DataTuple[] trainingData = DataSaverLoader.LoadPacManData();
		
		// We have 10 input neurons
		double[][] pacmanTrainingInput = new double[trainingData.length][];
		/*
		 * We'll have four outputs, each for one of possible movement.
		 * The order of output neurons per movement is respectively:
		 * UP[0], RIGHT[1], DOWN[2], LEFT[3]
		 */
		double[][] pacmanTrainingOutput = new double[trainingData.length][4];
		
		// let's populate the training tuples with normalized data
		for( int i=0; i < trainingData.length; i++ ) {
			DataTuple oneTuple = trainingData[i];
			
			double[] oneTrainingInput = getTrainingInputFromData( oneTuple );
			
			pacmanTrainingInput[i] = oneTrainingInput;
			
			
			pacmanTrainingOutput[i][0] = 0;
			pacmanTrainingOutput[i][1] = 0;
			pacmanTrainingOutput[i][2] = 0;
			pacmanTrainingOutput[i][3] = 0;
			switch ( oneTuple.DirectionChosen ) {
			case UP:
				pacmanTrainingOutput[i][0] = 1;
				break;
			case RIGHT:
				pacmanTrainingOutput[i][1] = 1;
				break;
			case DOWN:
				pacmanTrainingOutput[i][2] = 1;
				break;
			case LEFT:
				pacmanTrainingOutput[i][3] = 1;
				break;
			default:
				break;
			}
		}
		

		/**
		 * Let's construct the feed forward network.
		 * 
		 * We'll set the number if input neurons to the same number of elements in
		 * each row of pacmanTrainingOutput[][]
		 * Number out output neurons will be the same as the number of elements in
		 * each row of pacmanTrainingOutput[][]
		 * 
		 * For the number of hidden neurons we'll start with the rule of thumb stating:
		 * "The number of hidden neurons should be 2/3 the size of the input layer, 
		 * plus the size of the output layer."
		 * see: http://www.heatonresearch.com/node/707
		 * 
		 */
		final Network network = new Network();
		// input layer
		network.addLayer( new Layer(pacmanTrainingInput[0].length) );
		// hidden layer
		int hiddenNeuronCountAsFractionOfInputCount = Math.round(
				pacmanTrainingInput[0].length*((float)2/3));
		final int hiddenNeuronCount = 
				hiddenNeuronCountAsFractionOfInputCount + pacmanTrainingOutput[0].length;
		
		
		// much worse: int hiddenNeuronCount = (int)Math.round( pacmanTrainingInput[0].length * 1.7 );
		// also bad:
//		int hiddenNeuronCount = Math.round( (
//				pacmanTrainingInput[0].length + pacmanTrainingOutput[0].length) / 2 );
		
		System.out.println( "Input neuron count: " + pacmanTrainingInput[0].length);
		System.out.println( "Hidden neuron count: " + hiddenNeuronCount);
		
//		hiddenNeuronCount += 20;
		network.addLayer( new Layer( hiddenNeuronCount ) );
		// output layer
		network.addLayer( new Layer( pacmanTrainingOutput[0].length ));
		// let's initialize the network with random weights
		network.initializeWithRandomWeights();
		
		
		/*
		 * Let's save the the network if the training is interrupted: 
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { 
				try {
					
					saveNetwork(network, hiddenNeuronCount);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		/**
		 * Let's assign back propagation to each of the layers.
		 * We'll have a fixed learning rate of 0.7 and a
		 * momentum of 0.9 which scales the learning from the previous iteration
		 * before applying it to the current iteration, i.e. it specifies
		 * how much of an effect the previous training iteration will have
		 * on the current iteration.
		 */
		double learningRateDivisor = 1;
		double learningRate = 1 / learningRateDivisor;
//		double learningRate = 0.1;
//		double learningRate = 1;
		Backpropagation train = new Backpropagation(
				network, pacmanTrainingInput, pacmanTrainingOutput, learningRate );

		
		int epoch = 1;
		/**
		 * Let's do one million training iterations 
		 * or stop training when the error rate is less than .001
		 */
		do {
			if( epoch % 100 == 0 ) {
				learningRateDivisor++;
				learningRate = 1 / learningRateDivisor;
				train.setLearningRate( learningRate );
				System.out.println( "Epoch #" + epoch + 
						" Error: " + train.getError() +
						" Learning rate: " + learningRate );
			}
//			train.setLearningRate( (double)1 / epoch );
			
			train.performTrainingIteration();
			
			epoch++;
			
		} while( epoch < 1000000 && train.getError() > .001 );
		
		
		/**
		 * Now after training is complete, we'll save the trained network to disk
		 * to be able to load it later in a pac man controller.
		 */
		saveNetwork(network, hiddenNeuronCount);
	}


	private static void saveNetwork(final Network network, int hiddenNeuronCount)
			throws IOException {
		
		String pacmanNeuralNetworkFilename = 
				"pacmanANN__hiddenNeuronCount_" + hiddenNeuronCount + ".save";
		
		SerializeObject.save(pacmanNeuralNetworkFilename, network);
	}
}
