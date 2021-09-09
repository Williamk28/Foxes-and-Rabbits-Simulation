import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat food, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Fox extends PredatorPrey
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 28;
    // The age to which a fox can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    //The food value of a fruit.
    private static final int Fruit_Food_Value = 10;
    
    // Individual characteristics (instance fields).
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;
    // Checks if the fox is hungry
    private boolean hungry = false;
    // Checks if the Fox has been infected by the poison
    private boolean infected = false;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(field, location); //Gets the fox location and field from its superclass
        if(randomAge) { //If the random age is true then set a random age from 0 to 125
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE | Fruit_Food_Value); //FoodLevel is random between Rabbit or Fox food level
        }
        else { //This is a newborn fox
            age = 0; //Set the age for the new born to 0
            //Set the food level to 15
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * food. In the process, it might breed, die of hunger,
     * die of old age or die from poison.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<PredatorPrey> newFoxes)
    {
        incrementAge(); //This increases the foxes age by calling a method
        Infected(); //This checks if the fox is infected by calling a method
        incrementHunger(); //This increases the foxes hunger by calling a method
        if(isAlive()) { //If isAlive is true then
            giveBirth(newFoxes); //Try to give birth   
            Location move = getField().freeAdjacentLocation(getLocation()); //This lets the fox move around into free spaces adjacent to its current location
            if(move != null) {
            	setLocation(move); //Move to the new location if its empty
            }
            Hunger(); //This checks if the fox is hungry. It can only hunt when its hungry
            // Move towards a source of food if the fox is hungry.
            if (hungry == true) {  //If the fox is hungry then
            	Location newLocation = findFood(); //Call findFood method and set the location to newLocation
                if(newLocation == null) {  //If location is null then there is no food found
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                if(newLocation != null) { //If the location is not null then move to the new location
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
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
        	setDead();
        }
    }
    //This method checks if the fox is infected by the poison
    private void Infected() {
    	if(infected == true) { //If infected is true then the fox dies
    		setDead();
    	}
    }
    
    /**
     * Look for food adjacent to the current location.
     * Only the first food is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField(); //Gets the predator/prey field
        List<Location> adjacent = field.adjacentLocations(getLocation()); // This sets adjacent to a list of all its adjacent location 
        Iterator<Location> it = adjacent.iterator(); //This iterates through the adjacent list
        while(it.hasNext()) { //WHile there is another item in the adjacent list
            Location where = it.next(); // Set where to the current adjacent location
            Object food = field.getObjectAt(where);  //Sets food to the current predator/prey of the location at where
            if(food instanceof Rabbit) { //If food is Rabbit then the fox eats the rabbit
            	Rabbit rabbit = (Rabbit) food; 
                if(rabbit.isAlive()) { 
                	rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE; //Replenishes its foodLevel back to 15
                    //Remove the dead rabbit from the field.
                    return where;
                    }
                }
                else if(food instanceof Fruit) { //If food is Fruit then the fox eats the fruit
                	foodLevel = Fruit_Food_Value; //Replenishes its foodLevel back to 9 
                	Fruit fruit = (Fruit) food;
                	fruit.setDead();
                	return where; //Remove the fruit from the field
                }
                else if(food instanceof PoisonousBerry) { //If food is Poisonus Berry then the fox will becoem infected
                	infected = true;
                	PoisonousBerry poison = (PoisonousBerry) food;
                	poison.setDead();
                	return where; //Removes the poisonus berry from the field
                }
            }   
        return null;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<PredatorPrey> newFoxes)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField(); //This gets the predator/preys field
        List<Location> free = field.getFreeAdjacentLocations(getLocation()); //Sets free to all free adjacent locations from its current location
        int births = breed(); //Sets births to breed method
        for(int counter = 0; counter < births && free.size() > 0; counter++) { //For Loop where it loops if b is less than births AND the size of free is greater than 0. Increments b by 1 for each loop
            Location loc = free.remove(0); //This removes the first item from the list in free and sets it to loc
            Fox young = new Fox(false, field, loc); //A new baby fox is born 
            newFoxes.add(young); //Adds the baby fox to newFoxes list
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
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) { //If the fox is greater than the breeding age and rand is less than the breeding probability then
            births = rand.nextInt(MAX_LITTER_SIZE) + 1; // Random number between 1 and its max litter size plus 1
        }
        return births; //Returns the number of births
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE; //Checks if the age is greater than the breeding age
    }
    
    //This method is used to determine whether the fox is feeling hungry
    private void Hunger() {
    	if(foodLevel <= 6) { //If the foodLevel is less than or equal to 3 then set hungry to true
    		hungry = true; //This lets the fox to eat food.
    	}
    }
}
