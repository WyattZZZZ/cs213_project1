package parking;

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
     * Determines whether a deck-number token contains only digits.
     *
     * @param token the deck-number token
     * @return true if the token contains only digits; false otherwise
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

    /**
     * Changes the operating status of this deck.
     *
     * @param open true to open the deck; false to close it
     */
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
        for (int i = 0; i < this.numParked; i++){
            if (this.parkings[i].getVehicle().equals(vehicle)) {
                return i;
            }
        }
        return NOTFOUND;
    }


    /**
     * Returns the current parking activity for a vehicle.
     *
     * @param vehicle the vehicle to locate
     * @return the current parking activity, or null if the vehicle is absent
     */
    public Parking getParking(Vehicle vehicle) {
        int index = this.find(vehicle);
        return index == NOTFOUND ? null : this.parkings[index];
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

    /**
     * Parses a deck number using the error message for the current command.
     *
     * @param token the deck number token
     * @param error the invalid-number error for this command
     * @return the parsed number
     * @throws IllegalArgumentException if the number is malformed or too large
     */
    public static int parseNumber(
            String token,
            ErrorType error
    ) {
        if (isValidDeckNumber(token)) {
            try {
                return Integer.parseInt(token);
            } catch (NumberFormatException exception) {
                // Out-of-range numbers cannot identify a stored deck.
            }
        }
        throw new IllegalArgumentException(
                error.format(token)
        );
    }

    /**
     * Parses capacity and checks the maximum allowed capacity.
     *
     * @param token the capacity token
     * @return the valid capacity
     * @throws IllegalArgumentException if capacity is invalid or exceeds the maximum
     */
    public static int parseCapacity(String token) {
        if (token == null || !token.matches("[0-9]+")) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_CAPACITY.format(token)
            );
        }

        try {
            int capacity = Integer.parseInt(token);
            if (capacity > MAXCAPACITY) {
                throw new IllegalArgumentException(
                        ErrorType.CAPACITY_EXCEEDS_MAXIMUM.format(token)
                );
            }
            return capacity;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_CAPACITY.format(token)
            );
        }
    }

    /**
     * Creates a deck after validating location, hours, and capacity in order.
     *
     * @param number the validated deck number
     * @param city the location token
     * @param hourCode the operating-hours token
     * @param capacityToken the capacity token
     * @return the new open deck
     * @throws IllegalArgumentException if a supplied property is invalid
     */
    public static Deck create(
            int number,
            String city,
            String hourCode,
            String capacityToken
    ) {
        Location location = Location.findByCity(city);
        if (location == null) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_LOCATION.format(city)
            );
        }

        Hour hour = Hour.getHour(hourCode);
        if (hour == null) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_OPERATION_HOURS.format(hourCode)
            );
        }
        return new Deck(number, location, hour, parseCapacity(capacityToken));
    }

    /**
     * Checks whether this deck can accept another vehicle.
     *
     * @throws IllegalArgumentException if this deck is closed or full
     */
    public void validateEntry() {
        if (!this.open) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_CLOSED_FOR_PARKING.format(this.number)
            );
        }

        if (this.numParked == this.parkings.length) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_FULL.format(this.number)
            );
        }
    }

    /**
     * Records entry after the caller checks registration and other decks.
     *
     * @param vehicle the registered vehicle
     * @param timestamp the entry timestamp
     * @throws IllegalArgumentException if deck state or operating hours prevent entry
     */
    public void recordEntry(
            Vehicle vehicle,
            Timestamp timestamp
    ) {
        this.validateEntry();
        this.hour.validate(timestamp, true);
        this.enter(new Parking(vehicle, timestamp));
    }

    /**
     * Completes parking, removes it from this deck, and records vehicle history.
     * All validation happens before any state is changed.
     *
     * @param vehicle the vehicle to locate
     * @param timestamp the exit timestamp
     * @return the completed parking activity
     * @throws IllegalArgumentException if the vehicle is absent or exit is invalid
     */
    public Parking recordExit(
            Vehicle vehicle,
            Timestamp timestamp
    ) {
        Parking parking = this.getParking(vehicle);
        if (parking == null) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_NOT_IN_DECK.format(vehicle.getPlate())
            );
        }
        this.hour.validate(timestamp, false);
        parking.validateExit(timestamp);
        parking.setExit(timestamp);
        this.exit(parking);
        parking.getVehicle().addHistory(parking);
        return parking;
    }
}
