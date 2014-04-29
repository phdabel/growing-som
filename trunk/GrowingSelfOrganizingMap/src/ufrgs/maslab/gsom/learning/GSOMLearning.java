package ufrgs.maslab.gsom.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.neuroph.core.data.DataSetRow;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;
import ufrgs.maslab.gsom.network.GrowingSelfOrganizingMap;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.NeuronFactory;
import ufrgs.maslab.gsom.util.Position;
import ufrgs.maslab.gsom.util.Transmitter;


public class GSOMLearning {
	
	//learning rate and smoothing learning rate
	private double learningRate = Transmitter.getDoubleConfigParameter("gsom.properties", "learning.rate");
	private double initialLearningRate = Transmitter.getDoubleConfigParameter("gsom.properties", "initial.learning.rate");
	private double smoothingPhaseLR = Transmitter.getDoubleConfigParameter("gsom.properties", "smoothing.learning.rate");
	private double initialSmothingLR = Transmitter.getDoubleConfigParameter("gsom.properties", "initial.smoothing.learning.rate");
	//decay parameter
	private double smoothAlpha = Transmitter.getDoubleConfigParameter("gsom.properties", "smoothing.alpha");
	private double alpha = Transmitter.getDoubleConfigParameter("gsom.properties", "alpha");
	//factor of distribution of the error
	private double distribuctionFactor = Transmitter.getDoubleConfigParameter("gsom.properties", "distribuction.factor");
	//iteraionts
	private int[] iterations = { 
			Transmitter.getIntConfigParameter("gsom.properties", "growing.itr"),
			Transmitter.getIntConfigParameter("gsom.properties", "smoothing.itr") };
	
	//gaussian distribution of learning
	public boolean gaussianDistribution = false;
	public double beta = 0.1;
	public double sigma = 10.0;
	public double sigma0 = 10.0;
	
	private transient ArrayList<Position> updatedNeurons = new ArrayList<Position>();
	/**
	 *  counter for new neurons
	 */
	private int spreadCounter = 3;
	
	private double R = 3.8;
	private int[] nR = { 3, 1 }; // neighborhood radius
	
	private GrowingSelfOrganizingMap neuralNetwork;
	
	//public GrowingSelfOrganizingMapGraph2D graph2d;
	/**
	 * buffer to initializate weights
	 */
	private transient Neuron neuronBuffer = null;
	
	int currentIteration;
	

	public GSOMLearning(GrowingSelfOrganizingMap neuralNetwork){
		super();		
		this.neuralNetwork = neuralNetwork;
	}
	
	public void initialize()
	{
		if(this.getNeuralNetwork() == null)
			throw new NeuralNetworkException("Neural Network is not defined");
		Iterator<DataSetRow> iterator = this.neuralNetwork.getStructure().getInitializationDataSet().iterator();
		while(iterator.hasNext())
		{
			DataSetRow initDataSet = iterator.next();
			for(int k = 0; k < 10; k++){
				learnPattern(initDataSet, -1);
			}
		}
		this.setLearningRate(this.initialLearningRate);
		for(Neuron n : this.getNeuralNetwork().getStructure().values())
		{
			this.getNeuralNetwork().getStructure().getNeuronAt(n.getPosition()).error = 0.0;
			this.getNeuralNetwork().getStructure().getNeuronAt(n.getPosition()).totalError = 0.0;
			this.getNeuralNetwork().getStructure().getNeuronAt(n.getPosition()).hits = 0;
			this.neuralNetwork.getStructure().setWinner(null);
		}
		this.getNeuralNetwork().setErrorMax(0.0);
	}
	
	public void testingMode()
	{
		if(this.getNeuralNetwork() == null)
			throw new NeuralNetworkException("Neural Network is not defined");
		this.neuralNetwork.getStructure().getTest().shuffle();
		
		Iterator<DataSetRow> iterator = this.neuralNetwork.getStructure().getTest().iterator();
		while (iterator.hasNext()) {
			DataSetRow testingSetRow = iterator.next();
			testPattern(testingSetRow, 0);
		}
			
		
		
	}
	
	public void learn()
	{
		if(this.getNeuralNetwork() == null)
			throw new NeuralNetworkException("Neural Network is not defined");
		
		for (int phase = 0; phase < 2; phase++) {
			for (int k = 0; k < iterations[phase]; k++) {
				if(k % 25 == 0 && k != 0)
				{
					this.neuralNetwork.getStructure().cleanNetwork();
				}
				Iterator<DataSetRow> iterator = null; 
				this.neuralNetwork.getStructure().getInput().shuffle();
				iterator = this.neuralNetwork.getStructure().getInput().iterator();
				
				while (iterator.hasNext()) {
					DataSetRow trainingSetRow = iterator.next();
					learnPattern(trainingSetRow, phase);
					this.decayLearning(phase);
					if(this.gaussianDistribution)
					{
						this.decreaseNeighborhood();
					}
					if(this.sigma < 1)
						break;
					currentIteration = k;
					
				} // while
				if(phase == 0){
					this.learningRate = this.initialLearningRate;
					//this.sigma0 = this.neuralNetwork.getStructure().size();
					this.sigma = this.sigma0;
				}else if(phase == 1)
				{
					this.learningRate = this.initialSmothingLR;
				}					
			} // for k			
		} // for phase
		
	}
	
	
	public void calculate(double[] input){
		/*double[] weightedF = new double[input.length];
		Arrays.fill(weightedF, 1.0);
		if(this.weightedDistance){
			if(input[3] == 1)
			{
				weightedF = this.weightFeature2;
			}else{
				weightedF = this.weightFeature1;
			}
		}*/
		Position winner = null;
		double w_tmp = Double.MAX_VALUE;
		for(Neuron n : this.neuralNetwork.getStructure().values())
		{
			double output = 0.0;

			for(int k = 0; k < input.length; k++)
			{
				output += (Math.pow((input[k] - n.getWeights()[k]),2));
				//output += weightedF[k] * (Math.pow((input[k] - n.getWeights()[k]),2));
			}
			output = Math.sqrt(output);
			if(output < w_tmp)
			{
				w_tmp = output;
				winner = n.getPosition();
			}
		}
		

		this.neuralNetwork.getStructure().getNeuronAt(winner).error = this.neuralNetwork.getStructure().getNeuronAt(winner).error + w_tmp;
		//System.out.println("node "+winner.getAxisPosition()+" error: "+this.neuralNetwork.getStructure().getNeuronAt(winner).error);
		if(this.neuralNetwork.getStructure().getNeuronAt(winner).error > this.neuralNetwork.getErrorMax())
		{
			this.neuralNetwork.setErrorMax(this.neuralNetwork.getStructure().getNeuronAt(winner).error);
		}
		this.neuralNetwork.getStructure().setWinner(winner);
		
	}
	
	private double getNeighbourhood()
	{
		return this.sigma;
	}
	
	private void decreaseNeighborhood()
	{
		this.sigma = this.sigma - Math.abs(this.sigma0 * this.beta);
	}
	
	private double getDistance(Position winner, Position neuron)
	{
		double d = 0.0;
		for(int k = 0; k < winner.getDimension(); k++)
		{
			d += Math.pow((winner.getAxisPosition().get(k) - neuron.getAxisPosition().get(k)), 2);
		}
		d = Math.sqrt(d);
		return d;
	}
	
	private double getGaussianNeighbourhoodAdaptation(Position winner, Position neuron)
	{
		double d = 0.0;
		d = this.beta * Math.exp(-Math.pow(this.getDistance(winner, neuron),2)/(2*Math.pow(this.getNeighbourhood(), 2)));
		return d;
	}
	
	private void testPattern(DataSetRow dataSetRow, int phase) {
		
		double[] input = dataSetRow.getInput();
		this.calculate(input);
		
		Position winner = this.neuralNetwork.getStructure().getWinner();
		
		if (winner == null)
			return;
		
		double[] weights = this.neuralNetwork.getStructure().getNeuronAt(winner).getWeights();
		double[] neuronAttributeError = this.neuralNetwork.getStructure().getNeuronAt(winner).attributeError;
		double[] neuronAttributeValue = this.neuralNetwork.getStructure().getNeuronAt(winner).attributeValue;
		double diff = 0.0;
		for(int j = 0; j < weights.length; j++)
		{
			neuronAttributeValue[j] += input[j];
			diff = (input[j] - weights[j]);
			double attributeError = Math.pow(diff,2);
			//attributeError = Math.sqrt(attributeError);
			neuronAttributeError[j] = neuronAttributeError[j] + Math.sqrt(attributeError);
			diff = 0.0;
		}
		
		this.neuralNetwork.getStructure().getNeuronAt(winner).attributeError = neuronAttributeError;
		this.neuralNetwork.getStructure().getNeuronAt(winner).hits++;
		
	}
	
	private void learnPattern(DataSetRow dataSetRow, int phase) {
		
		double[] input = dataSetRow.getInput();
		
		this.calculate(input);
		
		Position winner = this.neuralNetwork.getStructure().getWinner();
		
		if (winner == null)
			return;
		
		if(phase == 0){
			
			if(!this.gaussianDistribution){
				adjustCellWeights(winner, input, true);
				adjustNeighborWeights(winner, this.nR[phase], input);
				this.updatedNeurons.clear();
			}else{
				gaussianAdjustCellWeights(winner, input, true);
				gaussianAdjustNeighborWeights(winner, (int)this.sigma, input);
				this.updatedNeurons.clear();
				
			}
	
			this.spread(winner);
			
		}else if(phase ==1){
			
			smoothCellWeights(winner, input, true);
			smoothNeighborWeights(winner, input);
			this.updatedNeurons.clear();
			
		}else if(phase == -1)
		{
			
			initializeCellWeights(winner, input);
			initializeNeighborWeights(winner, input);
			this.decayLearning(0);
		
			
		}
		

	}
	
	public void spread(Position winner)
	{
		
		if(this.neuralNetwork.getStructure().getNeuronAt(winner).error == this.neuralNetwork.getErrorMax() &&
				this.neuralNetwork.getErrorMax() >= this.neuralNetwork.getStructure().getGrowthThreshold())
        {
			/**
        	 * grow new nodes
        	 */
			if(this.neuralNetwork.getStructure().getNeuronAt(winner).existsFreePosition()
					//&& !this.neuralNetwork.getStructure().get(winner).spread
					)
        	{
				
        		try {
        			this.spreadNeuron(winner);
					this.getNeuralNetwork().getStructure().get(winner).spread = true;
					this.setLearningRate(this.initialLearningRate);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
        	}else{
        		/**
            	 *  divide error
            	 */
        		this.neuralNetwork.getStructure().getNeuronAt(winner).error = (this.neuralNetwork.getStructure().getGrowthThreshold() / 2);
        		this.adjustNeighborError(winner, this.nR[0]);
        		this.updatedNeurons.clear();
        	}
        }
		
	}
	
	
	
	/**
     * grow new neurons from the parameter neuron
     * @param neuron
     * @throws CloneNotSupportedException
     */
    public void spreadNeuron(Position neuron) throws CloneNotSupportedException
	{
    	
		for(int d = 0; d < this.neuralNetwork.getStructure().getDimensions(); d++)
        {
             
            Position position1 = neuron.clone();
            Position position2 = neuron.clone();
             
            Integer newPos1 = new Integer(position1.getAxisPosition().get(d).intValue());
            Integer newPos2 = new Integer(position2.getAxisPosition().get(d).intValue());
             
            newPos1++;
            newPos2--;
             
            position1.getAxisPosition().set(d, newPos1);
            position2.getAxisPosition().set(d, newPos2);
            
            if(!this.neuralNetwork.getStructure().containsKey(position1)){
            	this.increaseSpreadCounter();
            	this.growNeuron(position1, this.neuralNetwork.getStructure().getNeuronAt(neuron));
            }
            if(!this.neuralNetwork.getStructure().containsKey(position2)){
            	this.increaseSpreadCounter();
                this.growNeuron(position2, this.neuralNetwork.getStructure().getNeuronAt(neuron));
            }
        }
		this.neuralNetwork.getStructure().mapSize(this.neuralNetwork.getStructure().size());
		
	}
    
    private void growNeuron(Position p, Neuron winner)
	{
		Neuron neuron = NeuronFactory.createNeuron(p);
		neuron.setSpreadCounter(this.getSpreadCounter());
		neuron.setWeights(new double[this.neuralNetwork.getStructure().getInputCount()]);
		neuron.setAttributeError(new double[this.neuralNetwork.getStructure().getInputCount()]);
		neuron.attributeValue = new double[this.neuralNetwork.getStructure().getInputCount()];
		Arrays.fill(neuron.attributeError, 0.0);
		Arrays.fill(neuron.attributeValue, 0.0);
		Arrays.fill(neuron.weights, 0.0);
		neuron.error = 0.0;
		this.getNeuralNetwork().getStructure().put(p, neuron);
        
		try {
            this.neuralNetwork.getStructure().lookupNeighborhood(neuron);
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
		for(int x = 0; x < this.neuralNetwork.getStructure().getInputCount(); x++)
		{
			Double w = this.initializeWeights(neuron, winner, x);
			this.neuralNetwork.getStructure().getNeuronAt(p).getWeights()[x] = w;
		}
		
        this.neuronBuffer = null;
	}
    
    private Double initializeWeights(Neuron newNeuron, Neuron oldNeuron, int k)
    {
    	Double w = 0.0;
    	
    	if(newNeuron.getNeighbours().size() > 1)
    	{
    		for(Position p : newNeuron.getNeighbours())
    		{
    			w += this.neuralNetwork.getStructure().getNeuronAt(p).getWeights()[k];
    		}
    		w = w / newNeuron.getNeighbours().size();
    	}
    	else if(newNeuron.getNeighbours().size() == 1 && oldNeuron.getNeighbours().size() > 1){
    			if(this.neuronBuffer == null)
    			{
    				Position p = null;
					try {
						p = this.getNeighboursToWeightInitialization(oldNeuron, newNeuron);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					this.neuronBuffer = this.neuralNetwork.getStructure().getNeuronAt(p);
					
    			}
    			Double w1 = oldNeuron.getWeights()[k];
    			Double w2 = this.neuronBuffer.getWeights()[k]; 
    			if(w1 > w2)
    			{
    				w = w1 + (w1 - w2);
    			}else if(w2 > w1)
    			{
    				w = w1 - (w2 - w1);
    			}else{
    				w = w1;
    			}
    	}else if(newNeuron.getNeighbours().size() == 1 && oldNeuron.getNeighbours().size() == 1)
    	{
    		Double minW = Double.MAX_VALUE;
    		Double maxW = Double.MIN_VALUE;
    		for(Neuron neurons : this.neuralNetwork.getStructure().values())
    		{
    			if(!neurons.equals(newNeuron)){
	    			Double wn = neurons.getWeights()[k];
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
    
    private void initializeCellWeights(Position cell, double[] input)
    {
    	/*double[] weightedF = new double[input.length];
    	Arrays.fill(weightedF, 1.0);
		if(this.weightedDistance){
			if(input[3] == 1)
			{
				weightedF = this.weightFeature2;
			}else{
				weightedF = this.weightFeature1;
			}
		}*/
    	double diff = 0.0;
		double[] weights = this.neuralNetwork.getStructure().getNeuronAt(cell).getWeights();
		for(int j = 0; j < weights.length; j++)
		{
			diff = (input[j] - weights[j]);
			double dWeight = this.learningRate * (diff);
			weights[j] = weights[j] + dWeight;
			this.neuralNetwork.getStructure().getNeuronAt(cell).weights[j] = weights[j];
			diff = 0.0;
		}
    	
    }
    
    private void initializeNeighborWeights(Position neuron, double[] input)
	{
		List<Position> neighbours = this.neuralNetwork.getStructure().getNeuronAt(neuron).getNeighbours(); 
		for(Position p  : neighbours)
		{
			if(this.neuralNetwork.getStructure().getNeuronAt(p) != null)
			{
				Neuron neighbourNeuron = this.neuralNetwork.getStructure().getNeuronAt(p);
				initializeCellWeights(neighbourNeuron.getPosition(), input);
			}
		}
		
	}
        
    private void smoothCellWeights(Position cell, double[] input, boolean error)
    {
    	/*double[] weightedF = new double[input.length];
    	Arrays.fill(weightedF, 1.0);
		if(this.weightedDistance){
			if(input[3] == 1)
			{
				weightedF = this.weightFeature2;
			}else{
				weightedF = this.weightFeature1;
			}
		}*/
    	double diff = 0.0;
		double[] weights = this.neuralNetwork.getStructure().getNeuronAt(cell).getWeights();
		double[] neuronAttributeError = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeError;
		double[] neuronAttributeValue = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeValue;
		for(int j = 0; j < weights.length; j++)
		{
			neuronAttributeValue[j] += input[j];
			diff = (input[j] - weights[j]);
			double dWeight = this.smoothingPhaseLR * (diff);
			weights[j] = weights[j] + dWeight;
			this.neuralNetwork.getStructure().getNeuronAt(cell).weights[j] = weights[j];
			double attributeError = /*weightedF[j] **/ Math.pow(diff,2);
			//attributeError = Math.sqrt(attributeError);
			neuronAttributeError[j] = neuronAttributeError[j] + Math.sqrt(attributeError);
			diff = 0.0;
		}
		//this.neuralNetwork.getStructure().getNeuronAt(cell).hits++;
		
	    	
    }
    
    private void gaussianAdjustCellWeights(Position cell, double[] input, boolean error)
    {
    	Position winner = this.getNeuralNetwork().getStructure().getWinner();
    	/*double[] weightedF = new double[input.length];
		Arrays.fill(weightedF, 1.0);
		if(this.weightedDistance){
			if(input[0] >= 1)
			{
				weightedF = this.weightFeature2;
			}else{
				weightedF = this.weightFeature1;
			}
		}*/
		double diff = 0.0;
		double[] weights = this.neuralNetwork.getStructure().getNeuronAt(cell).getWeights();
		double[] neuronAttributeError = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeError;
		double[] neuronAttributeValue = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeValue;
		for(int j = 0; j < weights.length; j++)
		{
			neuronAttributeValue[j] += input[j];
			diff = (input[j] - weights[j]);
			double dWeight = this.learningRate * this.getGaussianNeighbourhoodAdaptation(winner,cell) * (diff);
			weights[j] = weights[j] + dWeight;
			this.neuralNetwork.getStructure().getNeuronAt(cell).weights[j] = weights[j];
			double attributeError = /*weightedF[j] */ Math.pow(diff,2);
			//attributeError = Math.sqrt(attributeError);
			neuronAttributeError[j] = neuronAttributeError[j] + Math.sqrt(attributeError);
			diff = 0.0;
		}
		
		this.updatedNeurons.add(cell);
		
    }
    
    private void adjustNeighborError(Position neuron, int neighborhood)
	{
		
		List<Position> neighbours = this.neuralNetwork.getStructure().getNeuronAt(neuron).getNeighbours(); 
		for(Position p  : neighbours)
		{
			if(!this.updatedNeurons.contains(p)){
				if(this.neuralNetwork.getStructure().getNeuronAt(p) != null)
				{
					Neuron neighbourNeuron = this.neuralNetwork.getStructure().getNeuronAt(p);
					adjustCellError(neighbourNeuron.getPosition());
					if(neighborhood > 1)
						adjustNeighborError(neighbourNeuron.getPosition(), neighborhood--);
				}
			}
		}
		
	}
    
    private void adjustCellError(Position cell)
    {
			Double e = this.neuralNetwork.getStructure().getNeuronAt(cell).error;
			this.neuralNetwork.getStructure().getNeuronAt(cell).error = e + (this.getDistribuctionFactor() * e);
			this.updatedNeurons.add(cell);
    }
    
	
	private void adjustCellWeights(Position cell, double[] input, boolean error) {
		/*double[] weightedF = new double[input.length];
		Arrays.fill(weightedF, 1.0);
		if(this.weightedDistance){
			if(input[0] >= 1)
			{
				weightedF = this.weightFeature2;
			}else{
				weightedF = this.weightFeature1;
			}
		}*/
		double diff = 0.0;
		double[] weights = this.neuralNetwork.getStructure().getNeuronAt(cell).getWeights();
		double[] neuronAttributeError = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeError;
		double[] neuronAttributeValue = this.neuralNetwork.getStructure().getNeuronAt(cell).attributeValue;
		for(int j = 0; j < weights.length; j++)
		{
			neuronAttributeValue[j] += input[j];
			diff = (input[j] - weights[j]);
			double dWeight = this.learningRate * (diff);
			weights[j] = weights[j] + dWeight;
			this.neuralNetwork.getStructure().getNeuronAt(cell).weights[j] = weights[j];
			double attributeError = /*weightedF[j] **/ Math.pow(diff,2);
			//attributeError = Math.sqrt(attributeError);
			neuronAttributeError[j] = neuronAttributeError[j] + Math.sqrt(attributeError);
			diff = 0.0;
		}
		
		this.updatedNeurons.add(cell);
		
	}
	
	private void smoothNeighborWeights(Position neuron, double[] input)
	{
		List<Position> neighbours = this.neuralNetwork.getStructure().getNeuronAt(neuron).getNeighbours(); 
		for(Position p  : neighbours)
		{
			if(this.neuralNetwork.getStructure().getNeuronAt(p) != null)
			{
				Neuron neighbourNeuron = this.neuralNetwork.getStructure().getNeuronAt(p);
				smoothCellWeights(neighbourNeuron.getPosition(), input, false);
			}
		}
		
	}
	
	private void gaussianAdjustNeighborWeights(Position neuron, int neighborhood, double[] input)
	{
		
		List<Position> neighbours = this.neuralNetwork.getStructure().getNeuronAt(neuron).getNeighbours(); 
		for(Position p  : neighbours)
		{
			if(!this.updatedNeurons.contains(p)){
				if(this.neuralNetwork.getStructure().getNeuronAt(p) != null)
				{
					Neuron neighbourNeuron = this.neuralNetwork.getStructure().getNeuronAt(p);
					gaussianAdjustCellWeights(neighbourNeuron.getPosition(), input, false);
					if(neighborhood > 0)
						gaussianAdjustNeighborWeights(neighbourNeuron.getPosition(), neighborhood--, input);
				}
			}
		}
		
	}
	
	private void adjustNeighborWeights(Position neuron, int neighborhood, double[] input)
	{
		
		List<Position> neighbours = this.neuralNetwork.getStructure().getNeuronAt(neuron).getNeighbours(); 
		for(Position p  : neighbours)
		{
			if(!this.updatedNeurons.contains(p)){
				if(this.neuralNetwork.getStructure().getNeuronAt(p) != null)
				{
					Neuron neighbourNeuron = this.neuralNetwork.getStructure().getNeuronAt(p);
					adjustCellWeights(neighbourNeuron.getPosition(), input, false);
					if(neighborhood > 1)
						adjustNeighborWeights(neighbourNeuron.getPosition(), neighborhood--, input);
				}
			}
		}
		
	}
	
	
	public void decayLearning(int phase)
	{
		if(phase == 0){
			int n = this.neuralNetwork.getStructure().getNeuronsCount();
			Double newLearningRate = (this.computePhi(n) * this.getLearningRate() * this.alpha);
			this.setLearningRate(newLearningRate);
		}else{
			Double newLearningRate = ((this.getLearningRate() * this.smoothAlpha));
			this.setLearningRate(newLearningRate);
		}
	}
	
	public void computeR()
	{
		double r = 0.0;
		int ct = this.neuralNetwork.getStructure().getInitialMapSize();
		
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
		phi = (1 - this.R / n); 
		return phi;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getInitialLearningRate() {
		return initialLearningRate;
	}

	public void setInitialLearningRate(double initialLearningRate) {
		this.initialLearningRate = initialLearningRate;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int[] getIterations() {
		return iterations;
	}

	public void setIterations(int[] iterations) {
		this.iterations = iterations;
	}

	public double getR() {
		return R;
	}

	public void setR(double r) {
		R = r;
	}

	public int[] getnR() {
		return nR;
	}

	public void setnR(int[] nR) {
		this.nR = nR;
	}

	public GrowingSelfOrganizingMap getNeuralNetwork() {
		return neuralNetwork;
	}

	public void setNeuralNetwork(GrowingSelfOrganizingMap neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}

	public int getSpreadCounter() {
		return spreadCounter;
	}

	public void setSpreadCounter(int spreadCounter) {
		this.spreadCounter = spreadCounter;
	}
	
	public void increaseSpreadCounter(){
		this.spreadCounter++;
	}
	
	public Position getNeighboursToWeightInitialization(Neuron oldNeuron, Neuron newNeuron) throws CloneNotSupportedException
	{
		Position p = null;
		Position possiblePosition1;
		Position possiblePosition2;
		Position possiblePosition3;
		Position possiblePosition4;
		if(oldNeuron.getPosition().getAxisPosition().get(0) != newNeuron.getPosition().getAxisPosition().get(0))
		{
			possiblePosition1 = oldNeuron.getPosition().clone();
	        possiblePosition2 = oldNeuron.getPosition().clone();
	        possiblePosition3 = oldNeuron.getPosition().clone();
	        possiblePosition4 = oldNeuron.getPosition().clone();
	        
	        Integer xPos1 = new Integer(possiblePosition1.getAxisPosition().get(0).intValue());
	        Integer xPos2 = new Integer(possiblePosition2.getAxisPosition().get(0).intValue());
	        Integer yPos1 = new Integer(possiblePosition1.getAxisPosition().get(1).intValue());
	        Integer yPos2 = new Integer(possiblePosition2.getAxisPosition().get(1).intValue());
	         
	        xPos1++;
	        xPos2--;
	        yPos1++;
	        yPos2--;
	        
	        possiblePosition1.getAxisPosition().set(0, xPos1);
	        possiblePosition2.getAxisPosition().set(0, xPos2);
	        possiblePosition3.getAxisPosition().set(1, yPos1);
	        possiblePosition4.getAxisPosition().set(1, yPos2);
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition1) && !possiblePosition1.equals(newNeuron.getPosition())){
	        	//front neuron
	        	//old one is back
	        	return possiblePosition1;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition2) && !possiblePosition2.equals(newNeuron.getPosition()))
	        {
	        	//back neuron
	        	//old one is at front
	        	return possiblePosition2;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition3))
	        {
	        	//upper neuron
	        	return possiblePosition3;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition4))
	        {
	        	//lower neuron
	        	return possiblePosition4;
	        }
	        
		}else{
			
			possiblePosition1 = oldNeuron.getPosition().clone();
	        possiblePosition2 = oldNeuron.getPosition().clone();
	        possiblePosition3 = oldNeuron.getPosition().clone();
	        possiblePosition4 = oldNeuron.getPosition().clone();
	        
	        Integer xPos1 = new Integer(possiblePosition1.getAxisPosition().get(0).intValue());
	        Integer xPos2 = new Integer(possiblePosition2.getAxisPosition().get(0).intValue());
	        Integer yPos1 = new Integer(possiblePosition1.getAxisPosition().get(1).intValue());
	        Integer yPos2 = new Integer(possiblePosition2.getAxisPosition().get(1).intValue());
	         
	        xPos1++;
	        xPos2--;
	        yPos1++;
	        yPos2--;
	        
	        possiblePosition1.getAxisPosition().set(0, xPos1);
	        possiblePosition2.getAxisPosition().set(0, xPos2);
	        possiblePosition3.getAxisPosition().set(1, yPos1);
	        possiblePosition4.getAxisPosition().set(1, yPos2);
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition3) && !possiblePosition3.equals(newNeuron.getPosition())){
	        	//upper neuron
	        	//old one is lower
	        	return possiblePosition3;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition4) && !possiblePosition4.equals(newNeuron.getPosition()))
	        {
	        	//lower neuron
	        	//old one is upper
	        	return possiblePosition4;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition1))
	        {
	        	//upper neuron
	        	return possiblePosition1;
	        }
	        if(this.neuralNetwork.getStructure().containsKey(possiblePosition2))
	        {
	        	//lower neuron
	        	return possiblePosition2;
	        }
			
		}
		
		return p;
	}

	public double getDistribuctionFactor() {
		return distribuctionFactor;
	}

	public void setDistribuctionFactor(double distribuctionFactor) {
		this.distribuctionFactor = distribuctionFactor;
	}

	public double getSmoothingPhaseLR() {
		return smoothingPhaseLR;
	}

	public void setSmoothingPhaseLR(double smoothingPhaseLR) {
		this.smoothingPhaseLR = smoothingPhaseLR;
	}

}
