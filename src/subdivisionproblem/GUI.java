package subdivisionproblem;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class controls the GUI and calls the algorithms to be run
 * @author Tamati Rudd 18045626
 */
public class GUI extends JPanel implements ActionListener {
    private static Land testLand;
    private static JFrame frame;
    private LandPanel landPanel;
    private JPanel resultsPanel;
    private JButton subdivideButton;
    private JLabel bruteForceLabel;
    private JTextField bruteForceResult;
    private JLabel greedyLabel;
    private JTextField greedyResult;
    private JLabel exactLabel;
    private JTextField exactResult;
    
    /**
     * Construct the GUI
     */
    public GUI() {
        landPanel = new LandPanel();
        GridLayout grid = new GridLayout(testLand.width, testLand.height);
        landPanel.setLayout(grid);
        frame.add(landPanel, BorderLayout.NORTH);
        
        resultsPanel = new JPanel();
        resultsPanel.setBackground(Color.white);
        subdivideButton = new JButton("Subdivide Land");
        subdivideButton.addActionListener(this);
        resultsPanel.add(subdivideButton);
        
        bruteForceLabel = new JLabel("Brute Force Value:");
        resultsPanel.add(bruteForceLabel);
        bruteForceResult = new JTextField();
        bruteForceResult.setColumns(4);
        bruteForceResult.setEditable(false);
        resultsPanel.add(bruteForceResult);
        
        greedyLabel = new JLabel("Greedy Value:");
        resultsPanel.add(greedyLabel);
        greedyResult = new JTextField();
        greedyResult.setColumns(4);
        greedyResult.setEditable(false);
        resultsPanel.add(greedyResult);
        
        exactLabel = new JLabel("Exact Value:");
        resultsPanel.add(exactLabel);
        exactResult = new JTextField();
        exactResult.setColumns(4);
        exactResult.setEditable(false);
        resultsPanel.add(exactResult);
        
        frame.add(resultsPanel, BorderLayout.SOUTH);
    }
    /**
     * Entry point of the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testLand = new Land(6, 6); //Change this value to change land size
        frame = new JFrame("Subdivision Problem"); 
        frame.getContentPane().setBackground(Color.white);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(600, testLand.height*100+78));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GUI());
        frame.setResizable(false);
        frame.pack(); 
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2, (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }  

    /**
     * When the subdivide button is clicked:
     * - Run each of the three algorithms
     * - Update the GUI with their values
     * - Print tree information to the console
     * - Repaint the GUI with the subdivisions (if any)
     * @param e event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        BruteForce bruteForce = new BruteForce(testLand);
        Land optimalSubdivision = bruteForce.runAlgorithm();
        System.out.println("\nBRUTE FORCE: Optimal Solution:");
        System.out.println(optimalSubdivision);
        System.out.println("Value: $"+optimalSubdivision.getValue());
        System.out.println("Subdivisions: "+bruteForce.possibleSubdivisions.size());
        bruteForceResult.setText("$"+optimalSubdivision.getValue());
        
        Greedy greedy = new Greedy(testLand);
        Land greedySubdivision = greedy.runAlgorithm();
        System.out.println("\nGREEDY: Greedy Solution:");
        System.out.println(greedySubdivision);
        System.out.println("Value: $"+greedySubdivision.getValue()); 
        greedyResult.setText("$"+greedySubdivision.getValue());

        Exact exact = new Exact(testLand);
        Land exactSubdivision = exact.runAlgorithm();
        System.out.println("\nEXACT: Exact Solution:");
        System.out.println(exactSubdivision);
        System.out.println("Value: $"+exactSubdivision.getValue()); 
        exactResult.setText("$"+exactSubdivision.getValue());
        
        subdivideButton.setEnabled(false);
        testLand.clear();
        testLand = exactSubdivision.clone();
        frame.repaint();
    }

    /**
     * Inner panel class used to display the Land plot and its subdivisions
     */
    private class LandPanel extends JPanel {
        
        public LandPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(testLand.width*100, testLand.height*100));
        }
        
        /**
         * Paint the LandPanel
         * @param g Graphics object
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4));
            drawLand(testLand, g2, 0, 0);      
        }
        
        /**
         * Recursively draw the Land plot and subdivisions (if any)
         * @param landToDraw the Land object to draw
         * @param g2 Graphics 2D object
         * @param widthPos X coordinate of the top left of the Rectangle
         * @param heightPos Y coordinate of the top left of the Rectangle
         */
        public void drawLand(Land landToDraw, Graphics2D g2, int widthPos, int heightPos) {
            //Draw the Land plot as a rectangle
            g2.drawRect(widthPos, heightPos, landToDraw.width*100, landToDraw.height*100);
            
            //If the Land has subdivisions, draw them, otherwise draw text showing the size of the subdivision
            if (landToDraw.hasLeftChild() && landToDraw.hasRightChild()) {
                if (landToDraw.height != landToDraw.rightChild.height) { //Horizontal split
                    //Compute position of the bottom rectangle, and draw both subdivisions
                    int bottomHeightPos = heightPos + (landToDraw.height - landToDraw.rightChild.height) * 100;
                    drawLand(landToDraw.leftChild, g2, widthPos, heightPos);
                    drawLand(landToDraw.rightChild, g2, widthPos, bottomHeightPos);
                            
                } else if (landToDraw.width != landToDraw.rightChild.width) { //Vertical split
                    //Compute position of the right rectangle, and draw both subdivisions
                    int rightWidthPos = widthPos + (landToDraw.width - landToDraw.rightChild.width) * 100;
                    drawLand(landToDraw.leftChild, g2, widthPos, heightPos);
                    drawLand(landToDraw.rightChild, g2, rightWidthPos, heightPos);
                }
            } else { //If the Land has no further subdivisions, draw text showing the subdivision size
                String text = "  "+landToDraw.width+"m x "+landToDraw.height+"m";
                FontMetrics fm = g2.getFontMetrics();
                int x = widthPos + (landToDraw.width / 2);
                int y = heightPos + (landToDraw.height / 2);
                g2.drawString(text, x, y + fm.getAscent());
            }
        }
    }
}
