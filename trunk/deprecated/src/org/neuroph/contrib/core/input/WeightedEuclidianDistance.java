package org.neuroph.contrib.core.input;

import java.io.Serializable;

import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.InputFunction;

public class WeightedEuclidianDistance extends InputFunction implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double getOutput(Connection[] inputConnections) {
		
		double sum = 0d;
		double output = 0d;
		for(Connection connection : inputConnections) {
			
			Neuron neuron = connection.getFromNeuron();
			double featureWeight = ((MappedCompetitiveNeuron)neuron).getFeatureWeight();
			Weight weight = connection.getWeight();
			double diff = Math.pow((neuron.getOutput() - weight.getValue()),2);
            sum += diff * featureWeight;
		}

        output = Math.sqrt(sum);
                
		return output;
	}
}
