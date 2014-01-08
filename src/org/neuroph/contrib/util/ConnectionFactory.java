/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.contrib.util;

import java.util.Random;

import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;


/**
 *
 * @author sim
 */
public class ConnectionFactory extends org.neuroph.util.ConnectionFactory {
    
    public ConnectionFactory() {
        super();
    }
    
    /**
	 * Creates full connectivity between the two specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void fullConnect(Layer fromLayer, DimensionalGrid<? extends MappedCompetitiveNeuron> toLayer) {
		for(MappedCompetitiveNeuron toNeuron : toLayer.values())
		{
			for(Neuron fromNeuron : fromLayer.getNeurons())
			{
				createConnection(fromNeuron, toNeuron);
			}
			
		}
		
	}
	
	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param fromNeuron
	 *            output neuron
	 * @param toNeuron
	 *            input neuron
	 */
	public static void createConnection(Neuron fromNeuron, MappedCompetitiveNeuron toNeuron) {
		Connection connection = new Connection(fromNeuron, toNeuron);
		Random r = new Random();
		connection.getWeight().setValue(r.nextDouble());;
		toNeuron.addInputConnection(connection);
	}
	
	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param fromNeuron
	 *            output neuron
	 * @param toNeuron
	 *            input neuron
	 * @param weight
	 * 		      weight of connection
	 */
	public static void createConnection(Neuron fromNeuron, MappedCompetitiveNeuron toNeuron, Double weight) {
		Connection connection = new Connection(fromNeuron, toNeuron);
		connection.getWeight().setValue(weight);;
		toNeuron.addInputConnection(connection);
	}
    
    
    
    

    
    
}
