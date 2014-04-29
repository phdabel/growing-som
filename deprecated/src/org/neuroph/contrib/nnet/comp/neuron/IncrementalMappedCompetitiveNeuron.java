package org.neuroph.contrib.nnet.comp.neuron;

public class IncrementalMappedCompetitiveNeuron extends MappedCompetitiveNeuron {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void calculate() {
		if (this.isCompeting()) {
			// get input only from neurons in this layer
			//this.netInput = this.inputFunction.getOutput();
		} else {
			// get input from other layers
			this.netInput = this.inputFunction.getOutput(this.getConnectionsFromOtherLayers());
			this.setIsCompeting(true);;
		}
		
		this.output = this.transferFunction.getOutput(this.netInput);
		outputHistory.add(0, new Double(this.output));
	}
	

}
