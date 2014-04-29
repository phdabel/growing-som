package ufrgs.maslab.gsom.util;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;
import ufrgs.maslab.gsom.neuron.Neuron;

public class NeuronFactory {
	
	public static Neuron createNeuron(Position position) {

		Neuron neuron;
		
		try {
			neuron = new Neuron(position);
			
		}  catch (NeuralNetworkException error){
			
			throw new NeuralNetworkException("Some Neural Network Exception!");			
		}

		return neuron;                    

	}

}
