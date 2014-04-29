/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.contrib.nnet;

import java.util.ArrayList;
import java.util.List;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.NeuralNetworkCalculatedEvent;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.transfer.Linear;
import org.neuroph.contrib.core.input.EuclidianDistance;
import org.neuroph.contrib.core.learning.SOMKohonenLearning;
import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.contrib.util.ConnectionFactory;
import org.neuroph.contrib.util.Position;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;

/**
 *
 * @author Abel Correa
 */
@SuppressWarnings("rawtypes")
public class SelfOrganizingMap extends NeuralNetwork{
    
    /**
    * The class fingerprint that is set to indicate serialization
    * compatibility with a previous version of the class.
    */	
    private static final long serialVersionUID = 1L;
    
    private DimensionalGrid<? extends MappedCompetitiveNeuron> map;
    //private CompetitiveLayer map;
    private List<Integer> neuronsCount = new ArrayList<Integer>();
    
    
    /**
	 *  internal use
	 *  don't modify it
	 */
	private transient ArrayList<Integer> pos = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private transient int ct = 0;
    
    public SelfOrganizingMap(int inputNeuronsCount, List<Integer> mapNeuronsCount)
    {
    	this.setNeuronsCount(mapNeuronsCount);
        this.createNetwork(inputNeuronsCount, mapNeuronsCount);
    }
 
    @SuppressWarnings("unchecked")
	private void createNetwork(int inputNeuronsCount, List<Integer> mapNeuronsCount)
    {
    	
    	// specify input neuron properties (use default: weighted sum input with
    	// linear transfer)
    	NeuronProperties inputNeuronProperties = new NeuronProperties();

    	// specify map neuron properties
    	NeuronProperties outputNeuronProperties = new NeuronProperties(
    	                                            MappedCompetitiveNeuron.class,        // neuron type
    	                                            EuclidianDistance.class,   // input function
    	                                            Linear.class       // transfer function
    	                                            );
    	// set network type
    	this.setNetworkType(NeuralNetworkType.KOHONEN);
    	
    	Layer inLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
    	this.addLayer(inLayer);
    	
    	this.map = new DimensionalGrid(mapNeuronsCount, outputNeuronProperties);
    	//this.map = DimensionalGridFactory.createLayer(mapNeuronsCount, outputNeuronProperties);
    	
    	ConnectionFactory.fullConnect(inLayer, this.map);
    	//this.addLayer(this.map);
    	    	
    	// set network input and output cells
    	this.setDefaultIO();

    	
    	this.setLearningRule();
    	
    	
    }
    
    private void setLearningRule(){
    	SOMKohonenLearning k = new SOMKohonenLearning();
    	k.setLearningRate(0.5);
    	k.setNeuralNetwork(this);
    	k.setIterations(100, 0);
    	this.setLearningRule(k);
    }
    
    /**
     * Learn the specified training set
     *
     * @param trainingSet set of training elements to learn
     */
    public void learn(DataSet trainingSet) {
        if (trainingSet == null) {
            throw new NeurophException("Training set is null!");
        }
        
        ((SOMKohonenLearning)this.getLearningRule()).learn(trainingSet);
    }
    
    /**
     * Performs calculation on whole network
     */
    public void calculate() {
    	
        this.getLayerAt(0).calculate();
        this.map.calculate();
        
        fireNetworkEvent(new NeuralNetworkCalculatedEvent(this));
    }
    	
	public List<Integer> getNeuronsCount() {
		return neuronsCount;
	}

	public void setNeuronsCount(List<Integer> neuronsCount) {
		this.neuronsCount = neuronsCount;
	}
	
	public void showTwoDimensionalGrid()
	{
		this.ct = 0;
		this.showTwoDimensionalGrid(0);
	}
	
	
	
	private void showTwoDimensionalGrid(int d)
 	{
 		
 		for(int i = 0; i < this.getNeuronsCount().get(d); i++)
 		{
 			if((d+1) < this.getNeuronsCount().size())
 			{
 				this.pos.add(i);
 				this.showTwoDimensionalGrid(d+1);
 				System.out.println("");
 				this.pos.remove(this.pos.lastIndexOf(i));
 			}else{
 				this.pos.add(i);
 				Position p = new Position(this.pos);
 				this.getNeuronMapOutput(p);
 				this.pos.remove(this.pos.lastIndexOf(i));
 				this.ct++;
 			}
 		}
 	}
	
	private void getNeuronMapOutput(Position position)
	{
		for(Neuron n : this.getOutputNeurons())
		{
			if(((MappedCompetitiveNeuron)n).getPosition().equals(position))
			{
				System.out.print("   "+((MappedCompetitiveNeuron)n).getPosition().getAxisPosition()+" - "+((MappedCompetitiveNeuron)n).getOutput());
			}
		}
	}
	
	/**
	 * Sets default input and output neurons for network (first layer as input,
	 * last as output)
	 */
	public void setDefaultIO() {
		ArrayList<Neuron> inputNeuronsList = new ArrayList<>();
		Layer firstLayer = this.getLayerAt(0);
		for (Neuron neuron : firstLayer.getNeurons() ) {
			if (!(neuron instanceof BiasNeuron)) {  // dont set input to bias neurons
				inputNeuronsList.add(neuron);
			}
		}

		Neuron[] inputNeurons = new Neuron[inputNeuronsList.size()];
		inputNeurons = inputNeuronsList.toArray(inputNeurons);
		MappedCompetitiveNeuron[] outputNeurons = this.map.values().toArray(new MappedCompetitiveNeuron[this.map.size()]); 
		this.setInputNeurons(inputNeurons);
		this.setOutputNeurons(outputNeurons);

	}
	
	public DimensionalGrid getMap() {
		return map;
	}
	
    

    
}
