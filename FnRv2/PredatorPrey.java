import java.util.List;

/**
 * A class representing shared characteristics of Predators and Preys.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public abstract class PredatorPrey
{
    //Checks if the predator/prey is alive
    private boolean alive;
    //The predators/preys field
    private Field field;
    //The position of the predator/prey in the field
    private Location location;
    
    /**
     * Create a new predator/prey at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public PredatorPrey(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this predator/prey act - that is: make it do
     * whatever it wants/needs to do.
     * @param newpredator/preys A list to receive newly born predator/preys.
     */
    abstract public void act(List<PredatorPrey> newPredators);

    /**
     * Check whether the predator/prey is alive or not.
     * @return true if the predator/prey is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the predator/prey is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the predator/prey's location.
     * @return The predator/prey's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the predator/prey at the new location in the given field.
     * @param newLocation The predator/prey's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the predator/prey's field.
     * @return The predator/prey's field.
     */
    protected Field getField()
    {
        return field;
    }

}
