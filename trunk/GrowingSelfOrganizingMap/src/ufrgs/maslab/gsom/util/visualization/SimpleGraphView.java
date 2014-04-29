package ufrgs.maslab.gsom.util.visualization;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import javax.swing.JFrame;

public class SimpleGraphView {
    public Graph<Integer, String> g;

    public SimpleGraphView() {       
        this.g = new SparseMultigraph<Integer, String>();
        this.g.addVertex(1);
        this.g.addVertex(2);
        this.g.addVertex(4); 
    }

    public static void main(String[] args) {
        SimpleGraphView sgv = new SimpleGraphView(); 
        Layout<Integer, String> layout = new CircleLayout(sgv.g);
        layout.setSize(new Dimension(800,800));  
        BasicVisualizationServer<Integer,String> vv =
            new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(850,850)); 

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);       
    }
}