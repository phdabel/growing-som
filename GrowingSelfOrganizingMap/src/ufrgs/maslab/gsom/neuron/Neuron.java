package ufrgs.maslab.gsom.neuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ufrgs.maslab.gsom.util.Position;

public class Neuron {
	
	private Position position;
	
	public int hits = 0;
	
	public double[] weights;
	
	public boolean spread = false;
	
	public int cluster = 0;
	
	public double[] attributeError;
	
	public double[] attributeValue;
	
	public double totalError = 0.0;
	
	public double error = 0.0;
	
	public int spreadCounter = 0;
	
	public Map<Position, Double> distanceBetweenNodes = new HashMap<Position, Double>();
	
	public Map<Position, Double> distanceBetweenJunctions = new HashMap<Position, Double>();
	
	public ArrayList<String> label = new ArrayList<String>();
	
	/**
	 *  map of neighbours neurons
	 */
	private List<Position> neighbours = new ArrayList<Position>();

	public Neuron(Position p)
	{
		this.setPosition(p);
	}
	
	public void setCluster(int c)
	{
		this.cluster = c;
	}
	
	@Override
	public String toString()
	{
		if(this.hits != 0){
			return this.getPosition().getAxisPosition().toString() + " (" + this.hits + ") ";
		}else{
			return this.getPosition().getAxisPosition().toString();
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
	
	
	public double[] getAttributeError() {
		return attributeError;
	}

	public void setAttributeError(double[] attributeError) {
		this.attributeError = attributeError;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public int getSpreadCounter() {
		return spreadCounter;
	}

	public void setSpreadCounter(int spreadCounter) {
		this.spreadCounter = spreadCounter;
	}

	public List<Position> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<Position> neighbours) {
		this.neighbours = neighbours;
	}
	
	@Override
	public int hashCode()
	{
		final int  HASH_OFFSET = 17;
		int hash = 1;
		for(int dim = 0; dim < this.getPosition().getAxisPosition().size(); dim++)
		{
			hash = ((hash * HASH_OFFSET) + this.getPosition().getAxisPosition().get(dim));
		}
		return hash + this.getPosition().hashCode();
	}
	
	@Override
    public boolean equals(Object other) 
    {
        if (this == other)
          return true;

        if (!(other instanceof Neuron))
          return false;

        Neuron otherPoint = (Neuron) other;
        
        return otherPoint.getPosition() == this.getPosition();
    }
	

}
