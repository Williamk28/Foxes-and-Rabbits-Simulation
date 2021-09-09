import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing Hunters, foxes, rabbits, fruits and poisonous berries.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 125;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 75;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.003;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.005; 
    // The probability that a fruit will be created in any given grid position.
    private static final double Fruit_Creation_Probability = 0.04;
    // The probability of a hunter spawning at the field
    private static final double Hunter_Creation = 0.001;
    // The probability of a poisonous berry spawning in the field
    private static final double Poison_Creation = 0.01;
    
    // List of predators/preys in the field.
    private List<PredatorPrey> PredatorPrey;
    
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        PredatorPrey = new ArrayList<PredatorPrey>(); //Initialises the array list 
        field = new Field(depth, width); //Initialises the field with the depth and width

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        //Sets the colour for each class of predator/prey
        view.setColor(Rabbit.class, Color.orange); //Rabbit will be orange
        view.setColor(Fox.class, Color.blue); //Fox will be blue
        view.setColor(Fruit.class,Color.magenta); //Fruit will be magenta
        view.setColor(Hunter.class,Color.black); //Hunter will be black
        view.setColor(PoisonousBerry.class,Color.red); //Poisonous Berry will be red
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * predator/prey
     */
    public void simulateOneStep()
    {
        step++;
        // Provide space for newborn predators/prey.
        List<PredatorPrey> newPredatorPrey = new ArrayList<PredatorPrey>();  
        for(Iterator<PredatorPrey> it = PredatorPrey.iterator(); it.hasNext(); ) { //For each loop where it loops if there is another item in the iterator
            PredatorPrey pred = it.next(); //Sets pred to a predator/prey in the list
            pred.act(newPredatorPrey); //This makes the pred do stuff like move around 
            if(! pred.isAlive()) { //If the predator/prey is not alive then remove it from the field
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        PredatorPrey.addAll(newPredatorPrey);

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0; //Sets the steps to 0
        PredatorPrey.clear(); //Clears the list of all the predator/prey
        populate(); //Populate the whole field with predaots/preys
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    //This gets the current step
    public int getStep() {
    	return step;
    }
    
    /**
     * Randomly populate the field with predators/preys.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom(); //gets a random number and sets it to rand
        field.clear(); //Clears the field
        for(int row = 0; row < field.getDepth(); row++) { //For Loop where it loops if row is less than the depth of the field. Each loop it iterates the row
            for(int col = 0; col < field.getWidth(); col++) { //For Loop where it loops if col si less than the width of the field. Each Loop it iterates the col
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) { //If rand is less than or equal to the fox creation probability then
                    Location location = new Location(row, col); //Get the current location from row and col
                    Fox fox = new Fox(true, field, location); //Creates a fox with a random age in the current field and location
                    PredatorPrey.add(fox); //Add the fox the list of PredatorPrey list
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) { //If rand is less than the rabbit creation probibility then 
                    Location location = new Location(row, col); //Get the current location from row and col
                    Rabbit rabbit = new Rabbit(true, field, location); //Creates a rabbit with a random age in the current field and locatiom
                    PredatorPrey.add(rabbit); //Adds the rabbit to the list of Predator/Prey
                }
                else if(rand.nextDouble() <= Fruit_Creation_Probability) { //If rand is less than the fruit creation probability then
                    Location location = new Location(row, col); //Get the current location from row and col
                    Fruit fruit = new Fruit(field, location); //Creates a fruit in the current field and location
                    PredatorPrey.add(fruit); //Add the fruit to the list of Predator/Prey
                }
                else if(rand.nextDouble() <= Hunter_Creation) { //If rand is less than the Hunter creation then 
                	Location location = new Location(row, col); //Get the current location from row and col
                    Hunter hunter = new Hunter(field, location); //Creates a Hunter in the current field and location
                    PredatorPrey.add(hunter);  //Add the hunter to the list of Predator/Prey
                }
                else if(rand.nextDouble() <= Poison_Creation) { //If rand is less than the poisonous berry creation then
                	Location location = new Location(row, col); //Get the current location from the row and col
                    PoisonousBerry poison = new PoisonousBerry(field, location); // Creates a poisonous berry in the current field and location
                    PredatorPrey.add(poison); //Add the poisonous berry to the list of Predator/Prey
                	}
      
                // else leave the location empty.
            }
        }
    }
}
