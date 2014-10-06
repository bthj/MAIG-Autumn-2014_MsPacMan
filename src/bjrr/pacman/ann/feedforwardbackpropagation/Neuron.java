package bjrr.pacman.ann.feedforwardbackpropagation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Neuron implements Serializable {

	private static final long serialVersionUID = -2962388376641019583L;
	
	
	private ArrayList<Connection> inputs;
	private ArrayList<Connection> outputs;
	
	private double outputValue;
	private double bias;
	private double error;
	
	
	public Neuron() {

		this.inputs = new ArrayList<Connection>();
		this.outputs = new ArrayList<Connection>();
	}
	
	
	/*
	 *	Defines the input connections to this neuron,
	 *	with associated weights.
	 */
//	class Input {
//		Neuron neuronToRecieveFrom;
//		double weight;
//	}
	
	
	/*
	 * We'll use this connection to reach the neurons in the 
	 * next higher layer, when computing the error for this neuron.
	 */
//	class Output {
//		Neuron neuronToOutputTo;
//		double weight; // TODO: the encapsulating Network, or Backpropagation?, class will have to set and maintain this value.
//	}
	
	
	public void updateInputWeights( double learningRate ) {
		
		for( Connection oneInput : inputs ) {
			
			double errorDelta = learningRate * this.error * oneInput.getSender().getOutputValue();
			oneInput.setWeight( oneInput.getWeight() + errorDelta );
		}
	}
	
	public void updateBias( double learningRate ) {
		
		double biasDelta = learningRate * this.error;
		this.bias += biasDelta;
	}
	
	
	/*
	 * Compute input to this neuron unit by summing all weighted inputs
	 * and adding the bias.
	 */
	public double getWeightedInputValueToThisUnit() {
		double weightedInputValueSum = 0;
		
		for( Connection oneInput : inputs ) {
			
			weightedInputValueSum += 
					oneInput.getWeight() * oneInput.getSender().getOutputValue();
		}
		weightedInputValueSum += this.bias;
		
		return weightedInputValueSum;
	}
	
	/*
	 * Computes the output value and saves it to a local variable,
	 * whose value can be then obtained via its access method.
	 * The logistic, or sigmoid, function is used.
	 */
	public void computeAndSaveOutputValue() {
		
		double inputToThisUnit = getWeightedInputValueToThisUnit();
		
		this.outputValue = 1 / (1 + Math.exp(-inputToThisUnit)); // <-- sigmoid function
	}
	
	
	
	///////// error calculation 
	
	/*
	 * Used for neurons in the output layer
	 */
	public void computeAndSaveErrorRelativeToKnownTargetValue( double targetValue ) {
		// here we use the derivative of the logistic / sigmoid function
		this.error = 
				this.outputValue*( 1 - this.outputValue ) // <-- sigmoid derivative 
				* ( targetValue - this.outputValue );
	}

	/*
	 * Used for neurons in hidden layers
	 */
	public void computeAndSaveErrorRelativeToConnectedOutputs() {
		
		double nextLayerWeightedErrorSum = 0;
		for( Connection oneOutput : outputs ) {
			
			nextLayerWeightedErrorSum += 
					oneOutput.getReceiver().getError() * oneOutput.getWeight();
			
			this.error = this.outputValue * ( 1 - this.outputValue ) // <-- sigmoid derivative
					* nextLayerWeightedErrorSum;
		}
	}
	
	
	///// initialization
	public void setRanndomWeightsToAllInputConnections() {
		Random rand = new Random();
		for( Connection oneInput : this.inputs ) {
			
			oneInput.setWeight( rand.nextDouble() );
		}
	}
	
	/*
	 * Used for neurons in the input layer
	 */
//	public void setFullWeightsToAllInputConnections() {
//		
//		for( Input oneInput : this.inputs ) {
//			
//			oneInput.weight = 1;
//		}
//	}
	
	public void addNeuronToInputs( Neuron inputNeuron ) {
		
		Connection connection = new Connection();
		connection.setSender(inputNeuron);
		connection.setReceiver(this);
		
		this.inputs.add( connection );
		inputNeuron.getOutputs().add( connection );
		
//		Input newInput = new Input();
//		newInput.neuronToRecieveFrom = inputNeuron;
//		this.inputs.add( newInput );
	}
	
//	public void addNeuronToOutputs( Neuron neuronToOutputTo ) {
//		
//		Output newOutput = new Output();
//		newOutput.neuronToOutputTo = neuronToOutputTo;
//	}
	
	
	///// access methods

	public ArrayList<Connection> getInputs() {
		return inputs;
	}
	public void setInputs(ArrayList<Connection> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<Connection> getOutputs() {
		return outputs;
	}
	public void setOutputs(ArrayList<Connection> outputs) {
		this.outputs = outputs;
	}

	public double getOutputValue() {
		return outputValue;
	}
	public void setOutputValue(double output) {
		this.outputValue = output;
	}

	public double getBias() {
		return bias;
	}
	public void setBias(double bias) {
		this.bias = bias;
	}

	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}
	
}
