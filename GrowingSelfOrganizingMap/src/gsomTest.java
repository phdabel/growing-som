import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import ufrgs.maslab.gsom.learning.GSOMLearning;
import ufrgs.maslab.gsom.network.GrowingSelfOrganizingMap;
import ufrgs.maslab.gsom.norm.LogTransformation;
import ufrgs.maslab.gsom.norm.Template;
import ufrgs.maslab.gsom.util.Position;
import ufrgs.maslab.gsom.util.visualization.SelfOrganizingMapGraph3D;


public class gsomTest {

	public static void main(String[] args) {
/*		
		double[] featureImportance1 = new double[6];
		double[] featureImportance2 = new double[6];
		
		featureImportance1[0] = 0.43;
		featureImportance1[1] = 0.17;
		featureImportance1[2] = 0.10;
		featureImportance1[3] = 0.15;
		featureImportance1[4] = 0.05;
		featureImportance1[5] = 0.10;

		featureImportance2[0] = 0.43;
		featureImportance2[1] = 0.10;
		featureImportance2[2] = 0.30;
		featureImportance2[3] = 0.07;
		featureImportance2[4] = 0.05;
		featureImportance2[5] = 0.05;
		
		DataSet smoothingSet = new DataSet(6,1);
		smoothingSet.addRow(new DataSetRow(new double[]{4,1,3,530,1590,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{4,2,2,330,660,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{4,2,4,630,2520,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{3,2,3,570,1710,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{2,2,1,140,140,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{1,1,4,300,1200,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{1,2,4,450,1800,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{1,2,2,400,800,1}, new double[]{1}));
		smoothingSet.addRow(new DataSetRow(new double[]{0,2,2,600,1200,0}, new double[]{0}));
		smoothingSet.addRow(new DataSetRow(new double[]{0,2,5,400,2000,0}, new double[]{0}));
		smoothingSet.addRow(new DataSetRow(new double[]{0,0,2,600,1200,0}, new double[]{0}));
		smoothingSet.addRow(new DataSetRow(new double[]{0,1,5,400,2000,0}, new double[]{0}));
		smoothingSet.addRow(new DataSetRow(new double[]{0,2,2,600,1200,0}, new double[]{0}));
		smoothingSet.normalize(new LogTransformation());
		smoothingSet.shuffle();
		
		DataSet trainingSet = new DataSet(6,1);

		trainingSet.addRow(new DataSetRow(new double[]{4,2,4,630,2520,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{4,1,3,530,1590,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{4,2,2,330,660,1}, new double[]{1}));
		
		trainingSet.addRow(new DataSetRow(new double[]{3,0,2,100,200,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{3,2,3,570,1710,1}, new double[]{1}));
		
		trainingSet.addRow(new DataSetRow(new double[]{2,2,3,500,1500,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{2,2,5,700,3500,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{2,2,3,140,420,1}, new double[]{1}));
		
		trainingSet.addRow(new DataSetRow(new double[]{1,2,2,150,300,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{1,2,4,450,1800,1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{1,2,2,400,800,1}, new double[]{1}));
		
		trainingSet.addRow(new DataSetRow(new double[]{0,2,3,400,1200,0}, new double[]{1}));
		
		trainingSet.addRow(new DataSetRow(new double[]{0,2,2,600,1200,0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{0,2,5,400,2000,0}, new double[]{0}));
		
		
		DataSet testSet = new DataSet(6,1);
		testSet.addRow(new DataSetRow(new double[]{4,2,5,200,1000,1}, new double[]{1}));
		testSet.addRow(new DataSetRow(new double[]{3,1,2,200,400,1}, new double[]{1}));
		testSet.addRow(new DataSetRow(new double[]{0,0,1,400,400,0}, new double[]{1}));
		testSet.addRow(new DataSetRow(new double[]{2,0,2,400,800,1}, new double[]{1}));
		testSet.addRow(new DataSetRow(new double[]{1,2,3,500,1500,1}, new double[]{1}));
		testSet.normalize(new LogTransformation());
		//invertColumn(testSet, 1);
		
		ArrayList<Integer> dim = new ArrayList<Integer>();
        
        dim.add(2);
        dim.add(2);
        
        GrowingSelfOrganizingMap gsom = new GrowingSelfOrganizingMap(trainingSet, dim);
        DataSet initializationSet = new DataSet(6,1);
        for(int i = 0; i < gsom.getStructure().getMapSize(); i++)
        {
        	initializationSet.addRow(randomTask());
        }
        for(int i = 0; i < 10; i++)
        {
        	trainingSet.addRow(randomTask());
        }
        //invertColumn(trainingSet, 1);
		
        initializationSet.normalize(new LogTransformation());
        //invertColumn(initializationSet,1);
        
        GSOMLearning learning = new GSOMLearning(gsom);
        learning.weightFeature1 = featureImportance1;
        learning.weightFeature2 = featureImportance2;
        learning.getNeuralNetwork().getStructure().setInitializationDataSet(initializationSet);
        learning.initialize();
        
        learning.learn();
        gsom.getStructure().cleanNetwork();
        gsom.creatingLabels();
        SelfOrganizingMapGraph3D gsomMap = new SelfOrganizingMapGraph3D(gsom.getStructure());
        testNeuralNetwork(testSet, learning);
		JFrame f = new JFrame();
		f.add(gsomMap);
		f.setSize(1024,768);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		*/

	}
	
	public static void testNeuralNetwork(DataSet ds, GSOMLearning gsom){
		for(DataSetRow row : ds.getRows())
		{
			double[] extinguish = new double[ds.getInputSize()];
			gsom.calculate(row.getInput());
			System.out.println("Input: ");
			int ct = 0;
			for(double d : row.getInput())
			{
				extinguish[ct] = d;
				System.out.print(d+", ");
				ct++;
			}
			System.out.println();
			System.out.println("Winner:");
			Position p1 = gsom.getNeuralNetwork().getStructure().getWinner();
			System.out.println(p1.getAxisPosition());
			System.out.println();
			extinguish[0] = 0.0d;
			extinguish[5] = 0.0d;
			gsom.calculate(extinguish);
			Position p2 = gsom.getNeuralNetwork().getStructure().getWinner();
			System.out.println("Winner without flames: "+p2.getAxisPosition());
			System.out.println("Disaster Distance: "+euclidianDistance(p1,p2));
			
			
		}
		
		
	}
	
	/*public static void invertColumn(DataSet ds, int column)
	{
		ds.normalize(new LogTransformation());
		for(DataSetRow row : ds.getRows())
		{
			row.getInput()[column] = (1 - row.getInput()[column]);
		}
	}*/
	
	public static double euclidianDistance(Position p1, Position p2)
	{
		double diff = 0.0;
		for(int k = 0; k < p1.getDimension(); k++)
		{
			diff += Math.pow((p1.getAxisPosition().get(k) - p2.getAxisPosition().get(k)),2);
		}
		return Math.sqrt(diff);
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

}
