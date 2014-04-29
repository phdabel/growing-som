package ufrgs.maslab.gsom.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.norm.Normalizer;

import ufrgs.maslab.gsom.exception.NeuralNetworkException;

public class TemplateNormalizer implements Normalizer {
	
	private double lowLimit=0, highLimit=1;
    double[] maxIn, maxOut; // contains max values for in and out columns
    double[] minIn, minOut; // contains min values for in and out columns     

    public TemplateNormalizer(Template template) {
        this.maxIn = template.maxInputValue;
        this.minIn = template.minInputValue;
        this.maxOut = template.maxOutputValue;
        this.minOut = template.minOutputValue;
    }  
    
    @Override
    public void normalize(DataSet dataSet) {
    	
    	if(dataSet.getInputSize() != maxIn.length || dataSet.getInputSize() != minIn.length)
    		throw new NeuralNetworkException("Input size different of the Template size.");
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeToRange(row.getInput(), minIn, maxIn);
            row.setInput(normalizedInput);
            
            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeToRange(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(normalizedOutput);
            }
            
        }
        
    }
    
    private double[] normalizeToRange(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = ((vector[i] - min[i]) / (max[i] - min[i])) * (highLimit - lowLimit) + lowLimit ;
        }

        return normalizedVector;             
    }	

}
