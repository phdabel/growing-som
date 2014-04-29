package ufrgs.maslab.gsom.util.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JApplet;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

import ufrgs.maslab.gsom.layer.CompetitiveLayer;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.Position;

public class GSOM2D extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6663057585351231558L;
	
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	
	private static final Dimension DEFAULT_SIZE = new Dimension(600,600);
	
	private JGraphModelAdapter modelAdapter;
	
	ListenableGraph<Neuron, DefaultEdge> g;
	
	JGraph graph;
	
	public GSOM2D(CompetitiveLayer<Neuron> map){
		this.g = new ListenableUndirectedGraph<Neuron, DefaultEdge>(DefaultEdge.class);
		this.modelAdapter = new JGraphModelAdapter(this.g);
		this.graph = new JGraph(this.modelAdapter);
		
		this.graph.setBounds(-100000, -100000, 100000, 100000);
		adjustDisplaySettings(this.graph);
		getContentPane().add(this.graph);
		resize(DEFAULT_SIZE);
		this.configureGraph(map);
		this.init();
	}
	
	public void configureGraph(CompetitiveLayer<Neuron> map)
	{
		for(Neuron n : map.values())
		{
			this.g.addVertex(n);
			this.positionVertexAt(n, n.getPosition().getAxisPosition().get(0), n.getPosition().getAxisPosition().get(1));
		}
		int ct = 0;
		for(Neuron n : map.values())
		{
			for(Position p : n.getNeighbours())
			{
				//Number edge = this.g.findEdge(n, map.getNeuronAt(p));
				DefaultEdge edge = createDefaultEdge();
				//edge = this.g.getEdgeFactory().createEdge(map.getNeuronAt(p),n);
				
				//edge.getAttributes().applyMap(this.createEdgeAttributes());
				this.g.addEdge(map.getNeuronAt(p),n, edge);
				
			}
		}
	}
	
	@Override
	public void init(){
				
	}

	private void adjustDisplaySettings(JGraph graph) {
		graph.setPreferredSize(DEFAULT_SIZE);
		Color c = DEFAULT_BG_COLOR;
		String colorStr = null;
		try{
			colorStr = getParameter("bgcolor");
		}catch(Exception e)
		{
			
		}
		
		if(colorStr != null)
		{
			c = Color.decode(colorStr);
		}
		graph.setBackground(c);
	}
		
	protected DefaultEdge createDefaultEdge() {
	    return new DefaultEdge();
	  }
	
	public Map createEdgeAttributes()
	{
		Map map = new Hashtable();
		GraphConstants.setLabelAlongEdge(map, false);
		return map;
	}
	
	private void positionVertexAt(Neuron vertex, int x, int y)
	{
		DefaultGraphCell cell = modelAdapter.getVertexCell(vertex);
		Map attr = cell.getAttributes();
		Rectangle2D b = GraphConstants.getBounds(attr);
		b.setRect(x*100, y*100, b.getWidth(), b.getHeight());
		GraphConstants.setBounds(attr, b);
		Map cellAttr = new HashMap();
		cellAttr.put(cell, attr);
		modelAdapter.edit(cellAttr, null, null, null);
		
	}
	

}
