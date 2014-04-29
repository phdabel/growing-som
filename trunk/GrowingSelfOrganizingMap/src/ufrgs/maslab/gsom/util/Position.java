package ufrgs.maslab.gsom.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;


/**
 * 
 * @author ABEL CORREA
 *
 */
public class Position implements Comparable<Position>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Integer> axisPosition = new ArrayList<Integer>();
	private Integer dimension = 0;
	
	public Position(){
				
	}
	
	public Position(int x)
	{
		if(this.getAxisPosition().isEmpty()){
			this.getAxisPosition().add(x);
			this.setDimension(this.getAxisPosition().size());
		}
	}
	
	public Position(int x, int y)
	{
		if(this.getAxisPosition().isEmpty())
		{
			this.getAxisPosition().add(x);
			this.getAxisPosition().add(y);
			this.setDimension(this.getAxisPosition().size());
		}
	}
	
	public Position(int x, int y, int z)
	{
		if(this.getAxisPosition().isEmpty())
		{
			this.getAxisPosition().add(x);
			this.getAxisPosition().add(y);
			this.getAxisPosition().add(z);
			this.setDimension(this.getAxisPosition().size());
		}
	}
	
	public Position(List<Integer> axes)
	{
		this.getAxisPosition().addAll(axes);
		this.setDimension(this.getAxisPosition().size());
	}
	
	
	
	@Override
	public int compareTo(Position p) {
		int difference = 0;
		if(this.getDimension() == p.getDimension())
		{
			for(int d = 0; d < this.getDimension(); d++)
			{
				difference += (p.getAxisPosition().get(d) - this.getAxisPosition().get(d));
			}
		}else{
			throw new NeuralNetworkException("Positions must be the same dimensions!");
		}
		if(this.equals(p))
		{
			return 0;
		}else if(difference < 0)
		{
			return 1;
		}else if(difference > 0)
		{
			return -1;
		}
		return 1;
		
	}
	
	protected void setDimension(int dim)
	{
		this.dimension = dim;
	}

	public Integer getDimension() {
		return dimension;
	}
	
	public List<Integer> getAxisPosition(){
		return this.axisPosition;
	}
	
	@Override
	public int hashCode()
	{
		final int  HASH_OFFSET = 31;
		int hash = 7;
		for(int dim = 0; dim < this.getDimension(); dim++)
		{
			hash = (hash^(dim)) + ((hash * HASH_OFFSET) + this.getAxisPosition().get(dim));
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getClass() != obj.getClass())
			return false;
		Position other = (Position)obj;
		if(this.getDimension() != other.getDimension())
			return false;
		for(int dim = 0; dim < this.getDimension(); dim++)
		{
			if(this.getAxisPosition().get(dim) != other.getAxisPosition().get(dim))
				return false;
		}
		return true;
	}
	
	@Override
	public Position clone() throws CloneNotSupportedException 
	{
		Position clonedPosition = new Position();
		for(int d = 0; d < this.getDimension(); d++)
		{
			clonedPosition.getAxisPosition().add(this.getAxisPosition().get(d));
			clonedPosition.setDimension(this.getDimension());
		}
		return clonedPosition;
		
	}

	
	

}
