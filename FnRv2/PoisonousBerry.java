import java.util.List;
import java.util.Random;
/**
 * This class allows anyone to eat the poisonous berries
 */
public class PoisonousBerry extends PredatorPrey {
	private static final int Max_Age = 70; //The maximum age before the poisonous berry rots
	private static final int NewSeed_Age = 14; //The age where it can plant a new seed
	private static final Random rand = Randomizer.getRandom(); //Random number generator for randomly spawning fruits in the field
	private static final double NewSeed_Probability = 0.008;
	
	private int Age; //The age of the fruit
	
	//This creates a new poisonous berry
	public PoisonousBerry(Field field, Location location) {
		//This gets the field and location from the Predator/Prey superclass
		super(field,location); 
		Age = rand.nextInt(Max_Age); //Sets the age of the poisonous berry to 0
	}
	//This increments the age of the poisonous berry and checks if it can sprout a new poisonous berry
	public void act(List<PredatorPrey> newPB) { 
		IncrementAge();
		if(isAlive()) {
			Sprouts(newPB); //Rare for the berry to sprout 
		}

	}
	
	//This increases the age for the poisonous berry
	private void IncrementAge() {
		Age = Age + 1; //Increments the age of the fruit by 1
		if (Age >= Max_Age) { //If Age is greater than Max_Age then make the poisonous berry rotten
			setDead();
		}
	}
	
	
	/**
     * Check whether or not the poisonous berry can sprout a new poisonous berry
     * New poisonous berry will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void Sprouts(List<PredatorPrey> newPB)
    {
        // New poisonous berry are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField(); //This gets the field for the predator/prey
        List<Location> free = field.getFreeAdjacentLocations(getLocation()); //This gets a list of the free adjacent location from its current location
        int seeds = No_Seeds(); //Checks the number of seeds it has
        for(int counter = 0; counter < seeds && free.size() > 0; counter++) { //For Loop where it loops if the counter is less than the seeds AND the size of free is greater than 0. It increments counter at the end of each loop
            Location loc = free.remove(0); //Removes the first location from the list
            PoisonousBerry PB = new PoisonousBerry(field, loc); //Creates a new poisonous berry
            newPB.add(PB); //Adds the new poisonous berry to the list newPB
        }
    }
        
    /**
     * Generate a number representing the number of seeds,
     * @return The number of seeds (may be zero).
     */
    private int No_Seeds()
    {
        int seeds = 0;
        if(Age >= NewSeed_Age && rand.nextDouble() <= NewSeed_Probability) {
            seeds = 1; //This only allows one seed to be sprouted
        }
        return seeds;
    }
	
}
