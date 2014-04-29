package ufrgs.maslab.gsom.util.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JRootPane;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import ufrgs.maslab.gsom.layer.CompetitiveLayer;
import ufrgs.maslab.gsom.neuron.Neuron;
import ufrgs.maslab.gsom.util.Position;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

public class GrowingSelfOrganizingMapGraph2D extends javax.swing.JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 233042281463844968L;
	
	private Graph<Neuron,Number> g = null;
	
	private CompetitiveLayer<Neuron> map;

    private VisualizationViewer<Neuron,Number> vv = null;

    private AbstractLayout<Neuron,Number> layout = null;

    Timer timer;

    boolean done;

    protected JButton switchLayout;

    public static final int EDGE_LENGTH = 50;
    
    public GrowingSelfOrganizingMapGraph2D(CompetitiveLayer<Neuron> map)
    {
    	this.map = map;
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
				//Number edge = this.g.findEdge(n, map.getNeuronAt(p));
				this.g.addEdge(ct, map.getNeuronAt(p),n);
				ct++;
			}
		}
	}
    
    @Override
    public void init() {
    	
        //create a graph
    	Graph<Neuron,Number> ig = Graphs.<Neuron,Number>synchronizedUndirectedGraph(new UndirectedSparseGraph<Neuron,Number>());

        ObservableGraph<Neuron,Number> og = new ObservableGraph<Neuron,Number>(ig);
        
        og.addGraphEventListener(new GraphEventListener<Neuron,Number>() {

			@Override
			public void handleGraphEvent(GraphEvent<Neuron, Number> evt) {
				System.err.println("got "+evt);

			}});
        
        this.g = og;
        //create a graphdraw

    	this.configureGraph(map);
        this.animateGraph();
        
        JRootPane rp = this.getRootPane();
        rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(java.awt.Color.white);
        getContentPane().setFont(new Font("Arial", Font.BOLD, 16));

        AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<Neuron, Number>();
        
        vv.setGraphMouse(graphMouse);
        graphMouse.add(new MousePlugin());


        vv.addKeyListener(graphMouse.getModeKeyListener());
        
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Neuron>());
        vv.setForeground(Color.black);

        //JPanel windows = new JPanel();
        
        
        vv.addComponentListener(new ComponentAdapter() {

			/**
			 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentResized(ComponentEvent arg0) {
				super.componentResized(arg0);
				System.err.println("resized");
				layout.setSize(arg0.getComponent().getSize());
			}});

        getContentPane().add(vv);
        switchLayout = new JButton("Switch to SpringLayout");
        switchLayout.addActionListener(new ActionListener() {

            @Override
			@SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent ae) {
            	Dimension d = vv.getSize();//new Dimension(600,600);
                if (switchLayout.getText().indexOf("Spring") > 0) {
                    switchLayout.setText("Switch to FRLayout");
                    layout =
                    	new SpringLayout<Neuron,Number>(g, new ConstantTransformer(EDGE_LENGTH));
                    layout.setSize(d);
            		Relaxer relaxer = new VisRunner((IterativeContext)layout);
            		relaxer.stop();
            		relaxer.prerelax();
            		StaticLayout<Neuron,Number> staticLayout =
            			new StaticLayout<Neuron,Number>(g, layout);
    				LayoutTransition<Neuron,Number> lt =
    					new LayoutTransition<Neuron,Number>(vv, vv.getGraphLayout(),
    							staticLayout);
    				Animator animator = new Animator(lt);
    				animator.start();
    			//	vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
    				vv.repaint();

                } else {
                    switchLayout.setText("Switch to SpringLayout");
                    layout = new FRLayout<Neuron,Number>(g, d);
                    layout.setSize(d);
            		Relaxer relaxer = new VisRunner((IterativeContext)layout);
            		relaxer.stop();
            		relaxer.prerelax();
            		StaticLayout<Neuron,Number> staticLayout =
            			new StaticLayout<Neuron,Number>(g, layout);
    				LayoutTransition<Neuron,Number> lt =
    					new LayoutTransition<Neuron,Number>(vv, vv.getGraphLayout(),
    							staticLayout);
    				Animator animator = new Animator(lt);
    				animator.start();
    			//	vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
    				vv.repaint();

                }
            }
        });

        getContentPane().add(switchLayout, BorderLayout.SOUTH);
        
        //modfica dados do vertex
        //this.vv.getRenderContext().setVertexLabelTransformer(new VertexTransformer(vv.getPickedVertexState()));
        
		vv.doLayout();
		

        
        timer = new Timer();
    }
    
    @Override
    public void start() {
        validate();
        //set timer so applet wfinalill change
        //timer.schedule(new RemindTask(), 1000, 1000); //subsequent rate
        vv.repaint();
    }

    Integer v_prev = null;

    public void process(Neuron n) {

    	vv.getRenderContext().getPickedVertexState().clear();
    	vv.getRenderContext().getPickedEdgeState().clear();
        try {
        	
            //add a vertex
        	
            g.addVertex(n);
            vv.getRenderContext().getPickedVertexState().pick(n, true);

            // wire it to some edges
            
            for(Position p : n.getNeighbours())
            {
            	Integer edge = g.getEdgeCount();
            	vv.getRenderContext().getPickedEdgeState().pick(edge, true);
            	List<Neuron> neuronL = new ArrayList<Neuron>(g.getVertices());
            	for(Neuron someNeuron : neuronL)
            	{
            		if(someNeuron.getPosition().equals(p))
            		{
            			g.addEdge(edge, someNeuron, n);
            		}
            	}
            }
            this.animateGraph();

        	 
        } catch (Exception e) {
            System.out.println(e);

        }
    }
    
    public void animateGraph()
    {
    	//create a graphdraw
        this.layout = new SpringLayout<Neuron,Number>(this.g,new ConstantTransformer(EDGE_LENGTH));
        this.layout.setSize(new Dimension(600,600));
        this.layout.initialize();
		Relaxer relaxer = new VisRunner((IterativeContext)layout);
		relaxer.stop();
		relaxer.prerelax();
		
		Layout<Neuron,Number> staticLayout =
			new StaticLayout<Neuron,Number>(this.g);
		

        this.vv = new VisualizationViewer<Neuron,Number>(staticLayout, new Dimension(600,600));
		
		LayoutTransition<Neuron,Number> lt =
				new LayoutTransition<Neuron,Number>(this.vv, this.vv.getGraphLayout(),
						staticLayout);
		
		Animator animator = new Animator(lt);
		animator.start();
		this.vv.repaint();

    }
    /*
    class RemindTask extends TimerTask {

        @Override
        public void run() {
            process();
            if(done) cancel();

        }
    }*/
    
    private static class VertexTransformer implements Transformer<Neuron, String>{
    	
    	private final PickedInfo<Neuron> pi;
    	
    	public VertexTransformer(PickedInfo<Neuron> pi)
    	{
    		this.pi = pi;
    	}
    	
    	@Override
    	public String transform(Neuron n)
    	{
    		if(pi.isPicked(n))
    			return n.toString();
    		else
    			return "nada";
    	}
    	
    }
    
    protected class MousePlugin extends TranslatingGraphMousePlugin implements MouseListener
    {
    	@Override
    	public void mouseClicked(final MouseEvent e)
    	{
    		@SuppressWarnings("unchecked")
			final VisualizationViewer<Neuron, Number> v = (VisualizationViewer<Neuron,Number>)e.getSource();
    		
    		final PickedState<Neuron> p = v.getPickedVertexState();
    		
    		
    		p.addItemListener(new ItemListener(){

    			@Override
    			public void itemStateChanged(ItemEvent e) {
    				// TODO Auto-generated method stub
    				Object neuron = e.getItem();
    				if(neuron instanceof Neuron)
    				{
    					Neuron n = (Neuron)neuron;
    					if(p.isPicked(n)){
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
    						System.out.print(" Feature Value: ");
    						for(double fv : n.attributeValue)
    						{
    							System.out.print(fv+", ");
    						}
    						System.out.println();
    						System.out.println("Hits: "+n.getHits());
    						System.out.print(" Counter "+n.getSpreadCounter());
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

    		
    	}
    }

    /*
    public static void main(String[] args) {
    	GrowingSelfOrganizingMapGraph2D and = new GrowingSelfOrganizingMapGraph2D();
    	JFrame frame = new JFrame();
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().add(and);

    	and.init();
    	and.start();
    	frame.pack();
    	frame.setVisible(true);
    }*/


}
