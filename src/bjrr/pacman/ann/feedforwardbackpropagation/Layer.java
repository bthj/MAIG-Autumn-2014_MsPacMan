package bjrr.pacman.ann.feedforwardbackpropagation;

import java.io.Serializable;
import java.util.ArrayList;

public class Layer implements Serializable {

	private static final long serialVersionUID = -3678962085286277609L;

	
	private ArrayList<Neuron> neuronsInLayer;
	
	
	public Layer( int neuronCount ) {
		
		this.neuronsInLayer = new ArrayList<Neuron>();
		
		for( int i=0; i < neuronCount; i++ ) {
			
			Neuron oneNeuron = new Neuron();
			this.neuronsInLayer.add( oneNeuron );
		}
	}

	
	public void initializeNeuronsInThisLayerWithRandomWeights() {
	
		for( Neuron oneNeuron : this.neuronsInLayer ) {
			
			oneNeuron.setRanndomWeightsToAllInputConnections();
		}
	}
	
//	public void initializeNeuronsInThisLayerWithFullWeights() {
//		
//		for( Neuron oneNeuron : this.neuronsInLayer ) {
//			
//			oneNeuron.setFullWeightsToAllInputConnections();
//		}
//	}
	

	/*
	 * Used by the Network when this is the first, input layer.
	 */
	public void setNeuronOutputsToInputValues( double[] input ) {
		
		for( int i=0; i < input.length; i++ ) {
			
			this.neuronsInLayer.get(i).setOutputValue( input[i] );
		}
	}
	
	public void computeNeuronOutputValuesFromTheirInputs() {
		
		for( Neuron oneNeuron : this.neuronsInLayer ) {
			
			oneNeuron.computeAndSaveOutputValue();
		}
	}
	
	/*
	 * Used by the Network to get output when this is the last layer
	 */	
	public double[] getOutputsFromLayer() {
		double[] output = new double[ this.neuronsInLayer.size() ];
		for( int i=0; i < this.neuronsInLayer.size(); i++ ) {
			
			output[i] = this.neuronsInLayer.get(i).getOutputValue();
		}
		return output;
	}
	
	
	///////// backpropagation 
	
	/*
	 * Used when this is the output layer.
	 */
	public void computeNeuronErrorsRelativeToKnownOutputAndUpdateWeightsAndBiases( 
			double[] knownOutput, double learningRate ) {
		
		for( int i=0; i < this.neuronsInLayer.size(); i++ ) {
			
			Neuron oneOutputLayerNeuron = this.neuronsInLayer.get(i);
			
			oneOutputLayerNeuron.computeAndSaveErrorRelativeToKnownTargetValue(
					knownOutput[i] );
			
			updateWeightsAndBias( oneOutputLayerNeuron, learningRate );
		}
	}
	
	/*
	 * Used when this is a hidden layer.
	 */
	public void compteNeuronErrorsRelativeToConnectedOutputsAndUpdateWeightsAndBiases( 
			double learningRate ) {
		
		for( Neuron oneHiddenLayerNeuron : this.neuronsInLayer ) {
			
			oneHiddenLayerNeuron.computeAndSaveErrorRelativeToConnectedOutputs();
			
			updateWeightsAndBias(oneHiddenLayerNeuron, learningRate);
		}
	}
	
	private void updateWeightsAndBias( Neuron neuron, double learningRate ) {
		
		neuron.updateInputWeights( learningRate );
		neuron.updateBias( learningRate );
	}
	
	
	
	public ArrayList<Neuron> getNeuronsInLayer() {
		return neuronsInLayer;
	}
	public void setNeuronsInLayer(ArrayList<Neuron> neuronsInLayer) {
		this.neuronsInLayer = neuronsInLayer;
	}
}
