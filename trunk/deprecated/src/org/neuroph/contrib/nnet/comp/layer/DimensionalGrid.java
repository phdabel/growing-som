package org.neuroph.contrib.nnet.comp.layer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.contrib.util.GridType;
import org.neuroph.contrib.util.MappedNeuronFactory;
import org.neuroph.contrib.util.Position;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.util.NeuronProperties;

/**
 * 
 * @author ABEL CORREA
 *
 * @param <N>
 */
public class DimensionalGrid<N extends MappedCompetitiveNeuron> extends HashMap<Position, N> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
     * Label for this layer
     */
    private String label;
    
    /**
	 * Max iterations for neurons to compete
	 * This is neccesery to limit, otherwise if there is no winner there will
	 * be endless loop.
	 */
	private int maxIterations = 50;
	
	/**
	 * The competition winner for this layer
	 */
	private N winner;
    
    /**
     *  total of dimensions of grid
     *  e.g. in Kohonen SelfOrganizingMaps totalDimensions = 2
     */
    private Integer totalDimensions = 0;
    
    /**
     *  Dimensional Grid Types:
     *  1 - Static Grid
     *    - static grids have pre-defined number of neurons
     *  2 - Incremental Grid
     *    - in incremental grids new neurons may appears when
     *    - existing neurons are not enough to represent the 
     *    - feature map
     */
    private GridType typeGrid = GridType.STATIC;
	
	/**
	 * Reference to parent neural network
	 */
	private NeuralNetwork<? extends LearningRule> parentNetwork;
	
	//public Map<Position, N> dimension = new HashMap<Position, N>();
	
	/**
     *  internal use
     *  don't modify it
     */
    private transient static ArrayList<Integer> pos = new ArrayList<Integer>();
    private transient static int ct = 0;
	
	
	public NeuralNetwork<? extends LearningRule> getParentNetwork() {
		return parentNetwork;
	}

	public void setParentNetwork(NeuralNetwork<? extends LearningRule> parentNetwork) {
		this.parentNetwork = parentNetwork;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	/**
	 * return a array of Neurons
	 * 
	 * @return
	 
	@SuppressWarnings("unchecked")
	public MappedCompetitiveNeuron[] getNeurons(){
		return this.dimension.values().toArray(new MappedCompetitiveNeuron[this.dimension.values().size()]);
	}*/
	
	public DimensionalGrid(List<Integer> dimensions, NeuronProperties neuronProperties)
	{
		super();
		this.setTotalDimensions(dimensions.size());
		this.mountPosition(dimensions, 0, neuronProperties);
		
	}
		
	
	/**
	 * adds mapped neighbors position of a neuron
	 * 
	 * @param neuron
	 * @throws CloneNotSupportedException 
	 */
	public void lookupNeighborhood(N neuron) throws CloneNotSupportedException
	{
		for(int d = 0; d < getTotalDimensions(); d++)
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
	
	private void deleteNeighborhood(N neuron)
	{
		for(int d = 0; d < this.totalDimensions; d++)
		{
			Position position1 = neuron.getPosition();
			Position position2 = neuron.getPosition();
			position1.getAxisPosition().set(d, (position1.getAxisPosition().get(d) - 1));
			position2.getAxisPosition().set(d, (position1.getAxisPosition().get(d) + 1));
			if(this.isMapped(position1))
				this.get(position1).removeNeighbour(neuron.getPosition());
			if(this.isMapped(position2))
				this.get(position2).removeNeighbour(neuron.getPosition());	
		}
		
	}
	
	/**
	 * adds a neuron newNeuron in the dimensional space
	 * looking up for neighbor neurons
	 * 
	 * @param newNeuron
	 *//*
	public void addNeuron(N newNeuron)
	{
		// prevent adding null neurons
        if (newNeuron == null) { 
            throw new NeurophException("Neuron cant be null!");
        }
        
        if(newNeuron instanceof MappedCompetitiveNeuron)
        {
        	
        	if(!this.isMapped(newNeuron) && newNeuron.isPositionDefined())
        	{
        		this.put(newNeuron.getPosition(), newNeuron);
        		try {
					this.lookupNeighborhood(newNeuron);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		//this.addNeuron(newNeuron);
        	}
        }else{
        	throw new NeurophException("Neuron must extend MappedCompetitiveNeuron!");
        }
        
	}*/
	
	/**
	 * Removes neuron from grid
	 * 
	 * @param neuron
	 *            neuron to remove
	 */
	public void removeNeuron(N neuron) {
		if(neuron == null)
			throw new NeurophException("Neuron can't be null!");
		this.deleteNeighborhood(neuron);
		this.remove(neuron.getPosition());
	}
	
	/**
	 * performs calculation for all neurons in the grid
	 */
	public void calculate(){
		boolean hasWinner = false;
		int iterationsCount = 0;
		while(!hasWinner)
		{
			int firingNeurons = 0;
			for(N neuron : this.values())
			{
				neuron.calculate();
				
				if(neuron.getOutput() > 0)
					firingNeurons++;
			}
			if(iterationsCount > this.maxIterations) break;
			
			if(firingNeurons > 1)
			{
				hasWinner = true;
			}
			
			iterationsCount++;
		}
		if(hasWinner)
		{
			double maxOutput = Double.MIN_VALUE;
			
			for(N neuron: this.values())
			{
				N cNeuron = neuron;
				cNeuron.setIsCompeting(false);
				if(cNeuron.getOutput() > maxOutput)
				{
					maxOutput = cNeuron.getOutput();
					this.setWinner(cNeuron);
				}
			}
		}
		
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
    private boolean isMapped(MappedCompetitiveNeuron neuron)
    {
        if(neuron == null)
            throw new NeurophException("Neuron can't be null!");
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
            throw new NeurophException("Position can't be null!");
        return this.containsKey(position);
    }

	public GridType getTypeGrid() {
		return typeGrid;
	}

	public void setTypeGrid(GridType typeGrid) {
		this.typeGrid = typeGrid;
	}

	public Integer getTotalDimensions() {
		return totalDimensions;
	}

	public void setTotalDimensions(Integer totalDimensions) {
		this.totalDimensions = totalDimensions;
	}
	
	
	public void setNeuron(Position index, N neuron) {
		
		if(!this.isMapped(index))
			throw new NeurophException("Specified position don't contains a neuron");
        
		if(neuron == null)
        	throw new NeurophException("Neuron can't be null!");

		this.put(index, neuron);
		try {
			this.lookupNeighborhood(neuron);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public MappedCompetitiveNeuron getWinner() {
		return winner;
	}

	public void setWinner(N winner) {
		this.winner = winner;
	}
	
	/**
	 * Resets the activation and input levels for all neurons in this layer
	 */
	public void reset() {
		for(N neuron : this.values()) {
			neuron.reset();
		}		
	}

	/**
	 * Randomize input connection weights for all neurons in this layer
	 */
	public void randomizeWeights() {
		for(N neuron : this.values()) {
			neuron.randomizeWeights();
		}
	}


	/**
	 * Randomize input connection weights for all neurons in this layer
         * within specified value range
	 */
	public void randomizeWeights(double minWeight, double maxWeight) {
		for(N neuron : this.values()) {
			neuron.randomizeWeights(minWeight, maxWeight);
		}
	}
        
     /**
      * Initialize connection weights for all neurons in this layer using a
      * random number generator
      *
      * @param generator the random number generator
      */
     public void randomizeWeights(Random generator) {
           for(N neuron : this.values()) {
                neuron.randomizeWeights(generator);
           }
     }        

     /**
      * Initialize connection weights for the whole layer to to specified value
      * 
      * @param value the weight value
      */
     public void initializeWeights(double value) {
           for(N neuron : this.values()) {
               neuron.initializeWeights(value);
           }
     }
     
     public void initializeError()
     {
    	 for(N neuron : this.values())
    	 {
    		 neuron.setError(0.0);
    	 }
     }
     
     @SuppressWarnings("unchecked")
	public void mountPosition(List<Integer> dim, int d, NeuronProperties neuronProperties)
 	{
    	 for(int i = 0; i < dim.get(d); i++)
         {
             if(d+1 < dim.size())
             {
                 pos.add(i);
                 mountPosition(dim, d+1, neuronProperties);
                 pos.remove(pos.lastIndexOf(i));
             }else{
                 pos.add(i);
                 Position p = new Position(pos);
                 N neuron = (N)MappedNeuronFactory.createNeuron(neuronProperties, p);
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
	
	

}
