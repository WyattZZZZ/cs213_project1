package parking;

/**
 * Maintains a resizable array for storing and managing registered vehicles.
 *
 * @author Ethan Vu
 */
public class VehicleList {
    private static final int LENGTH = 4;
    private static final int NOTFOUND = -1;

    private Vehicle[] vehicles;
    private int numVehicles;

    /**
     * Default constructor initializing an empty list with capacity of 4.
     */
    public VehicleList() {
        vehicles = new Vehicle[LENGTH];
        numVehicles = 0;
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
}