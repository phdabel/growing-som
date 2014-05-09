package ufrgs.maslab.gsom.util.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.vecmath.Point3f;

import ufrgs.maslab.gsom.layer.CompetitiveLayer;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.Position;
import edu.uci.ics.jung.algorithms.layout3d.SpringLayout;
import edu.uci.ics.jung.graph.AbstractTypedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization3d.VisualizationViewer;
import edu.uci.ics.jung.visualization3d.decorators.PickableVertexPaintTransformer;

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

		//PluggableRenderContext<Neuron, Integer> render = new PluggableRenderContext<Neuron, Integer>();
		
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
		
				
		
		
		vv.getRenderContext().setVertexStringer(new ToStringLabeller<Neuron>());
		
		for(Neuron n : layout.getVertices()){
			//vv.getRenderContext().getVertexAppearanceTransformer().transform(n);
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
