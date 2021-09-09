import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class SimulatorView extends JFrame implements ActionListener
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;
    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;
    // Creates a string for the amount of steps done 
    private final String STEP_PREFIX = "Step: ";
    // Creates a string for the population for each class of predator/prey
    private final String POPULATION_PREFIX = "Population: ";
    //Creates a label for stepLabel, population
    private JLabel stepLabel, population;
    //Creates a fieldview for us to see the simulation
    private FieldView fieldView;    
    
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new FieldStats(); //Initialises the fieldstats
        colors = new LinkedHashMap<Class, Color>(); //Initialises the linked HashMap

        setTitle("Predators and Preys Simulation"); //Sets the title for the simulation
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER); //Initialises the label and sets its position
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER); //Initialises the label and sets its position
        
        setLocation(600, 0); //Sets the location of the simulation
        
        fieldView = new FieldView(height, width);    //Initialises the field view
    	
        Container contents = getContentPane(); //Container that stores the contents for the frame
        contents.add(stepLabel, BorderLayout.NORTH); //Add the label to the container and set its position
        contents.add(fieldView, BorderLayout.CENTER); //Add the fieldview to the container and set its poisiton
        contents.add(population, BorderLayout.SOUTH); //Add the label to the container and set its position

        pack(); //Makes everything fit in the simulator
        setVisible(true); //Makes the simulator visible

    }
    
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class PredatorPreyClass, Color color)
    {
        colors.put(PredatorPreyClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class PredatorPreyClass)
    {
        Color col = colors.get(PredatorPreyClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if(!isVisible()) { //If the simulation is not visible then set it visible
            setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step); //This will show the current step of the simulation
        stats.reset(); //Resets the stats
        
        fieldView.preparePaint(); 
        //This paints the colour of each predator/prey class to the simulator
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object PredatorPrey = field.getObjectAt(row, col);
                if(PredatorPrey != null) {
                    stats.incrementCount(PredatorPrey.getClass());
                    fieldView.drawMark(col, row, getColor(PredatorPrey.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
   
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
    
	public void actionPerformed(ActionEvent event) {
	}

}
