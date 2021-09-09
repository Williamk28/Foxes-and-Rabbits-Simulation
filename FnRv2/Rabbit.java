import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, eat food and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Rabbit extends PredatorPrey
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 12;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 75;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.04;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The food value of a fruit 
    private static final int Fruit_Food_Value = 15;
    
    // Individual characteristics (instance fields).
    // The rabbit's age.
    private int age;
    //The rabbit's hunger level
    private int foodLevel;
    //Checks if the rabbit is hungry `
    private boolean hungry = false;
    //Checks if the rabbit is infected
    private boolean infected = false;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location); //Gets the field and location from its superclass Predator/Prey
        age = 0; //Sets the age of the new born rabbit to 0
        foodLevel = Fruit_Food_Value; //Sets the rabbit's food level to 7
        if(randomAge) { //If random age is true then set a random age for the rabbit between 0 and 75
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * Rabbits will run around, eat, breed or die
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<PredatorPrey> newRabbits)
    {
    	Infected(); //Checks if the rabbit is infected
        incrementAge(); //Increases the age of the rabbit 
        IncreaseHunger(); //Increases the hunger for the rabbit
        if(isAlive()) { //If the rabbit is alive then
            giveBirth(newRabbits); //Try to give birth 
            //Move around the field
            Location move = getField().freeAdjacentLocation(getLocation()); //Get a list of all its free adjacent location from its current location and sets it to move
            if(move != null) { //If move is not null then move to that new location
            	setLocation(move);
            }
            Hunger(); //Checks if the rabbit is hungry
            if (hungry == true) { //If the rabbit hungry then find food
            	// Try to move towards fruits in the field.
            	Location newLocation = findFood(); //Sets newLocation to the findFood method
            	if(newLocation == null) { //If the new location is null then there is no food
            	//If there is nothing in the new location then try and move to a new location in the field
            	newLocation = getField().freeAdjacentLocation(getLocation());
            	}
            	//If there is food in the new location then move to that new location
            	if(newLocation != null) {
            		setLocation(newLocation);
            	}
            	else {
            		// Overcrowding.
            		setDead();
            }
        }
    }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) { //If the age of the rabbit is greater than the max age then it dies
            setDead();
        }
    }
    
    //This method is use to decrease the food level for the rabbits
    private void IncreaseHunger() {
    	foodLevel = foodLevel - 1; //Remove 1 from the food level
    	if(foodLevel <= 0) { //If the foodLevel is less than or equal to 0 then the Rabbit dies
    		setDead();
    	}
    }
    
    //This method checks if the rabbit is infected by poison
    private void Infected() {
    	if(infected == true) { //If the rabbit is infected then it dies
    		setDead();
    	}
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<PredatorPrey> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField(); //This gets the field for the predator/prey
        List<Location> free = field.getFreeAdjacentLocations(getLocation()); //Gets the list of all the free adjacent locations from its current location
        int births = breed(); //set the number of births to the breed method
        for(int counter = 0; counter < births && free.size() > 0; counter++) { //For Loop where it loops when the counter is less than births and the size of free is greater than 0
            Location loc = free.remove(0); //Removes the first location from free
            Rabbit young = new Rabbit(false, field, loc); //A new baby rabbit is born
            newRabbits.add(young); //Add the new baby rabbit to the list newRabbits
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) { //If the rabbits age is greater than the breeding age AND rand is lower than the breeding probability then
            births = rand.nextInt(MAX_LITTER_SIZE) + 1; //Get a random integer between 0 and 4 and add 1
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    //This method allows the rabbit to find food
    private Location findFood() {
    	 Field field = getField(); //This gets the field predator/prey
    	 //Sets adjacent to field which holds a random list of adjacent locations based from its current location
         List<Location> adjacent = field.adjacentLocations(getLocation());
         //This iterates through the list of adjacent locations
         Iterator<Location> it = adjacent.iterator(); 
         //If there is another item(location) in the variable it then do the following
         while(it.hasNext()) {  //While there is another item in the iterator then
             Location where = it.next(); //Sets where to it which currently holds A free adjacent location
             Object food = field.getObjectAt(where); //This returns an animal or a fruit at the given location
             if(food instanceof Fruit) { //If food is fruit then replenish foodLevel to 13
            	 foodLevel = Fruit_Food_Value;
            	 Fruit fruit = (Fruit) food;
             	 fruit.setDead();
                 return where; //This removes the fruit from the field
             }
             else if(food instanceof PoisonousBerry) { //If the food at the current location is a poisonous berry then
                 infected = true; //The rabbit is now infected 
                 PoisonousBerry poison = (PoisonousBerry) food;
             	 poison.setDead();
                 return where; //Removes the poisonous berry from the field
             }
         }
         return null; //This returns null when there is no food or they are not at any adjacent location from the rabbit
    }      
    
    //This method is used to determine whether the rabbit is feeling hungry
    private void Hunger() {
    	if(foodLevel <= 5) { //If the foodLevel is less than or equal to 3 then set hungry to true
    		hungry = true;
    	}
    }
}
