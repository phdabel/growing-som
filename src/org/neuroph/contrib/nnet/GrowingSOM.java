package org.neuroph.contrib.nnet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neuroph.contrib.core.input.EuclidianDistance;
import org.neuroph.contrib.core.learning.GSOMLearning;
import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.contrib.util.ConnectionFactory;
import org.neuroph.contrib.util.MappedNeuronFactory;
import org.neuroph.contrib.util.Position;
import org.neuroph.contrib.util.WriteFile;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.NeuralNetworkCalculatedEvent;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.transfer.Linear;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;

public class GrowingSOM extends NeuralNetwork<GSOMLearning> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//store network's max error
	private Double hError = 0.0;
	
	//network's spread factor (SF)
	private Double spreadFactor = 0.3;
	
	//factor of distribution of error to the winner's neighbours (FD)
	private Double distribuctionFactor = 0.1;
	
	/**
	 *  counter for new neurons
	 */
	private int spreadCounter = 0;
	
	//network' growth threshold (GT)
	private Double growthThreshold;
	
	//initial learning rate
	private Double initialLearningRate = 0.9;
	/**
     * store properties of grid neurons
     */
    private NeuronProperties neuronProperties;
	
	private DimensionalGrid<MappedCompetitiveNeuron> map;
    //private CompetitiveLayer map;
    private List<Integer> neuronsCount = new ArrayList<Integer>();
    
    /**
	 *  internal use
	 *  don't modify it
	 */
	private transient ArrayList<Integer> pos = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private transient int ct = 0;
	//neuron buffer to initialize weights
	private transient MappedCompetitiveNeuron neuronBuffer = null;
    
    public GrowingSOM(int inputNeuronsCount, List<Integer> mapNeuronsCount)
    {
    	this.setNeuronsCount(mapNeuronsCount);
        this.createNetwork(inputNeuronsCount, mapNeuronsCount);
        this.getMap().initializeError();
        
        this.setGrowthThreshold(-inputNeuronsCount * Math.log(this.getSpreadFactor()));
    }
	
    public void setNeuronsCount(List<Integer> neuronsCount) {
		this.neuronsCount = neuronsCount;
	}
    
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
    	
    	this.neuronProperties = outputNeuronProperties;
    	
    	// set network type
    	this.setNetworkType(NeuralNetworkType.KOHONEN);
    	
    	Layer inLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
    	this.addLayer(inLayer);
    	
    	this.map = new DimensionalGrid<MappedCompetitiveNeuron>(mapNeuronsCount, outputNeuronProperties);
    	//this.map = DimensionalGridFactory.createLayer(mapNeuronsCount, outputNeuronProperties);
    	
    	ConnectionFactory.fullConnect(inLayer, this.map);
    	//this.addLayer(this.map);
    	    	
    	// set network input and output cells
    	this.setDefaultIO();

    	
    	this.setLearningRule();
    	
    	
    }
    
	private void setLearningRule(){
    	GSOMLearning k = new GSOMLearning();
    	k.setLearningRate(this.getInitialLearningRate());
    	k.setNeuralNetwork(this);
    	k.setIterations(50, 0);
    	this.setLearningRule(k);
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
	
	public List<Integer> getNeuronsCount() {
		return neuronsCount;
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
	
	
	public void testMode()
	{
		this.getLayerAt(0).calculate();
		this.getMap().calculate();
	}
	
	/**
	 *  performs smoothing phase
	 */	
	public void smoothingPhase()
	{
		this.getLayerAt(0).calculate();
        this.getMap().calculate();
        this.getMap().getWinner().setError(this.getMap().getWinner().getError() + this.getMap().getWinner().getOutput());
        //this.getMap().getWinner().incrementHit();
        if(this.getMap().getWinner().getError() > this.getMaxError())
        {
        	this.setMaxError(this.getMap().getWinner().getError());
        }
        String s = "";
		for(Integer i : this.getMap().getWinner().getPosition().getAxisPosition())
		{
			s += i.toString()+";";
		}
		WriteFile.getInstance().write(s);
		WriteFile.getInstance().write(this.getMap().getWinner().getHits().toString()+";");
		WriteFile.getInstance().write(this.getMap().getWinner().getSpreadCounter().toString()+";");
        WriteFile.getInstance().write("F;");
        WriteFile.getInstance().nLine();
        fireNetworkEvent(new NeuralNetworkCalculatedEvent(this));
	}
	
	/**
     * Performs calculation on whole network
     */
    public void calculate() {
    	
        this.getLayerAt(0).calculate();
        this.getMap().calculate();
        this.getMap().getWinner().setError(this.getMap().getWinner().getError() + this.getMap().getWinner().getOutput());
        this.getMap().getWinner().incrementHit();
        if(this.getMap().getWinner().getError() > this.getMaxError())
        {
        	this.setMaxError(this.getMap().getWinner().getError());
        }
        String s = "";
		for(Integer i : this.getMap().getWinner().getPosition().getAxisPosition())
		{
			s += i.toString()+";";
		}
		WriteFile.getInstance().write(s);
		WriteFile.getInstance().write(this.getMap().getWinner().getHits().toString()+";");
		WriteFile.getInstance().write(this.getMap().getWinner().getSpreadCounter().toString()+";");
        /**
         * growing nodes condition
         */
        if(this.getMaxError() > this.getGrowthThreshold())
        {
        	/**
        	 * grow new nodes
        	 */
        	if(this.getMap().getWinner().existsFreePosition())
        	{
        		WriteFile.getInstance().write("T;");
        		//WriteFile.write(bw, "Input: "+this.getMap().getWinner().getPosition().getAxisPosition());
        		//System.out.println("growing nodes");
        		try {
					this.spreadNeuron(this.getMap().getWinner());
					this.getLearningRule().setLearningRate(this.initialLearningRate);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
        	}
        	/**
        	 *  divide error
        	 */
        	else{
        		WriteFile.getInstance().write("F;");
        		//System.out.println("dividing error");
        		this.getMap().getNeuronAt(this.getMap().getWinner().getPosition()).setError(this.getGrowthThreshold() / 2);
        		for(Position p : this.getMap().getWinner().getNeighbours())
        		{
        			Double e = this.getMap().getNeuronAt(p).getError();
        			this.getMap().getNeuronAt(p).setError(e * this.getDistribuctionFactor() + e);
        		}
        	}
        }
        WriteFile.getInstance().nLine();
        fireNetworkEvent(new NeuralNetworkCalculatedEvent(this));
    }
    
    /**
     * grow new neurons from the parameter neuron
     * @param neuron
     * @throws CloneNotSupportedException
     */
    public void spreadNeuron(MappedCompetitiveNeuron neuron) throws CloneNotSupportedException
	{
    	this.increaseSpreadCounter();
		for(int d = 0; d < this.getMap().getTotalDimensions(); d++)
        {
             
            Position position1 = neuron.getPosition().clone();
            Position position2 = neuron.getPosition().clone();
             
            Integer newPos1 = new Integer(position1.getAxisPosition().get(d).intValue());
            Integer newPos2 = new Integer(position2.getAxisPosition().get(d).intValue());
             
            newPos1++;
            newPos2--;
             
            position1.getAxisPosition().set(d, newPos1);
            position2.getAxisPosition().set(d, newPos2);
            /*System.out.println("neuron "+neuron.getPosition().getAxisPosition());
            System.out.println("position1 "+position1.getAxisPosition());
            System.out.println("position2 "+position2.getAxisPosition());*/
            if(!this.getMap().containsKey(position1)){
            	this.growNeuron(position1, neuron);
            }
            if(!this.getMap().containsKey(position2)){
                this.growNeuron(position2, neuron);
            }
        }
		MappedCompetitiveNeuron[] outputNeurons = this.map.values().toArray(new MappedCompetitiveNeuron[this.map.size()]); 
		this.setOutputNeurons(outputNeurons);
	}
    
    
	private void growNeuron(Position p, MappedCompetitiveNeuron winner)
	{
		MappedCompetitiveNeuron neuron = (MappedCompetitiveNeuron)MappedNeuronFactory.createNeuron(this.neuronProperties, p);
		neuron.setSpreadCounter(this.getSpreadCounter());
        this.getMap().put(p, neuron);
        try {
            this.getMap().lookupNeighborhood(neuron);
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
                
        for(Neuron fromNeuron : this.getLayers()[0].getNeurons())
		{
        	Double w = this.initializeWeights(neuron, winner, fromNeuron);
			ConnectionFactory.createConnection(fromNeuron, neuron, w);
		}
        this.neuronBuffer = null;
	}
    
    private Double initializeWeights(MappedCompetitiveNeuron newNeuron, MappedCompetitiveNeuron oldNeuron, Neuron inConnection)
    {
    	Double w = 0.0;
    	
    	if(newNeuron.getNeighbours().length > 1)
    	{
    		for(Position p : newNeuron.getNeighbours())
    		{
    			w += this.getMap().getNeuronAt(p).getConnectionFrom(inConnection).getWeight().value;
    		}
    		w = w / newNeuron.getNeighbours().length;
    	}
    	else if(newNeuron.getNeighbours().length == 1 && oldNeuron.getNeighbours().length > 1){
    			if(this.neuronBuffer == null)
    			{
    				Random r = new Random();
    				int bP = r.nextInt((oldNeuron.getNeighbours().length - 1));
    				this.neuronBuffer = this.getMap().getNeuronAt(oldNeuron.getNeighbours()[bP]);
    			}
    			Double w1 = oldNeuron.getConnectionFrom(inConnection).getWeight().value;
    			Double w2 = this.neuronBuffer.getConnectionFrom(inConnection).getWeight().value; 
    			if(w1 > w2)
    			{
    				w = w1 + (w1 - w2);
    			}else if(w2 > w1)
    			{
    				w = w1 - (w2 - w1);
    			}else{
    				w = w1;
    			}
    	}else if(newNeuron.getNeighbours().length == 1 && oldNeuron.getNeighbours().length == 1)
    	{
    		Double minW = Double.MAX_VALUE;
    		Double maxW = Double.MIN_VALUE;
    		for(MappedCompetitiveNeuron neurons : this.getMap().values())
    		{
    			if(!neurons.equals(newNeuron)){
	    			Double wn = neurons.getConnectionFrom(inConnection).getWeight().value;
	    			if(wn < minW)
	    			{
	    				minW = wn;
	    			}
	    			if(wn > maxW)
	    			{
	    				maxW = wn;
	    			}
    			}
    		}
    		w = (minW + maxW) / 2;
    	}
    	
    	return w;
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
        
        ((GSOMLearning)this.getLearningRule()).learn(trainingSet);
    }
    
    public DimensionalGrid<MappedCompetitiveNeuron> getMap() {
		return map;
	}
	
	public Double getMaxError() {
		return hError;
	}
	public void setMaxError(Double maxError) {
		this.hError = maxError;
	}
	public Double getSpreadFactor() {
		return spreadFactor;
	}
	public void setSpreadFactor(Double spreadFactor) {
		this.spreadFactor = spreadFactor;
	}
	public Double getGrowthThreshold() {
		return growthThreshold;
	}
	public void setGrowthThreshold(Double growthThreshold) {
		this.growthThreshold = growthThreshold;
	}

	public Double getInitialLearningRate() {
		return initialLearningRate;
	}

	public void setInitialLearningRate(Double initialLearningRate) {
		this.initialLearningRate = initialLearningRate;
	}
	

	public int getSpreadCounter() {
		return spreadCounter;
	}

	public void increaseSpreadCounter()
	{
		this.spreadCounter++;
	}

	public Double getDistribuctionFactor() {
		return distribuctionFactor;
	}

	public void setDistribuctionFactor(Double distribuctionFactor) {
		this.distribuctionFactor = distribuctionFactor;
	}

}
