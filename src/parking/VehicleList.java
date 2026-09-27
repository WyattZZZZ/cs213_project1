package parking;

/**
 * Maintains a resizable array for storing and managing registered vehicles.
 *
 * @author Ethan Vu
 */
public class VehicleList {
    /** Initial capacity and growth amount of the vehicle array. */
    private static final int LENGTH = 4;

    /** Value returned when a vehicle is not found. */
    private static final int NOTFOUND = -1;

    /** Resizable array containing registered vehicles. */
    private Vehicle[] vehicles;

    /** Number of vehicles currently stored. */
    private int numVehicles;

    /**
     * Default constructor initializing an empty list with capacity of 4.
     */
    public VehicleList() {
        vehicles = new Vehicle[LENGTH];
        numVehicles = 0;
    }

    /**
     * Determines whether no vehicles are registered.
     *
     * @return true if the list is empty; false otherwise
     */
    public boolean isEmpty() {
        return this.numVehicles == 0;
    }

    /**
     * Checks if a specific license plate exists within a given vehicle list.
     *
     * @param vehicleList the VehicleList instance to search
     * @param plate the license plate string to search for
     * @return true if the plate exists in the list; false otherwise
     */
    public static boolean containsPlate(VehicleList vehicleList, String plate) {
        if (vehicleList == null || plate == null) {
            return false;
        }
        Vehicle searchTemplate = new Vehicle(plate);
        return vehicleList.contains(searchTemplate);
    }

    /**
     * Finds the index of a vehicle in the array.
     *
     * @param vehicle the vehicle object to locate
     * @return index of the vehicle if found; NOTFOUND (-1) otherwise
     */
    private int find(Vehicle vehicle) {
        for (int i = 0; i < numVehicles; i++) {
            if (vehicles[i].equals(vehicle)) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Resizes the vehicle array by increasing its capacity by 4.
     */
    private void grow() {
        Vehicle[] newVehicles = new Vehicle[vehicles.length + LENGTH];
        for (int i = 0; i < numVehicles; i++) {
            newVehicles[i] = vehicles[i];
        }
        vehicles = newVehicles;
    }

    /**
     * In-place selection sort algorithm to sort vehicles by plate alphabetically.
     */
    private void sortByPlate() {
        for (int i = 0; i < numVehicles - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < numVehicles; j++) {
                if (vehicles[j].getPlate().compareToIgnoreCase(vehicles[minIndex].getPlate()) < 0) {
                    minIndex = j;
                }
            }
            Vehicle temp = vehicles[minIndex];
            vehicles[minIndex] = vehicles[i];
            vehicles[i] = temp;
        }
    }

    /**
     * Retrieves the vehicle matching the given vehicle template.
     *
     * @param vehicle the template vehicle with plate to search for
     * @return the registered vehicle instance; null if not found
     */
    public Vehicle getVehicle(Vehicle vehicle) {
        int index = find(vehicle);
        if (index != NOTFOUND) {
            return vehicles[index];
        }
        return null;
    }

    /**
     * Adds a new vehicle to the end of the array.
     *
     * @param vehicle the vehicle object to add
     */
    public void add(Vehicle vehicle) {
        if (numVehicles == vehicles.length) {
            grow();
        }
        vehicles[numVehicles] = vehicle;
        numVehicles++;
    }

    /**
     * Removes a vehicle from the array by replacing it with the last element.
     *
     * @param vehicle the vehicle object to remove
     */
    public void remove(Vehicle vehicle) {
        int index = find(vehicle);
        if (index != NOTFOUND) {
            vehicles[index] = vehicles[numVehicles - 1];
            vehicles[numVehicles - 1] = null;
            numVehicles--;
        }
    }

    /**
     * Checks whether the vehicle list contains a specific vehicle.
     *
     * @param vehicle the vehicle object to check
     * @return true if found in the list; false otherwise
     */
    public boolean contains(Vehicle vehicle) {
        return find(vehicle) != NOTFOUND;
    }

    /**
     * Prints the list of registered vehicles ordered by license plate.
     */
    public void printByPlate() {
        if (numVehicles == 0) {
            return;
        }
        sortByPlate();
        for (int i = 0; i < numVehicles; i++) {
            System.out.println(vehicles[i].toString());
        }
    }

    /**
     * Displays parking history for all registered vehicles ordered by plate.
     */
    public void printHistory() {
        if (numVehicles == 0) {
            return;
        }
        sortByPlate();
        for (int i = 0; i < numVehicles; i++) {
            vehicles[i].printHistory();
        }
    }

    /**
     * Validates and registers a plate.
     *
     * @param plate the original plate token
     * @return the new registered vehicle
     * @throws IllegalArgumentException if the plate is invalid or already registered
     */
    public Vehicle register(String plate) {
        Vehicle.validatePlate(plate);
        if (containsPlate(this, plate)) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_ALREADY_REGISTERED.format(plate)
            );
        }
        Vehicle vehicle = new Vehicle(plate.toUpperCase());
        this.add(vehicle);
        return vehicle;
    }

    /**
     * Gets a registered vehicle after validating its plate.
     *
     * @param plate the original plate token
     * @param missing the error to use if no registered vehicle matches
     * @return the registered vehicle
     * @throws IllegalArgumentException if the plate is invalid or unregistered
     */
    public Vehicle requireVehicle(
            String plate,
            ErrorType missing
    ) {
        Vehicle.validatePlate(plate);
        Vehicle vehicle = this.getVehicle(new Vehicle(plate));
        if (vehicle == null) {
            throw new IllegalArgumentException(
                    missing.format(plate)
            );
        }
        return vehicle;
    }

    /**
     * Finds a registered vehicle that is not currently in any deck.
     *
     * @param plate the original plate token
     * @param decks the decks to check for current parking
     * @return the vehicle eligible to enter
     * @throws IllegalArgumentException if the plate is invalid, absent, or parked
     */
    public Vehicle requireEnteringVehicle(
            String plate,
            DeckList decks
    ) {
        Vehicle vehicle = this.requireVehicle(plate, ErrorType.VEHICLE_NOT_REGISTERED);
        if (decks.findDeckByVehicle(vehicle) != null) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_ALREADY_IN_DECK.format(plate)
            );
        }
        return vehicle;
    }

    /**
     * Removes a registered vehicle only if it is not parked and has no history.
     *
     * @param plate the original plate token
     * @param decks the decks to check for current parking
     * @return the removed vehicle
     * @throws IllegalArgumentException if the vehicle cannot be removed
     */
    public Vehicle unregister(
            String plate,
            DeckList decks
    ) {
        Vehicle vehicle = this.requireVehicle(plate, ErrorType.VEHICLE_NOT_FOUND_FOR_REMOVAL);
        if (decks.findDeckByVehicle(vehicle) != null) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_CURRENTLY_PARKED.format(plate)
            );
        }

        if (vehicle.hasHistory()) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_HAS_HISTORY.format(plate)
            );
        }
        this.remove(vehicle);
        return vehicle;
    }

    /**
     * Prints a complete registered-vehicle report.
     */
    public void printVehiclesReport() {
        if (this.isEmpty()) {
            System.out.println(
                    ErrorType.VEHICLE_LIST_EMPTY
            );
            return;
        }

        System.out.println(
                "** List of registered vehicles, ordered by license plate **"
        );
        this.printByPlate();
        System.out.println(
                "** end of list **"
        );
    }

    /**
     * Prints a complete history report for all registered vehicles.
     */
    public void printHistoryReport() {
        if (this.isEmpty()) {
            System.out.println(
                    ErrorType.DECK_LIST_EMPTY
            );
            return;
        }

        System.out.println(
                "** Parking history for all vehicles, ordered by plate/timestamp **"
        );
        this.printHistory();
        System.out.println(
                "** end of parking history **"
        );
    }
}
