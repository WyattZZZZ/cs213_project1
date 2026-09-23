package parking;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Represents a parking deck.
 *
 * @author wyattzhang
 */
public class Deck {

    /** Maximum permitted capacity of any parking deck. */
    public static final int MAXCAPACITY = 6;

    /** Value returned when a parking activity is not found. */
    private static final int NOTFOUND = -1;

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
    private ExecutionException executionException;

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
     * Check if the ID number is valid
     *
     * @return the deck number
     */
    public static boolean isValidDeckNumber(String token) {
        return token != null
                && token.matches("^[0-9]+$");
    }

    /**
     * Returns the identification number of this deck.
     *
     * @return the deck number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Returns the location of this deck.
     *
     * @return the deck location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Returns the operating-hours schedule of this deck.
     *
     * @return the operating-hours schedule
     */
    public Hour getHour() {
        return this.hour;
    }

    /**
     * Returns the array containing the current parking activities.
     *
     * @return the array of parking activities
     */
    public Parking[] getParkings() {
        return this.parkings;
    }

    /**
     * Returns the number of vehicles currently parked in this deck.
     *
     * @return the number of parked vehicles
     */
    public int getNumParked() {
        return this.numParked;
    }

    /**
     * Determines whether this deck is currently open.
     *
     * @return true if the deck is open; false otherwise
     */
    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Finds the parking activity belonging to a vehicle.
     *
     * @param vehicle the vehicle to find
     * @return the index of the vehicle, or -1 if it is not found
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < this.parkings.length; i++){
            if (Objects.equals(this.parkings[i].getVehicle().getPlate(),
                    vehicle.getPlate())) {
                return i;
            }
        }
        return NOTFOUND;
    }


    /**
     * Adds a parking activity to this deck.
     *
     * @param parking the parking activity to add
     */
    public void enter(Parking parking) {
        if (parking == null) {
            return;
        }
        if (this.numParked >= this.parkings.length) {
            return;
        }
        if (this.find(parking.getVehicle()) != -1) {
            return;
        }

        this.parkings[this.numParked] = parking;
        this.numParked++;
    }

    /**
     * Removes a parking activity from this deck.
     *
     * @param parking the parking activity to remove
     */
    public void exit(Parking parking) {
        if (parking == null || this.numParked == 0) {
            return;
        }

        int parkingIndex = this.find(parking.getVehicle());

        if (parkingIndex == -1) {
            return;
        }

        int lastIndex = this.numParked - 1;

        this.parkings[parkingIndex] = this.parkings[lastIndex];
        this.parkings[lastIndex] = null;
        this.numParked--;
    }

    /**
     * Returns a textual representation of this deck.
     *
     * @return the deck's information
     */
    @Override
    public String toString() {
        String vehicleWord =
                this.numParked == 1
                        ? " vehicle"
                        : " vehicles";

        return "Deck#"
                + this.number
                + "@"
                + this.location
                + "[open "
                + this.hour
                + "] [capacity "
                + this.parkings.length
                + "] ["
                + this.numParked
                + vehicleWord
                + "] ["
                + this.location.getCounty()
                + "]";
    }
}