import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neuroph.contrib.nnet.GrowingSOM;
import org.neuroph.contrib.util.Position;
import org.neuroph.contrib.util.WriteFile;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;


public class testgsom {
	
	public static void main(String[] args) {
		/*
		// create training set (logical AND function)
        DataSet trainingSet = new DataSet(3, 1);
        trainingSet.addRow(new DataSetRow(new double[]{1, 0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0, 1}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1, 1}, new double[]{1}));
        trainingSet.normalize();
        */
		DataSet trainingSet = new DataSet(6,1);
		for(int i = 0; i < 50; i++)
		{
			trainingSet.addRow(randomTask());
		}
		
		/*
		DataSet trainingSet = new DataSet(16,1);
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,0,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,0,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,0,1,1,0,0,1,0,1,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,0,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,0,0,4,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,0,1,0,1,1,1,1,1,0,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,1,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,0,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,1,0,0,4,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,1,1,0,4,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,1,0,0,1,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,2,0,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,1,0,0,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,2,0,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,1,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,0,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,1,0,1,0,0,0,0,1,1,0,6,0,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,1,0,1,0,0,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,0,0,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,1,0,1,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,1,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,1,0,1,0,0,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,0,0,8,0,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,0,0,0,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,1,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,0,1,1,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,1,1,1,1,1,0,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,1,1,0,1,1,0,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,0,1,0,1,1,1,1,1,0,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,1,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,0,0,1,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,0,0,0,0,1,0,0,1,1,0,8,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,0,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,1,1,1,1,1,0,1,0,0,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,1,1,1,1,1,0,1,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,0,0,0,1,1,1,1,0,1,0,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,1,0,0,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,1,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,1,1,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,1,1,1,1,0,0,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,0,1,1,0,0,1,0,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,0,0,0,0,0,5,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,1,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,1,0,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,0,0,0,1,0,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,0,1,1,1,0,0,4,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,0,0,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,1,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,1,1,1,1,0,0,1,0,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,1,0,0,1,1,1,0,0,2,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,4,1,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,1,0,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,0,1,1,1,0,0,2,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,1,0,1,0,0,0,0,1,1,0,6,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1,0,0,1,0,0,1,1,1,1,0,0,4,1,0,1}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));*/
		trainingSet.normalize();
        ArrayList<Integer> dim = new ArrayList<Integer>();
        dim.add(2);
        dim.add(2);
        dim.add(2);
        
        WriteFile.getInstance().openFile("experiment");
        
        // create perceptron neural network
        //GrowingSOM gsom = new GrowingSOM(16, dim);
        GrowingSOM gsom = new GrowingSOM(6, dim);
        int ct = 1;
        for(Integer i : dim)
        {
        	ct *= i;
        }
       
        WriteFile.getInstance().write("Initial Neurons:;"+ct+";");
        WriteFile.getInstance().nLine();
        WriteFile.getInstance().write("Initial Learning Rate:;"+gsom.getLearningRule().getLearningRate()+";");
        WriteFile.getInstance().nLine();
        
        
        WriteFile.getInstance().write("temperature;matter;floors;groundArea;totalArea;burning;x;y;z;hits;spreadCounter;new;");
        WriteFile.getInstance().nLine();
                
        // learn the training set
        gsom.learn(trainingSet);
        // test perceptron
        WriteFile.getInstance().write("network size:;"+gsom.getMap().size());
        //testNeuralNetwork(gsom, trainingSet);
        // save trained perceptron
        WriteFile.getInstance().closeFile();
        
        
        WriteFile.getInstance().openFile("test");
        gsom.getLearningRule().setLearningRate(0.001);
        gsom.getLearningRule().setIterations(0, 50);
        WriteFile.getInstance().write("Initial Learning Rate:;"+gsom.getLearningRule().getLearningRate()+";");
        WriteFile.getInstance().nLine();
        WriteFile.getInstance().write("temperature;matter;floors;groundArea;totalArea;burning;x;y;z;hits;spreadCounter;new;");
        WriteFile.getInstance().nLine();
        gsom.learn(trainingSet);
        gsom.save("som.nnet");
        WriteFile.getInstance().closeFile();
       
        
        GrowingSOM loadGSOM = (GrowingSOM)GrowingSOM.load("som.nnet");
        System.out.println("Testing trained perceptron");
        testNeuralNetwork(loadGSOM, trainingSet);
        
		
	}
	
	public static DataSetRow randomTask()
	{
		Random r = new Random();
		//Random r1 = new Random();
		double[] d = new double[6];
		for(int i = 0; i < 6; i++)
		{
			switch(i)
			{
				case 0:
					d[i] = r.nextInt(5);
					break;
				case 1:
					d[i] = r.nextInt(3);
					break;
				case 2:
					if(d[i-1] > 0){
						d[i] = (1 + r.nextInt(5));
					}else{
						d[i] = (1 + r.nextInt(2));
					}
					break;
				case 3:
					d[i] = (100 + r.nextInt(900));
					break;
				case 4:
					d[i] = d[i-2]*d[i-1];
					break;
				case 5:
					if(d[0] == 0){
						d[i] = 0;
					}else{
						d[i] = 1;
					}					
					break;
			}
		}
		DataSetRow task = new DataSetRow(d, new double[]{0});
		return task;
	}
	
	
	/**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param testSet data set used for testing
     */
	public static void testNeuralNetwork(GrowingSOM neuralNet, DataSet testSet) {
        for(DataSetRow trainingElement : testSet.getRows()) {
    		neuralNet.setInput(trainingElement.getInput());
    		System.out.print("input: ");
    		for(Double d : trainingElement.getInput())
    		{
    			System.out.print(d+", ");
    		}
    		System.out.println();
            System.out.println("winner "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());            
        }
        
    }
	
	/*
    public static void testNeuralNetwork(GrowingSOM neuralNet, DataSet testSet) {
    	List<Position> ativados = new ArrayList<Position>();
    	int ct = 0;
        for(DataSetRow trainingElement : testSet.getRows()) {
    		neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            
            System.out.print("id: "+ct);
            System.out.println(" vencedor "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            if(!ativados.contains(neuralNet.getMap().getWinner().getPosition()))
            {
            	ativados.add(neuralNet.getMap().getWinner().getPosition());
            	System.out.print("Neuronio "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            	System.out.println(" Erro "+neuralNet.getMap().getWinner().getError());
            }
            ct++;
            
            
        }
        System.out.println("ativados "+ativados.size());
    }*/

}

