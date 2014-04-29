package org.neuroph.contrib.core.learning;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.neuroph.contrib.nnet.GrowingSOM;
import org.neuroph.contrib.nnet.comp.layer.DimensionalGrid;
import org.neuroph.contrib.nnet.comp.neuron.MappedCompetitiveNeuron;
import org.neuroph.contrib.util.Position;
import org.neuroph.contrib.util.WriteFile;
import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;

public class GSOMLearning extends LearningRule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	double learningRate = 1d;
	//decay parameter
	double alpha = 0.1;
	int[] iterations = { 50, 0 };
	double decStep[] = new double[2];
	double R = 3.8;
	int mapSize = 0;
	int[] nR = { 1, 1 }; // neighborhood radius
	int currentIteration;

	public GSOMLearning(){
		super();		
	}
	
	public void computeR()
	{
		int ct = 1;
		double r = 0.0;
		for(Integer n : ((GrowingSOM)this.neuralNetwork).getNeuronsCount())
		{
			ct = n * ct;
		}
		if(ct != 4)
		{
			r = (ct * 3.8)/4;
			this.R = r;
		}
	}
	
	public Double computePhi(int n)
	{
		this.computeR();
		Double phi = 0.0;
		phi = 1 - this.R / n; 
		return phi;
	}

	@Override
	public void learn(DataSet trainingSet) {
		for (int phase = 0; phase < 2; phase++) {
			for (int k = 0; k < iterations[phase]; k++) {
				Iterator<DataSetRow> iterator = trainingSet.iterator();
				while (iterator.hasNext() && !isStopped()) {
					DataSetRow trainingSetRow = iterator.next();
					learnPattern(trainingSetRow, phase);				
				} // while
				currentIteration = k;
				//this.notifyChange();	
				this.fireLearningEvent(new LearningEvent(this));
				if (isStopped()) return;
			} // for k
			int n = ((GrowingSOM)neuralNetwork).getMap().size();
			Double newLearningRate = this.computePhi(n) * this.getLearningRate() * this.alpha;
			this.setLearningRate(newLearningRate);
		} // for phase
	}

	private void learnPattern(DataSetRow dataSetRow, int phase) {
		
		String s = "";
		for(Double d : dataSetRow.getInput())
		{
			s += d.toString()+";";
		}
		WriteFile.getInstance().write(s);
		
		neuralNetwork.setInput(dataSetRow.getInput());
		if(phase == 0){
			((GrowingSOM)neuralNetwork).calculate();
		}else{
			((GrowingSOM)neuralNetwork).smoothingPhase();;
		}
		MappedCompetitiveNeuron winner = ((GrowingSOM)neuralNetwork).getMap().getWinner();
		
		//MappedCompetitiveNeuron winner = getClosest();
		if (winner.getOutput() == 0)
			return;

		DimensionalGrid<MappedCompetitiveNeuron> mapLayer = ((GrowingSOM)neuralNetwork).getMap();
		
		adjustCellWeights(winner);
		adjustNeighborWeights(winner, this.nR[phase], mapLayer);

		

	}

	private void adjustNeighborWeights(MappedCompetitiveNeuron neuron,
			int neighborhood, DimensionalGrid<MappedCompetitiveNeuron> map) {
		adjustMappedNeuron(neuron, map, neighborhood);

	}
	
	private void adjustMappedNeuron(MappedCompetitiveNeuron neuron, DimensionalGrid<MappedCompetitiveNeuron> map, int neighborhood)
	{
		List<Position> neighbours = Arrays.asList(neuron.getNeighbours());
		for(Position p  : neighbours)
		{
			adjustCellWeights(map.getNeuronAt(p));
		}
		/*
		for(MappedCompetitiveNeuron n : map.values())
		{
			if(neighbours.contains(((MappedCompetitiveNeuron)n).getPosition()))
			{
				adjustCellWeights(n);				
			}
		}*/
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

	private void adjustCellWeights(MappedCompetitiveNeuron cell) {
		//Iterator<Connection> i = cell.getInputsIterator();
		//			while (i.hasNext()) {
		//				Connection conn = i.next();
		for(Connection conn : cell.getInputConnections()) {
			double dWeight = (learningRate)*(conn.getInput() - conn.getWeight().getValue());
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
		int neuronsNum = ((GrowingSOM)neuralNetwork).getMap().size();
		this.mapSize = (int) Math.sqrt(neuronsNum);
	}

}
