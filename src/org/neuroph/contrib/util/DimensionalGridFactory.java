package org.neuroph.contrib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.util.NeuronProperties;

public class DimensionalGridFactory {
	
	
	private static Map<Position, ? extends MappedCompetitiveNeuron> neurons = new HashMap();
	
	/**
     *  total of dimensions of grid
     *  e.g. in Kohonen SelfOrganizingMaps totalDimensions = 2
     */
    private static Integer totalDimensions = 0;
    
	/**
	 *  internal use
	 *  don't modify it
	 */
	private transient static ArrayList<Integer> pos = new ArrayList<Integer>();
	private transient static int ct = 0;
	
		
	public static DimensionalGrid<? extends MappedCompetitiveNeuron> createLayer(List<Integer> dim, NeuronProperties neuronProperties)
	{
		setTotalDimensions(dim.size());
		mountPosition(dim, 0, neuronProperties);
		CompetitiveLayer layer = new CompetitiveLayer(0, neuronProperties);
		for(MappedCompetitiveNeuron neuron : neurons.values())
		{
			layer.addNeuron(neuron);
		}
		//return layer;
		return null;
	}
	
	private static void mountPosition(List<Integer> dim, int d, NeuronProperties neuronProperties)
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
 				MappedCompetitiveNeuron neuron = MappedNeuronFactory.createNeuron(neuronProperties, p);
 				getMap().put(neuron.getPosition(), neuron);
 				try {
					lookupNeighborhood(neuron);
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
	 * adds mapped neighbors position of a neuron
	 * 
	 * @param neuron
	 * @throws CloneNotSupportedException 
	 */
	private static void lookupNeighborhood(MappedCompetitiveNeuron neuron) throws CloneNotSupportedException
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
				getMap().get(neuron.getPosition()).addNeighbour(position1);
				getMap().get(position1).addNeighbour(neuron.getPosition());
			}
			if(isMapped(position2)){
				getMap().get(neuron.getPosition()).addNeighbour(position2);
				getMap().get(position2).addNeighbour(neuron.getPosition());
			}
		}
	}

	private static Integer getTotalDimensions() {
		return totalDimensions;
	}

	private static void setTotalDimensions(Integer total) {
		totalDimensions = total;
	}

	private static Map<Position, MappedCompetitiveNeuron> getMap() {
		//return neurons;
		return null;
	}
	
	/**
	 * tests if a given neuron was mapped in the 
	 * dimensional space
	 * 
	 * @param neuron
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isMapped(MappedCompetitiveNeuron neuron)
	{
		if(neuron == null)
			throw new NeurophException("Neuron can't be null!");
		return getMap().containsKey(neuron.getPosition());
		
	}
	
	/**
	 * test if a position has neuron mapped
	 * @param position
	 * @return
	 */
	public static boolean isMapped(Position position)
	{
		if(position == null)
			throw new NeurophException("Position can't be null!");
		
		return getMap().containsKey(position);
	}


}
