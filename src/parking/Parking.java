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
     * Returns the vehicle involved in this parking activity.
     *
     * @return the vehicle involved in the parking activity
     */
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * Returns the timestamp at which the vehicle entered the parking deck.
     *
     * @return the entry timestamp
     */
    public Timestamp getEnter() {
        return this.enter;
    }

    /**
     * Returns the timestamp at which the vehicle exited the parking deck.
     *
     * @return the exit timestamp, or null if the vehicle has not exited
     */
    public Timestamp getExit() {
        return this.exit;
    }

    /**
     * Set the timestamp when the vehicle exited the parking deck.
     *
     * @param t timestamp of exiting
     */
    public void setExit(Timestamp t) {
        this.exit = t;
    }


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
        if (this.exit == null){
            return this.vehicle.getPlate()
                    + " [entered: "
                    + this.enter.toString()
                    + "][exited: null]";
        } else {
            return this.vehicle.getPlate()
                    + " [entered: "
                    + this.enter.toString()
                    + "][exited: "
                    + this.exit.toString()
                    + "]";
        }
    }

    /** Maximum permitted parking duration in calendar days. */
    private static final int MAX_PARKING_DAYS = 2;

    /**
     * Validates the exit order and the maximum parking duration.
     *
     * @param timestamp the proposed exit timestamp
     * @throws IllegalArgumentException if exit precedes entry or exceeds two days
     */
    public void validateExit(Timestamp timestamp) {
        if (timestamp.compareTo(this.enter) < 0) {
            throw new IllegalArgumentException(
                    ErrorType.EXIT_BEFORE_ENTRY.format(
                            timestamp,
                            this.enter
                    )
            );
        }

        if (timestamp.compareTo(this.enter.plusDays(MAX_PARKING_DAYS)) > 0) {
            throw new IllegalArgumentException(
                    ErrorType.EXIT_EXCEEDS_TWO_DAYS.format()
            );
        }
    }
}
