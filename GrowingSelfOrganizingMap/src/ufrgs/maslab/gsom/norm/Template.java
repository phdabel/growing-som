package ufrgs.maslab.gsom.norm;

public class Template {
	
	double[] minInputValue, minOutputValue;
	double[] maxInputValue, maxOutputValue;
	String[] attributes;
	
	public Template()
	{
		
	}
	
	public Template(double[] minIn, double[] maxIn){
		this.minInputValue = minIn;
		this.maxInputValue = maxIn;
		this.minOutputValue = new double[]{0d};
		this.maxOutputValue = new double[]{1d};
	}
	
	public Template(double[] minIn, double[] maxIn, double[] minOut, double[]maxOut)
	{
		this.minInputValue = minIn;
		this.maxInputValue = maxIn;
		this.minOutputValue = minOut;
		this.maxOutputValue = maxOut;
	}
	
	public void setAttributes(String[] attributes)
	{
		this.attributes = attributes;
	}
	
	public String[] getAttributes()
	{
		return this.attributes;
	}
	

}
