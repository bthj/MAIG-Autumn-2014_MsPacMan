package bjrr.pacman.ann.feedforwardbackpropagation;

import java.io.Serializable;
import java.util.ArrayList;

public class Network implements Serializable {

	private static final long serialVersionUID = -2101882607518753326L;

	
	private ArrayList<Layer> layers;
	
	
	public Network() {
		
		this.layers = new ArrayList<Layer>();
	}
	
	
	public void addLayer( Layer layer ) {
		
		this.layers.add( layer );
		
		if( this.layers.size() > 1 ) {
			
			fullyConnectInputsFromPreviousLayer(
					this.layers.get(this.layers.size()-2), 
					layer );
			
//			fullyConnectOutputsToNextLayer(
//					this.layers.get(this.layers.size()-2), 
//					layer );
		}
	}
	
	private void fullyConnectInputsFromPreviousLayer( 
			Layer previous, Layer inputReceiver ) {
		
		for( Neuron oneReceivingLayerNeuron : inputReceiver.getNeuronsInLayer() ) {
			
			for( Neuron oneSendingLayerNeuron : previous.getNeuronsInLayer() ) {
				
				oneReceivingLayerNeuron.addNeuronToInputs( oneSendingLayerNeuron );
			}
		}
	}
	
//	private void fullyConnectOutputsToNextLayer( Layer outputSender, Layer next ) {
//		
//		for( Neuron oneSendingLayerNeuron : outputSender.getNeuronsInLayer() ) {
//			
//			for( Neuron oneReceivingLayerNeuron : next.getNeuronsInLayer() ) {
//				
//				oneSendingLayerNeuron.addNeuronToOutputs( oneReceivingLayerNeuron );
//			}
//		}
//	}
	
	
	public double[] computeOutputs( double[] input ) {
		// TODO: throw error if input count does not match neuron count in first layer
		
		for( int i=0; i < this.layers.size(); i++ ) {
			
			if( i == 0 ) { 
				// we'll set the output of the input layer neurons
				// to the value of the input.
				this.layers.get(i).setNeuronOutputsToInputValues( input );
				
			} else {
				
				this.layers.get(i).computeNeuronOutputValuesFromTheirInputs();
			}
		}
		// Now we'll obtain the output from the last layer
		return this.layers.get( this.layers.size()-1 ).getOutputsFromLayer();
	}
	
	public void initializeWithRandomWeights() {
		
		for( int i=0; i < this.layers.size(); i++ ) {
			
			if( i == 0 ) { // the input layer just passes values unchanged through
//				this.layers.get(i).initializeNeuronsInThisLayerWithFullWeights();
			} else {
				this.layers.get(i).initializeNeuronsInThisLayerWithRandomWeights();	
			}
		}
	}


	
	
	public ArrayList<Layer> getLayers() {
		return layers;
	}
	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

}
