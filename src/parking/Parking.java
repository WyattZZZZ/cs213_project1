package parking;

/**
 * Represents one parking activity.
 *
 * @author wyattzhang
 */
public class Parking {

    /** The vehicle involved in the parking activity. */
    private Vehicle vehicle;

    /** The timestamp at which the vehicle entered. */
    private Timestamp enter;

    /** The timestamp at which the vehicle exited. */
    private Timestamp exit;

    /**
     * Creates a parking activity without an exit timestamp.
     *
     * @param vehicle the vehicle entering the deck
     * @param enter the entry timestamp
     */
    public Parking(Vehicle vehicle, Timestamp enter) {
        this.vehicle = vehicle;
        this.enter = enter;
        this.exit = null;
    }

    /**
     * Returns a textual representation of the parking activity.
     *
     * @return the vehicle and its entry and exit timestamps
     */
    @Override
    public String toString() {
        return "";
    }
}