package ufrgs.maslab.gsom.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.neuroph.core.data.DataSet;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.NeuronFactory;
import ufrgs.maslab.gsom.util.Position;
import ufrgs.maslab.gsom.util.Transmitter;

public class CompetitiveLayer<N extends Neuron> extends HashMap<Position, N> implements Serializable {
	
private static final long serialVersionUID = -2770439268611932205L;
	
	/**
     *  internal use
     *  don't modify it
     */
    private transient static ArrayList<Integer> pos = new ArrayList<Integer>();
    private transient static int ct = 0;
    /**
     * 
     */
	
	private DataSet input;
	
	private DataSet test;
	
	private DataSet initializationDataSet;
	
	private transient Position winner;
	
	private int dimensions;
	
	private int inputCount;
	
	private int initialMapSize;
	
	private int mapSize;
	
	private Double growthThreshold;
	
	private Double spreadFactor = Transmitter.getDoubleConfigParameter("gsom.properties", "growth.threshold");
	
	private int initialSpreadCounter = 0;
	
	
	public CompetitiveLayer(DataSet input, List<Integer> dimensions)
	{
		super();
		
		if(input.getRows().size() == 0)
			throw new NeuralNetworkException("input can't be null");
		if(dimensions.size() == 0)
			throw new NeuralNetworkException("dimensions can't null");
		
		this.setInput(input);
		this.setInputCount(input.getInputSize());
		this.setDimensions(dimensions.size());
		this.initialMapSize(dimensions);
		this.mapSize(this.initialMapSize);
		this.mountPosition(dimensions, 0);
		this.randomizeWeightsAndError();
		this.setGrowthThreshold(-this.inputCount * Math.log(this.getSpreadFactor()));
	}
	
	public void mapSize(int size)
	{
		this.mapSize = size;
	}
	
	private void initialMapSize(List<Integer> dimensions)
	{
		int countMap = 1;
		for(Integer n : dimensions)
		{
			countMap = n * countMap;
		}
		this.setInitialMapSize(countMap);
	}
	
	
	@SuppressWarnings("unchecked")
	public void mountPosition(List<Integer> dim, int d)
 	{
    	 for(int i = 0; i < dim.get(d); i++)
         {
             if(d+1 < dim.size())
             {
                 pos.add(i);
                 mountPosition(dim, d+1);
                 pos.remove(pos.lastIndexOf(i));
             }else{
                 pos.add(i);
                 Position p = new Position(pos);
                 N neuron = (N)NeuronFactory.createNeuron(p);
                 
                 neuron.setWeights(new double[this.inputCount]);
                 neuron.setAttributeError(new double[this.inputCount]);
                 neuron.attributeValue = new double[this.inputCount];
                 neuron.spreadCounter = this.initialSpreadCounter;
                 this.initialSpreadCounter++;
                 this.put(neuron.getPosition(), neuron);
                 try {
                     this.lookupNeighborhood(neuron);
                 } catch (CloneNotSupportedException e) {
                     System.out.println(e.getMessage());
                     e.printStackTrace();
                 }
                 pos.remove(pos.lastIndexOf(i));
                 ct++;
             }
         }
    	
 	}
	
	/**
     *  remove useless neurons
     */
    public void cleanNetwork(){
    	ArrayList<N> deletePositions = new ArrayList<N>();
    	for(N neurons : this.values())
    	{
    		if(neurons.hits == 0 && neurons.getNeighbours().size() <= 1){
    			deletePositions.add(neurons);
    		}else if(neurons.error > -1 && neurons.error < 1){
    			deletePositions.add(neurons);    			
    		}
    	}
    	/*for(N neurons : this.values())
		{
    		if(neurons.spreadCounter > 3){
    			if(neurons.getNeighbours().isEmpty())
    			{
    				deletePositions.add(neurons);	
    			}
    			else if((neurons.error > -1 && neurons.error < 1) && neurons.getNeighbours().size() > 1)
	    		{
	    			deletePositions.add(neurons);
	    		}else if(neurons.hits == 0 && neurons.getNeighbours().size() == 1)
	    		{
	    			deletePositions.add(neurons);
	    		}
    		}
		}*/
    	for(N n : deletePositions)
    	{
    		this.removeNeuron(n);
    	}
    	
    }
	
	/**
	 * adds mapped neighbors position of a neuron
	 * 
	 * @param neuron
	 * @throws CloneNotSupportedException 
	 */
	public void lookupNeighborhood(N neuron) throws CloneNotSupportedException
	{
		for(int d = 0; d < getDimensions(); d++)
        {
             
            Position position1 = neuron.getPosition().clone();
            Position position2 = neuron.getPosition().clone();
             
            Integer newPos1 = new Integer(position1.getAxisPosition().get(d).intValue());
            Integer newPos2 = new Integer(position2.getAxisPosition().get(d).intValue());
             
            newPos1++;
            newPos2--;
             
            position1.getAxisPosition().set(d, newPos1);
            position2.getAxisPosition().set(d, newPos2);
             
            if(isMapped(position1)){
                this.get(neuron.getPosition()).addNeighbour(position1);
                this.get(position1).addNeighbour(neuron.getPosition());
                
            }
            if(isMapped(position2)){
                this.get(neuron.getPosition()).addNeighbour(position2);
                this.get(position2).addNeighbour(neuron.getPosition());
                
            }
        }
	}
	
	private void deleteNeighborhood(N neuron) throws CloneNotSupportedException
	{
		for(int d = 0; d < this.getDimensions(); d++)
		{
			Position position1 = neuron.getPosition().clone();
			Position position2 = neuron.getPosition().clone();
			
			Integer newPos1 = new Integer(position1.getAxisPosition().get(d).intValue());
            Integer newPos2 = new Integer(position2.getAxisPosition().get(d).intValue());
             
            newPos1++;
            newPos2--;
            
            position1.getAxisPosition().set(d, newPos1);
            position2.getAxisPosition().set(d, newPos2);
			

			if(this.isMapped(position1))
				this.get(position1).removeNeighbour(neuron.getPosition());
			if(this.isMapped(position2))
				this.get(position2).removeNeighbour(neuron.getPosition());	
		}
		
	}
	
	/**
	 * Randomize input connection weights for all neurons in this layer
	 */
	public void randomizeWeightsAndError() {
		Random r = new Random();
		for(N neuron : this.values()) {
			if(neuron.getAttributeError() == null || neuron.getAttributeError().length == 0)
				throw new NeuralNetworkException("Input is not defined");
			if(neuron.getWeights() == null || neuron.getWeights().length == 0)
				throw new NeuralNetworkException("Input is not defined");
			if(neuron.attributeValue == null || neuron.attributeValue.length == 0)
				throw new NeuralNetworkException("Input is not defined");
			
			for(int i = 0; i < this.inputCount; i++)
			{
				neuron.getAttributeError()[i] = 0d;
				neuron.attributeValue[i] = 0d;
				neuron.getWeights()[i] = r.nextDouble();
			}
		}
	}
	
	/**
	 * Removes neuron from grid
	 * 
	 * @param neuron
	 *            neuron to remove
	 */
	public void removeNeuron(N neuron) {
		if(neuron == null)
			throw new NeuralNetworkException("Neuron can't be null!");
		try {
			this.deleteNeighborhood(neuron);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.remove(neuron.getPosition());
	}

	public DataSet getInput() {
		return input;
	}
	
	/**
	 * Returns neuron at specified index position in the grid
	 * 
	 * @param index
	 *            neuron index position
	 * @return neuron at specified index position
	 */
	public N getNeuronAt(Position index) {
		return this.get(index);
	}
	
	/**
	 * Returns number of neurons in this layer
	 * 
	 * @return number of neurons in this layer
	 */
	public int getNeuronsCount() {
		return this.size();
	}
			
	 /**
     * tests if a given neuron was mapped in the 
     * dimensional space
     * 
     * @param neuron
     * @return
     */
    @SuppressWarnings("unused")
    private boolean isMapped(N neuron)
    {
        if(neuron == null)
            throw new NeuralNetworkException("Neuron can't be null!");
        return this.containsKey(neuron.getPosition());
         
    }
     
    /**
     * test if a position has neuron mapped
     * @param position
     * @return
     */
    public boolean isMapped(Position position)
    {
        if(position == null)
            throw new NeuralNetworkException("Position can't be null!");
        return this.containsKey(position);
    }

	public void setInput(DataSet input) {
		this.input = input;
	}


	public int getDimensions() {
		return dimensions;
	}


	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}


	public int getInputCount() {
		return inputCount;
	}


	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public int getInitialMapSize() {
		return initialMapSize;
	}

	public void setInitialMapSize(int initialMapSize) {
		this.initialMapSize = initialMapSize;
	}

	public Position getWinner() {
		return winner;
	}

	public void setWinner(Position winner) {
		this.winner = winner;
	}

	public Double getGrowthThreshold() {
		return growthThreshold;
	}

	public void setGrowthThreshold(Double growthThreshold) {
		this.growthThreshold = growthThreshold;
	}

	public Double getSpreadFactor() {
		return spreadFactor;
	}

	public void setSpreadFactor(Double spreadFactor) {
		this.spreadFactor = spreadFactor;
	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	public DataSet getInitializationDataSet() {
		return initializationDataSet;
	}

	public void setInitializationDataSet(DataSet initializationDataSet) {
		this.initializationDataSet = initializationDataSet;
	}

	public DataSet getTest() {
		return test;
	}

	public void setTest(DataSet test) {
		this.test = test;
	}

}
