package ufrgs.maslab.gsom.util.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import org.apache.commons.collections15.Transformer;

import ufrgs.maslab.gsom.layer.CompetitiveLayer;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.Position;
import edu.uci.ics.jung.algorithms.layout3d.SpringLayout;
import edu.uci.ics.jung.graph.AbstractTypedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization3d.PluggableRenderContext;
import edu.uci.ics.jung.visualization3d.VisualizationViewer;

public class SelfOrganizingMapGraph3D extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 15L;
	
	private AbstractTypedGraph<Neuron, Number> g =  
            new UndirectedSparseGraph<Neuron, Number>();
	
	
	public SelfOrganizingMapGraph3D(CompetitiveLayer<Neuron> map)
	{
		super(new BorderLayout());
		this.configureGraph(map);
		
		Graph<Neuron,Number> graph = this.g;
		SpringLayout.LengthFunction<Number> f = new SpringLayout.UnitLengthFunction(50);
		
		final SpringLayout<Neuron,Number> layout = new SpringLayout<Neuron,Number>(graph,f);
		
		for(Neuron n : map.values())
		{
			Point3f p = new Point3f();
			p.x = n.getPosition().getAxisPosition().get(0);
			p.y = n.getPosition().getAxisPosition().get(1);
			if(n.getPosition().getAxisPosition().size() == 3)
			{
				p.z = n.getPosition().getAxisPosition().get(2);
			}else{
				p.z = 0;
			}
			layout.setLocation(n, p);
			//layout.setRepulsionRange(0);
			//layout.setStretch(0d);
			/*while(!layout.done())
			{
				
			}*/
			//layout.transform(n);
			//layout.lock(n, true);
			
			
		}

		layout.initialize();

		PluggableRenderContext<Neuron, Integer> render = new PluggableRenderContext<Neuron, Integer>();
		VisualizationViewer<Neuron,Number> vv = 
				new VisualizationViewer<Neuron,Number>();
				
		
		final PickedState<Neuron> p = vv.getRenderContext().getPickedVertexState();
		p.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				Object neuron = e.getItem();
				if(neuron instanceof Neuron)
				{
					Neuron n = (Neuron)neuron;
					if(p.isPicked(n)){
						for(Position k : n.distanceBetweenNodes.keySet())
						{
							System.out.println("Neuron: "+k.getAxisPosition()+" path distance: "+n.distanceBetweenNodes.get(k));
						}
						for(Position k : n.distanceBetweenJunctions.keySet())
						{
							System.out.println("Neuron: "+k.getAxisPosition()+" junction distance: "+n.distanceBetweenJunctions.get(k));
						}
						
						System.out.print("Neuron: "+n.getPosition().getAxisPosition());
						System.out.print(" Neighbours: ");
						for(Position p : n.getNeighbours())
						{
							System.out.print(" "+p.getAxisPosition()+", ");
						}
						
						System.out.println();
						System.out.print(" Weight: ");
						for(double w: n.getWeights())
						{
							System.out.print(w+", ");
						}
						System.out.println();
						/*
						System.out.print(" Feature Value: ");
						for(double fv : n.attributeValue)
						{
							System.out.print(fv+", ");
						}
						System.out.println();*/
						System.out.println("Hits: "+n.getHits());
						System.out.println("Counter "+n.getSpreadCounter());
						System.out.println("Labels: ");
						for(String l : n.label)
						{
							System.out.println(l);
						}
						System.out.println("Error: "+n.error);
						
					}
				}
				
			}
			
		});
		
		Transformer<Neuron, Point3f> vertexColor = new Transformer<Neuron, Point3f>(){
			@Override
			public Point3f transform(Neuron i){
				Point3f p = new Point3f();
				p.x = i.getPosition().getAxisPosition().get(0);
				p.y = i.getPosition().getAxisPosition().get(1);
				if(i.getPosition().getDimension() == 3){
					p.z = i.getPosition().getAxisPosition().get(2);
				}else
				{
					p.z = 0;
				}
				return p;
				/*
				float r = 0.0f;
				float g = 0.0f;
				float b = 1.0f;
				if(i.label.contains(0)){
					g = g * 0.2f;
					b = b * 0.3f;
				}else if(i.label.contains(1)){
					r = r * 0.1f;
					b = b * 0.3f;
				}else if(i.label.contains(2))
				{
					r = r * 0.1f;
					g = g * 0.2f;
				}else if(i.label.contains(3))
				{
					g = g * 0.2f;
					b = b * 0.3f;
				}else if(i.label.contains(4))
				{
					r = r * 0.1f;
					b = b * 0.3f;
				}else if(i.label.contains(5)){
					r = r * 0.1f;
					g = g * 0.2f;
				}
				System.out.println(r+" - "+b+" - "+g);
				Color3f color = new Color3f();
				color.set(r, g, b);
				return color;*/
				
			}
		};
		
		
		//vv.setSize(5000, 5000);
		Transformer<Neuron,Appearance> vertexShapeTransformer = new Transformer<Neuron,Appearance>() {
 			@Override
			public Appearance transform(Neuron n) {
 				
 				
 				Color3f lightGray = new Color3f(0.7f, 0.7f, 0.7f);
 				Color3f black = new Color3f(0,0,0);
 				Color3f red = new Color3f(1, 0, 0);
 				
 				
 				//Material redMaterial = new Material(red, black,red, red, 100.0f);
 				Material blackMaterial = new Material(lightGray, black,black, lightGray, 10.0f);
 				Appearance blackLook = new Appearance();
 				//Appearance redLook = new Appearance();
 				//redLook.setMaterial(redMaterial);
 				blackLook.setMaterial(blackMaterial);
 				//if(n.hits == 0)
 					return blackLook;
				//return redLook;
				
 			}
		};
		
		vv.getRenderContext().setVertexAppearanceTransformer(vertexShapeTransformer);
		vv.getRenderContext().setVertexStringer(new ToStringLabeller<Neuron>());
		
		
		//layout.setInitializer(vertexColor);
		for(Neuron n : layout.getVertices()){
			
			layout.transform(n);
			
		}
		

		//vv.setBackground(Color.gray);
		vv.setGraphLayout(layout);
		vv.doLayout();
		add(vv);
	}
	
	public void configureGraph(CompetitiveLayer<Neuron> map)
	{
		for(Neuron n : map.values())
		{
			this.g.addVertex(n);	
		}
		int ct = 0;
		for(Neuron n : map.values())
		{
			for(Position p : n.getNeighbours())
			{
				if(map.getNeuronAt(p) != null){
					Number edge = this.g.findEdge(n, map.getNeuronAt(p));
					this.g.addEdge(ct, map.getNeuronAt(p),n);
					ct++;
				}
			}
		}
	}


	public AbstractTypedGraph<Neuron, Number> getG() {
		return g;
	}


	public void setG(AbstractTypedGraph<Neuron, Number> g) {
		this.g = g;
	}
	

}
