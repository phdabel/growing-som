package org.neuroph.contrib.nnet.comp.neuron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neuroph.contrib.core.input.EuclidianDistance;
import org.neuroph.contrib.util.Position;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;

/**
 * 
 * @author ABEL CORREA
 *
 */
public class MappedCompetitiveNeuron extends CompetitiveNeuron {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		
	/**
	 *  list of spatial points of neuron
	 *  e.g.:
	 *  position = {3, 4} 
	 *  neuron may be in a bidimensional grid with coordinates x = 3 and y = 4
	 */
	private Position position = null; 
	
	/**
	 *  extra parameter.
	 *  it can be used to define the importance of some feature
	 *  of input space
	 *  
	 *  inputFunction WeightedEuclidianDistance uses this parameter as weight
	 *  of each dimension of problem
	 */
	private Double featureWeight = null;
	
	/**
	 *  hit counter of neuron
	 *  increases one when neuron is winner
	 *  
	 */
	private int hits = 0;
	
	/**
	 *  growing node counter
	 *  0 mean it is a initial node
	 */
	private int spreadCounter = 0;
	
	/**
	 *  map of neighbours neurons
	 */
	private List<Position> neighbours = new ArrayList<Position>();
	
	/**
	 * creates an unidimensional neuron
	 * @param inputFunction
	 * @param transferFunction
	 * @param x
	 */
	public MappedCompetitiveNeuron(InputFunction inputFunction,
			TransferFunction transferFunction, int x)
	{
		super(inputFunction, transferFunction);
		Position position = new Position(x);
		this.setPosition(position);
		this.generateID();
	}
	/**
	 * creates a bidimensional neuron
	 * 
	 * @param inputFunction
	 * @param transferFunction
	 * @param x
	 * @param y
	 */
	public MappedCompetitiveNeuron(InputFunction inputFunction,
			TransferFunction transferFunction, int x, int y)
	{
		super(inputFunction, transferFunction);
		Position position = new Position(x,y);
		this.setPosition(position);
		this.generateID();
	}
	/**
	 *  creates a tridimensional neuron
	 *   
	 * @param inputFunction
	 * @param transferFunction
	 * @param x
	 * @param y
	 * @param z
	 */
	public MappedCompetitiveNeuron(InputFunction inputFunction,
			TransferFunction transferFunction, int x, int y, int z)
	{
		super(inputFunction, transferFunction);
		Position position = new Position(x,y,z);
		this.setPosition(position);
		this.generateID();
	}
	
	
	/**
	 * creates a neuron with
	 * multidimensional coordinates
	 *  
	 * @param inputFunction
	 * @param transferFunction
	 * @param coordinates
	 */
	public MappedCompetitiveNeuron(InputFunction inputFunction,
			TransferFunction transferFunction, List<Integer> coordinates)
	{
		super(inputFunction, transferFunction);
		Position position = new Position(coordinates);
		this.setPosition(position);
		this.generateID();
		
	}
	
	/**
	 * initialize neuron with inputFunction and transferFunction
	 * and no position or id
	 * 
	 * @param inputFunction
	 * @param transferFunction
	 */
	public MappedCompetitiveNeuron(InputFunction inputFunction,
			TransferFunction transferFunction)
	{
		super(inputFunction, transferFunction);
		this.generateID();
	}
	
	public MappedCompetitiveNeuron()
	{
		super(new EuclidianDistance(), new Linear());
	}
	
	/**
	 *  makes a generic ID to this neuron
	 *  ID may be the current position of neuron in a grid
	 */
	public void generateID(){
		if(!this.position.getAxisPosition().isEmpty()){
			Iterator<Integer> it = this.position.getAxisPosition().iterator();
			String id = "(";
			while(it.hasNext())
			{
				id = id + it.next();
				if(it.hasNext())
				{
					id = id + ",";
				}
			}
			id = id + ")";
		}else{
			throw new NeurophException("Neuron has no position!");
		}
	}
		
	public void addNeighbour(Position neuron)
	{
		if(this.isValidNeighbour(neuron))
		{
			this.neighbours.add(neuron);
		}
	}
	
	public void removeNeighbour(Position neuron)
	{
		if(this.neighbours.contains(neuron))
			this.neighbours.remove(neuron);
	}
	
	private boolean isValidNeighbour(Position neuron)
	{
		if(this.position.getAxisPosition().size() == neuron.getAxisPosition().size())
		{
			
			if(this.isFreePosition(neuron))
			{
				int neighbourConstraint = 0;
				for(int dim = 0; dim < this.position.getAxisPosition().size(); dim++)
				{
					neighbourConstraint += Math.abs(neuron.getAxisPosition().get(dim) - 
							this.position.getAxisPosition().get(dim));
				}
				if(neighbourConstraint == 1)
					return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void calculate() {
		if (this.isCompeting()) {
			// get input only from neurons in this layer
			//this.netInput = this.inputFunction.getOutput();
		} else {
			// get input from other layers
			this.netInput = this.inputFunction.getOutput(this.getConnectionsFromOtherLayers());
			//System.out.println("Conections from other layers "+this.getConnectionsFromOtherLayers());
			//System.out.println("InputFunction "+this.netInput);
			//this.setError(this.getError() + this.netInput);
			this.setIsCompeting(true);;
		}
		this.output = this.transferFunction.getOutput(this.netInput);
		//outputHistory.add(0, new Double(this.output));
	}
	
	private boolean isFreePosition(Position p){
		if(this.existsFreePosition() && !this.neighbours.contains(p))
			return true;
		return false;
	}
	
	public boolean existsFreePosition(){
		if(this.neighbours.size() < (this.position.getAxisPosition().size() * 2))
			return true;
		return false;
	}


	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}


	public Position[] getNeighbours() {
		return this.neighbours.toArray(new Position[this.neighbours.size()]);
	}
	
	public boolean isPositionDefined(){
		return !this.position.equals(null);
	}
	public Double getFeatureWeight() {
		return featureWeight;
	}
	public void setFeatureWeight(Double featureWeight) {
		this.featureWeight = featureWeight;
	}
	public Integer getHits() {
		return hits;
	}
		
	public void incrementHit()
	{
		this.hits++;
	}
	public Integer getSpreadCounter() {
		return spreadCounter;
	}
	public void setSpreadCounter(int spreadCounter) {
		this.spreadCounter = spreadCounter;
	}
	
	

}
