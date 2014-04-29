package org.neuroph.contrib.core.learning;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.neuroph.contrib.nnet.SelfOrganizingMap;
import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.contrib.util.Position;
import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;

public class SOMKohonenLearning extends LearningRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	double learningRate = 0.9d;
	int[] iterations = { 100, 0 };
	double decStep[] = new double[2];
	int mapSize = 0;
	int[] nR = { 1, 1 }; // neighborhood radius
	int currentIteration;

	public SOMKohonenLearning(){
		super();
	}

	@Override
	public void learn(DataSet trainingSet) {
		for (int phase = 0; phase < 2; phase++) {
			for (int k = 0; k < iterations[phase]; k++) {
				Iterator<DataSetRow> iterator = trainingSet.iterator();
				while (iterator.hasNext() && !isStopped()) {
					DataSetRow trainingSetRow = iterator.next();
					learnPattern(trainingSetRow, nR[phase]);				
				} // while
				currentIteration = k;
				//this.notifyChange();	
				this.fireLearningEvent(new LearningEvent(this));
				if (isStopped()) return;
			} // for k
			learningRate = learningRate * 0.5;
		} // for phase
	}

	private void learnPattern(DataSetRow dataSetRow, int neighborhood) {
		neuralNetwork.setInput(dataSetRow.getInput());
		((SelfOrganizingMap)neuralNetwork).calculate();
		
		MappedCompetitiveNeuron winner = ((SelfOrganizingMap)neuralNetwork).getMap().getWinner();
		//MappedCompetitiveNeuron winner = getClosest();
		if (winner.getOutput() == 0)
			return;

		DimensionalGrid<MappedCompetitiveNeuron> mapLayer = ((SelfOrganizingMap)neuralNetwork).getMap();
		
		adjustCellWeights(winner, 0);
		
		adjustNeighborWeights(winner, neighborhood, mapLayer);

		

	}

	private void adjustNeighborWeights(MappedCompetitiveNeuron neuron,
			int neighborhood, DimensionalGrid<MappedCompetitiveNeuron> map) {
		int r = 1;
		adjustMappedNeuron(neuron, map, r, neighborhood);

	}
	
	private void adjustMappedNeuron(MappedCompetitiveNeuron neuron, DimensionalGrid<MappedCompetitiveNeuron> map, int r, int neighborhood)
	{
		List<Position> neighbours = Arrays.asList(neuron.getNeighbours());
		for(MappedCompetitiveNeuron n : map.values())
		{
			if(neighbours.contains(((MappedCompetitiveNeuron)n).getPosition()))
			{
				adjustCellWeights(n, r);				
			}
		}
	}

	// get unit with closetst weight vector
	/*private MappedCompetitiveNeuron getClosest() {
		//			Iterator<Neuron> i = this.neuralNetwork.getLayerAt(1)
		//					.getNeuronsIterator();
		MappedCompetitiveNeuron winner = null;
		double minOutput = 100;
		for(MappedCompetitiveNeuron n : ((SelfOrganizingMap)this.neuralNetwork).getMap().values() ) {
			//			while (i.hasNext()) {
			//				Neuron n = i.next();
			double out = n.getOutput();
			if (out < minOutput) {
				minOutput = out;
				winner = n;
			} // if
		} // while
		return winner;
	}*/

	private void adjustCellWeights(MappedCompetitiveNeuron cell, int r) {
		//Iterator<Connection> i = cell.getInputsIterator();
		//			while (i.hasNext()) {
		//				Connection conn = i.next();
		for(Connection conn : cell.getInputConnections()) {
			double dWeight = (learningRate / (r + 1))   
					* (conn.getInput() - conn.getWeight().getValue());
			conn.getWeight().inc(dWeight);
		}// while
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setIterations(int Iphase, int IIphase) {
		this.iterations[0] = Iphase;
		this.iterations[1] = IIphase;
	}

	public Integer getIteration() {
		return new Integer(currentIteration);
	}

	public int getMapSize() {
		return mapSize;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
		super.setNeuralNetwork(neuralNetwork);
		int neuronsNum = ((SelfOrganizingMap)neuralNetwork).getMap().size();
		this.mapSize = (int) Math.sqrt(neuronsNum);
	}

}
