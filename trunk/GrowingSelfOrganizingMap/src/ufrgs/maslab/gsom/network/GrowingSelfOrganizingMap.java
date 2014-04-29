package ufrgs.maslab.gsom.network;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.data.DataSet;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;
import ufrgs.maslab.gsom.layer.CompetitiveLayer;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.norm.Template;
import ufrgs.maslab.gsom.util.Position;


public class GrowingSelfOrganizingMap{
	
	private CompetitiveLayer<Neuron> structure;
	private double errorMax = 0.0;
	private transient ArrayList<Position> tempList = new ArrayList<Position>();
	private Template template;

	public GrowingSelfOrganizingMap(DataSet input, List<Integer> dimensions)
	{
		CompetitiveLayer<Neuron> mapLayer = new CompetitiveLayer<Neuron>(input, dimensions);
		//this.setTemplate(template);
		this.setStructure(mapLayer);
	}

	public CompetitiveLayer<Neuron> getStructure() {
		return structure;
	}

	public void setStructure(CompetitiveLayer<Neuron> structure) {
		this.structure = structure;
	}

	public double getErrorMax() {
		return errorMax;
	}

	public void setErrorMax(double errorMax) {
		this.errorMax = errorMax;
	}
	
	
	public void creatingLabels(Template template){
    	for(Neuron n : this.getStructure().values())
    	{
    		
    		double[] f = this.getStructure().getNeuronAt(n.getPosition()).attributeValue;
    		double[] erF = this.getStructure().getNeuronAt(n.getPosition()).attributeError;
    		
			for(int i = 0; i < this.getStructure().getInputCount(); i++)
    		{
				if(n.getHits() != 0){
	    			Double featureVal = (f[i]/n.getHits());
	    			Double errorFeature = Math.abs(erF[i]/n.getHits());
	    			f[i] = featureVal;
	    			erF[i] = errorFeature;
	    			if(featureVal != 0 && (errorFeature * 100) >= 0.1)
	    			{
	    				String labelAttribute = template.getAttributes()[i];//this.getLabel(i);
	    				labelAttribute += ": "+n.weights[i];
	    				n.label.add(labelAttribute);
	    			}
				}
    		}
			this.getStructure().getNeuronAt(n.getPosition()).attributeError = erF;
			this.getStructure().getNeuronAt(n.getPosition()).attributeValue = f;
    	}
    }
	
	public String getLabel(int x)
	{
		String s = "";
		switch(x)
		{
			case 0:
				s = "Temperature";
				break;
			case 1:
				s = "Burning kitchen";
				break;
			case 2:
				s = "Burning apartment";
				break;
			case 3:
				s = "Burning Floor";
				break;
			case 4:
				s = "Burning Building";
				break;
			case 5:
				s = "Wooden House";
				break;		
			case 6:
				s = "Steel framed";
				break;
			case 7:
				s = "Concrete";
				break;
			case 8:
				s = "1 floor";
				break;
			case 9:
				s = "2 floors";
				break;
			case 10:
				s = "3 floors";
				break;
			case 11:
				s = "4 floors";
				break;
			case 12:
				s = "5 floors";
				break;
			case 13:
				s = "huge area";
				break;
			case 14:
				s = "burning";
				break;
			case 15:
				s = "x";
				break;
			case 16:
				s = "y";
				break;
		
		}
		
		return s;
	}

	public void buildSkeleton() {
		Neuron[] n = new Neuron[999];
		Arrays.fill(n, null);
		for(Neuron neuron : this.getStructure().values())
		{
			neuron.getNeighbours().clear();
			n[neuron.spreadCounter] = neuron;
		}

		for(int k = 0; k < n.length; k++)
		{
			if(k <= 3)
			{
				switch(k)
				{
					case 1:
						this.getStructure().getNeuronAt(n[0].getPosition()).addNeighbour(n[1].getPosition());
						this.getStructure().getNeuronAt(n[1].getPosition()).addNeighbour(n[0].getPosition());
						break;
					case 2:
						this.getStructure().getNeuronAt(n[0].getPosition()).addNeighbour(n[2].getPosition());
						this.getStructure().getNeuronAt(n[2].getPosition()).addNeighbour(n[0].getPosition());
						break;
					case 3:
						this.getStructure().getNeuronAt(n[3].getPosition()).addNeighbour(n[1].getPosition());
						this.getStructure().getNeuronAt(n[1].getPosition()).addNeighbour(n[3].getPosition());
						this.getStructure().getNeuronAt(n[3].getPosition()).addNeighbour(n[2].getPosition());
						this.getStructure().getNeuronAt(n[2].getPosition()).addNeighbour(n[3].getPosition());
						break;
				}				
			}else{
				if(n[k] != null){
					//System.out.println(k+" - "+n[k].getPosition().getAxisPosition()+" - "+n[k].hits);
					Position p = null;
					try {
						p = this.lookupNeighborhood(n[k]);
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(p.getAxisPosition());
					if(p!=null){
						this.getStructure().getNeuronAt(p).addNeighbour(n[k].getPosition());
						this.getStructure().getNeuronAt(n[k].getPosition()).addNeighbour(p);
					}
				}
				
			}
		}
		
		//calculate hit point distance
		for(Neuron neuron : this.getStructure().values())
		{
			if(neuron.hits > 0)
			{
				for(Position neighN : neuron.getNeighbours())
				{
					if(this.getStructure().getNeuronAt(neighN).hits > 0)
					{
						this.tempList.add(neighN);
						neuron.distanceBetweenNodes.put(neighN, this.distanceBetweenNodes(neuron, this.getStructure().getNeuronAt(neighN)));
					}else{
						this.recursiveSearch(neighN, neuron.getPosition());
					}
				}
			}
			this.tempList.clear();
			
		}
		
		//calculate junction distance
		for(Neuron neuron : this.getStructure().values())
		{
			if(neuron.hits  == 0)
			{
				for(Position neighN : neuron.getNeighbours())
				{
					if(this.getStructure().getNeuronAt(neighN).hits == 0)
					{
						this.tempList.add(neighN);
						neuron.distanceBetweenJunctions.put(neighN, this.distanceBetweenNodes(neuron, this.getStructure().getNeuronAt(neighN)));
					}else{
						this.recursiveJunctionSearch(neighN, neuron.getPosition());
					}
				}
			}
		}
		
		Map<Position, Double> maxDistance = new HashMap<Position, Double>();
		int ct = 0;
		while(ct < 2)
		{
			Double val = this.maxDistance();
			for(Neuron neuron : this.getStructure().values())
			{	
				for(Position p : neuron.distanceBetweenNodes.keySet())
				{
					if(neuron.distanceBetweenNodes.get(p) >= val)
					{
						maxDistance.put(p, neuron.distanceBetweenNodes.get(p));
					}
				}
				
			}
			
			
			for(Position p : maxDistance.keySet())
			{
				ArrayList<Position> removeFromP = new ArrayList<Position>();
				for(Position neighB : this.getStructure().getNeuronAt(p).getNeighbours())
				{
					this.getStructure().getNeuronAt(neighB).getNeighbours().remove(p);
					removeFromP.add(neighB);
				}
				for(Position removeP : removeFromP)
				{
					this.getStructure().getNeuronAt(p).getNeighbours().remove(removeP);
				}
				//System.out.println("Pos: "+p.getAxisPosition()+" val: "+maxDistance.get(p));
			}
			ct++;
			maxDistance.clear();
		}
		
	}
	
	public Double maxDistance()
	{
		Double val = Double.MIN_VALUE;
		Position bestP = null;
		Neuron bestN = null;
		for(Neuron neuron : this.getStructure().values())
		{
			for(Position p : neuron.distanceBetweenNodes.keySet())
			{
				if(neuron.distanceBetweenNodes.get(p) >= val)
				{
					bestN = neuron;
					bestP = p;
					val = neuron.distanceBetweenNodes.get(p);
				}
			}
		}
		if(bestP!=null)
			bestN.distanceBetweenNodes.remove(bestP);
		return val;
		
	}
	
	public void recursiveJunctionSearch(Position pos, Position source)
	{
		for(Position neighbour : this.getStructure().getNeuronAt(pos).getNeighbours())
		{
			if(!neighbour.equals(source) && !this.tempList.contains(neighbour) && this.getStructure().getNeuronAt(pos).hits == 0)
			{
				this.tempList.add(neighbour);
				this.getStructure().getNeuronAt(source).distanceBetweenNodes.put(neighbour, this.distanceBetweenNodes(
						this.getStructure().getNeuronAt(source),this.getStructure().getNeuronAt(pos)));
			}else
			{
				for(Position p : this.getStructure().getNeuronAt(pos).getNeighbours())
				{
					if(!this.tempList.contains(p)){
						this.tempList.add(p);
						this.recursiveSearch(p, source);
					}
				}
			}
		}
	}
	
	
	public void recursiveSearch(Position pos, Position source)
	{
		for(Position neighbour : this.getStructure().getNeuronAt(pos).getNeighbours())
		{
			if(!neighbour.equals(source) && !this.tempList.contains(neighbour) && this.getStructure().getNeuronAt(pos).hits > 0)
			{
				this.tempList.add(neighbour);
				this.getStructure().getNeuronAt(source).distanceBetweenNodes.put(neighbour, this.distanceBetweenNodes(
						this.getStructure().getNeuronAt(source),this.getStructure().getNeuronAt(pos)));
			}else
			{
				for(Position p : this.getStructure().getNeuronAt(pos).getNeighbours())
				{
					if(!this.tempList.contains(p)){
						this.tempList.add(p);
						this.recursiveSearch(p, source);
					}
				}
			}
		}
	}
	
	public Double distanceBetweenNodes(Neuron n1, Neuron n2)
	{
		Double dist = 0.0;
		for(int k = 0; k < n1.getWeights().length; k++)
		{
			dist += Math.pow((n1.weights[k] - n2.weights[k]),2);
		}
		return Math.sqrt(dist);
		//return dist;
	}
	 
	public Position lookupNeighborhood(Neuron neuron) throws CloneNotSupportedException
	{

        int spreadCt = Integer.MAX_VALUE;
		Position bestPosition = null;
		for(int d = 0; d < neuron.getPosition().getDimension(); d++)
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
            	if(this.getStructure().getNeuronAt(position1).spreadCounter < spreadCt)
            	{
            		spreadCt = this.getStructure().getNeuronAt(position1).spreadCounter;
            		bestPosition = position1;
            	}
            }
            if(isMapped(position2)){
            	if(this.getStructure().getNeuronAt(position2).spreadCounter < spreadCt)
            	{
            		spreadCt = this.getStructure().getNeuronAt(position2).spreadCounter;
            		bestPosition = position2;
            	}                
            }
        }
		return bestPosition;
	}
	
	public boolean isMapped(Position position)
    {
        if(position == null)
            throw new NeuralNetworkException("Position can't be null!");
        return this.getStructure().containsKey(position);
    }

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
	

}
