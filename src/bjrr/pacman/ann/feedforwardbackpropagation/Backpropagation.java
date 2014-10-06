package bjrr.pacman.ann.feedforwardbackpropagation;

public class Backpropagation {

	private Network network;
	private double[][] inputTrainingSet;
	private double[][] idealOutputSet;
	private double learningRate;
	
	private double error;  // error rate from the last training iteration / epoch.
	
	public Backpropagation( 
			Network networkToTrain,
			double[][] inputSet, 
			double[][] idealOutputs, 
			double learningRate) {
		
		this.network = networkToTrain;
		this.inputTrainingSet = inputSet;
		this.idealOutputSet = idealOutputs;
		this.learningRate = learningRate;
		
		this.error = Double.MAX_VALUE;
	}
	
	/*
	 * One epoch.
	 */
	public void performTrainingIteration() {
		
		this.error = 0;
		
		for( int i=0; i < inputTrainingSet.length; i++ ) {
			
			double[] oneInputTrainingTuple = inputTrainingSet[i];
			double[] oneOutputTrainingTuple = idealOutputSet[i];
			
			double[] actualNetworkOutput = network.computeOutputs( oneInputTrainingTuple );
			
			
			// we'll perform case updating of the network weights, rather than batch updating
			
			for( int layerIndex = network.getLayers().size()-1; layerIndex > 0; layerIndex-- ) {
				
				if( layerIndex == network.getLayers().size()-1 ) {
					// we have the output layer
					network.getLayers().get(layerIndex)
					.computeNeuronErrorsRelativeToKnownOutputAndUpdateWeightsAndBiases(
							oneOutputTrainingTuple, learningRate );
					
				} else {
					
					network.getLayers().get(layerIndex)
					.compteNeuronErrorsRelativeToConnectedOutputsAndUpdateWeightsAndBiases(
							learningRate );
				}
			}
			
			
			double tupleError = 0;
			for( int e=0; e < oneOutputTrainingTuple.length; e++ ) {
				
				tupleError += Math.abs( oneOutputTrainingTuple[e] - actualNetworkOutput[e] );
			}
			tupleError = tupleError / oneOutputTrainingTuple.length;
			this.error += tupleError;
		}
		this.error = this.error / inputTrainingSet.length;
	}

	
	
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}

	public double getLearningRate() {
		return learningRate;
	}
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

}
