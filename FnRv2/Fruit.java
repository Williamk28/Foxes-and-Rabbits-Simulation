import java.util.List;
import java.util.Random;
/**
 * This class allows the Hunters, Foxes and Rabbit to eat fruits
 * Contains the fruits age and random spawn
 */
public class Fruit extends PredatorPrey {
	private static final int Max_Age = 60; //The maximum age before the fruit rots
	private static final int NewSeed_Age = 12; //The age where it can plant a new seed
	private static final Random rand = Randomizer.getRandom(); //Random number generator for randomly spawning fruits in the field
	private static final double NewSeed_Probability = 0.08;
	
	private int Age; //The age of the fruit	
	
	//This creates a fruit at a location in the field
	public Fruit(Field field, Location location) {
		//This gets the field and location from the PredatorPrey super class
		super(field,location); 
		Age = rand.nextInt(Max_Age); //Sets the age of the fruit to 0
	}
	
	//The fruit can only age or sprout a new fruit
	public void act(List<PredatorPrey> newFruit) {
		IncrementAge(); //Increases the age of the fruit by calling a method
		if(isAlive()) {
			Sprouts(newFruit); //Checks if the seed can grow into a fruit
		}
	}
	
	//This increases the fruits age
	private void IncrementAge() {
		Age = Age + 1; //Increments the age of the fruit by 1
		if (Age >= Max_Age) { //If Age is greater than Max_Age then make the fruit rot
			setDead(); //Fruit rots
		}
	}
	
	/**
     * Check whether or not the fruit is able to plant a new seed
     * New fruit will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void Sprouts(List<PredatorPrey> newFruit)
    {
        // New fruits will be sprouted into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField(); //This gets the field of the predator/prey
        List<Location> free = field.getFreeAdjacentLocations(getLocation()); //This gets all the free adjacent locations from it current location and sets it to free.
        int seeds = No_Seeds();  //Checks the amount of seeds by calling a method
        for(int counter = 0; counter < seeds && free.size() > 0; counter++) { //For loop where it loops if counter is less than seeds and the size of free is greater than 0
            Location loc = free.remove(0); //Removes the first location from the list
            Fruit fruit = new Fruit(field, loc); //Sets the new location and field for the fruit 
            newFruit.add(fruit); //Add the fruit to the list newFruit
        }
    }
        
    /**
     * Generate a number representing the number of seeds if the fruit is greater than the NewSeed_Age
     * @return The number of seeds (may be zero).
     */
    private int No_Seeds()
    {
        int seeds = 0;
        if(Age >= NewSeed_Age && rand.nextDouble() <= NewSeed_Probability) { //If the fruits age is greater than the NewSeed_Age AND rand is less than the NewSeed_Probability then seeds = 1
            seeds = rand.nextInt(5);
        }
        return seeds;
    }

	
}
