package bjrr.pacman.ann.feedforwardbackpropagation;

import java.io.Serializable;

/**
 * Represents a connection between two neurons across two layers
 * in a feed forward neural network.
 * 
 * @author bthj
 *
 */

public class Connection implements Serializable {

	private static final long serialVersionUID = -6205127806492707467L;
	
	
	private Neuron sender;
	private Neuron receiver;
	private double weight;
	
	
	
	public Neuron getSender() {
		return sender;
	}
	public void setSender(Neuron sender) {
		this.sender = sender;
	}
	public Neuron getReceiver() {
		return receiver;
	}
	public void setReceiver(Neuron receiver) {
		this.receiver = receiver;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
