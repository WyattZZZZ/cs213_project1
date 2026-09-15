package parking;

/**
 * Represents a parking deck.
 *
 * @author wyattzhang
 */
public class Deck {

    /** Maximum permitted capacity of any parking deck. */
    public static final int MAXCAPACITY = 6;

    /** The deck identification number. */
    private int number;

    /** The deck's location. */
    private Location location;

    /** The deck's operating hours. */
    private Hour hour;

    /** The parking activities currently in the deck. */
    private Parking[] parkings;

    /** The number of vehicles currently parked. */
    private int numParked;

    /** Whether the deck is open. */
    private boolean open;

    /**
     * Creates a parking deck.
     *
     * @param number the deck number
     * @param location the deck location
     * @param hour the operating-hours schedule
     * @param capacity the deck capacity
     */
    public Deck(int number, Location location, Hour hour, int capacity) {
        this.number = number;
        this.location = location;
        this.hour = hour;
        this.parkings = new Parking[capacity];
        this.numParked = 0;
        this.open = true;
    }

    /**
     * Finds the parking activity belonging to a vehicle.
     *
     * @param vehicle the vehicle to find
     * @return the index of the vehicle, or -1 if it is not found
     */
    private int find(Vehicle vehicle) {
        return -1;
    }

    /**
     * Records a vehicle entering the deck.
     *
     * @param parking the parking activity to add
     */
    public void enter(Parking parking) {
        // TODO: Add the parking activity.
    }

    /**
     * Records a vehicle exiting the deck.
     *
     * @param parking the parking activity to remove
     */
    public void exit(Parking parking) {
        // TODO: Remove the parking activity.
    }

    /**
     * Returns a textual representation of this deck.
     *
     * @return the deck's information
     */
    @Override
    public String toString() {
        return "";
    }
}