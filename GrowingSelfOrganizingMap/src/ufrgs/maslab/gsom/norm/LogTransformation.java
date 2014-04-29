package ufrgs.maslab.gsom.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.norm.Normalizer;

public class LogTransformation implements Normalizer  {

	private double max = 10d;
	
	@Override
	public void normalize(DataSet dataSet) {
		
		for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = this.logTransformation(row.getInput()); 
            row.setInput(normalizedInput);            
            
        }
		
	}
	
	/**
	 * perform the normalization of one input vector
	 * it performs the log10 of the input and divide by the max base, usually 10
	 * 
	 * @param vector
	 * @return
	 */
	public double[] logTransformation(double[] vector){
		double[] normalizedVector = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (Math.log10(vector[i]+1) / this.getMax());
        }

        return normalizedVector; 
		
	}
	
	/**
	 * returns the max base
	 * @return
	 */
	public double getMax() {
		return max;
	}

	/**
	 * sets the max base to normalize
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}
}
