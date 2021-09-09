import java.util.Iterator;
import java.util.List;

/*
 * The Hunter class is a Predator and hunts down everything including foxes
 * However, there are only a few of them
 * The hunter can only die to hunger or getting infected 
 */
public class Hunter extends PredatorPrey {
	//This is the food value when the hunter eats a fox
	private static final int Fox_Food_Value = 15;
	//This is the food value when the hunter eats a rabbit
	private static final int Rabbit_Food_Value = 10;
	//This is the food value when the hunter eats a fruit
	private static final int Fruit_Food_Value = 8;
	
	//The hunter's food level
	private int foodLevel;
	//Determines whether the hunter is hungry
	private boolean hungry = false;
	//Determines whether the hunter is infected by poison
	private boolean infected = false;
	
	
	//This creates a new fox in the field at a location with the Fox foodLevel
	public Hunter(Field field, Location location) {
		super(field,location);
		foodLevel = Fox_Food_Value;
	}
	
	//This decreases the foodLevel for the hunter
	private void IncrementHunger() {
		foodLevel = foodLevel - 1;
		if(foodLevel <=0) { //If the hunters food level is below or equal to 0 then it dies
	    setDead();
		}
	}
	
	//Determines if the hunter is hungry
	private void Hunger() {
		if (foodLevel <= 4) { //IF the hunters food level is blow 6 then hungry is set to true
			hungry = true;
		}
	}
	
	//If the hunter is infected then it dies and will be removed from the field
	private void Infected() {
		if(infected == true) {
			setDead();
		}
	}
	
	//This method allows the Hunter to find food
	private Location findFood()
    {
        Field field = getField(); //This gets the field predator/prey
        List<Location> adjacent = field.adjacentLocations(getLocation()); //This gets a list of all the free adjacent locations from its current location
        Iterator<Location> it = adjacent.iterator(); //This iterates through the list
        while(it.hasNext()) { //while there is an item next in the iterator
            Location where = it.next(); //Set the location where to the current location in the list 
            Object food = field.getObjectAt(where); //Gets the food from the location where 
            if(food instanceof Rabbit) { //If the food is a rabbit then the Hunter eats the rabbit
            	Rabbit rabbit = (Rabbit) food;
                if(rabbit.isAlive()) { 
                	rabbit.setDead();
                    foodLevel = Rabbit_Food_Value; //Replenishes its food level to 10
                    // Remove the dead rabbit from the field.
                    return where;
                   }
                }
            	else if(food instanceof Fox) { //If the food is a fox then the hunter eats the fox
            		Fox fox = (Fox) food;
            		if(fox.isAlive()) {
            			fox.setDead();
            			foodLevel = Fox_Food_Value; //Replenishes its food level to 15
            			return where; //Removes the dead fox from the field
            		}
            	}
                else if(food instanceof Fruit) { //If the food at the current location is fruit then
                	foodLevel = Fruit_Food_Value; //Replenishes the food level to 7
                	Fruit fruit = (Fruit) food;
                	fruit.setDead();
                	return where; //Remove the fruit from the field
                }
                else if(food instanceof PoisonousBerry) { //If the food at the current location is poisonous berry then
                	infected = true; //The hunter is infected 
                	PoisonousBerry poison = (PoisonousBerry) food;
                	poison.setDead();
                	return where; //Removes the poisonous berry from the field
                }
        }
        return null;
    }
	
	//The hunter moves around and when it is hungry it will hunt and eat anything other than itself
	public void act(List<PredatorPrey> newAnimals) {
		IncrementHunger(); //This increases the hunters hunger
		Infected(); //Checks if the hunter is infected by poison
		if(isAlive()) { //If the hunter is alive then
            Location move = getField().freeAdjacentLocation(getLocation()); //Get all of its free adjacent location from its current position
            if(move != null) { //Thi lets the hunter move to a new spot if the spot he is moving to is empty
            	setLocation(move);
            }
			Hunger(); //Checks if the hunter is hungry
			if (hungry == true) { //If the hunter is hungry then 
				Location newLocation = findFood(); //Set newLocaiton to findFood method
                if(newLocation == null) {  //If the newLocation is empty then there is no food 
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                //If the newLocation is not null then there is food so move to that location
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    setDead();
                }
			}
		}
		
	}
	
	
}
