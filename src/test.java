import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.neuroph.contrib.nnet.SelfOrganizingMap;
import org.neuroph.contrib.util.Position;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class test {

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
		trainingSet.addRow(new DataSetRow(new double[]{0,1,1,0,1,0,0,0,1,1,0,0,2,1,0,0}, new double[]{0}));
        ArrayList<Integer> dim = new ArrayList<Integer>();
        dim.add(10);
        dim.add(10);
        
        // create perceptron neural network
        SelfOrganizingMap som = new SelfOrganizingMap(16, dim);
        // learn the training set
        som.learn(trainingSet);
        // test perceptron
        System.out.println("Testing trained perceptron");
        testNeuralNetwork(som, trainingSet);
        // save trained perceptron
        som.save("som.nnet");
        
        //@SuppressWarnings("deprecation")
		//SelfOrganizingMap loadedPerceptron = (SelfOrganizingMap) SelfOrganizingMap.load("som.nnet");
        // test loaded neural network
        //System.out.println("Testing loaded perceptron");
        //testNeuralNetwork(loadedPerceptron, trainingSet);
		
	}
	
	/**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param testSet data set used for testing
     */
    public static void testNeuralNetwork(SelfOrganizingMap neuralNet, DataSet testSet) {
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
            	//System.out.print("Neuronio "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            	//System.out.println(" Erro "+neuralNet.getMap().getWinner().getError());
            }
            ct++;
            /*
            DataSetRow trainingElement2 = testSet.getRowAt(17);
            neuralNet.setInput(trainingElement2.getInput());
            neuralNet.calculate();
            
            System.out.println("Veado: " + Arrays.toString(trainingElement2.getInput()) );
            System.out.println("vencedor "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            neuralNet.showTwoDimensionalGrid();
            
            
            DataSetRow trainingElement3 = testSet.getRowAt(46);
            neuralNet.setInput(trainingElement3.getInput());
            neuralNet.calculate();
            
            System.out.println("lagosta: " + Arrays.toString(trainingElement3.getInput()) );
            System.out.println("vencedor "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            neuralNet.showTwoDimensionalGrid();
            
            DataSetRow trainingElement4 = testSet.getRowAt(53);
            neuralNet.setInput(trainingElement4.getInput());
            neuralNet.calculate();
            
            System.out.println("polvo: " + Arrays.toString(trainingElement4.getInput()) );
            System.out.println("vencedor "+neuralNet.getMap().getWinner().getPosition().getAxisPosition());
            neuralNet.showTwoDimensionalGrid();*/
            
        }
        System.out.println("ativados "+ativados.size());
    }

}
