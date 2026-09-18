package parking;

/**
 * Represents a registered vehicle with its license plate and parking history.
 *
 * @author Ethan Vu
 */
public class Vehicle {
    private String plate;
    private History history;

    /**
     * Parameterized constructor to create a vehicle with a license plate.
     *
     * @param plate the 7-character license plate string
     */
    public Vehicle(String plate) {
        this.plate = plate;
        this.history = null;
    }

    /**
     * Gets the license plate of the vehicle.
     *
     * @return the license plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Gets the head node of the vehicle's parking history linked list.
     *
     * @return the head History node
     */
    public History getHistory() {
        return history;
    }

    /**
     * Checks if the vehicle has any parking history recorded.
     *
     * @return true if history is not empty; false otherwise
     */
    public boolean hasHistory() {
        return history != null;
    }

    /**
     * Adds a parking activity to the vehicle's history in descending order of entry timestamp.
     *
     * @param parking the parking activity to insert
     */
    public void addHistory(Parking parking) {
        History newNode = new History(parking);
        if (history == null) {
            history = newNode;
            return;
        }

        // Uses Timestamp.compareTo() via parking.getEnter()
        if (parking.getEnter().compareTo(history.getParking().getEnter()) > 0) {
            newNode.setNext(history);
            history = newNode;
            return;
        }

        History current = history;
        while (current.getNext() != null
                && current.getNext().getParking().getEnter().compareTo(parking.getEnter()) >= 0) {
            current = current.getNext();
        }
        newNode.setNext(current.getNext());
        current.setNext(newNode);
    }

    /**
     * Prints the parking history of the vehicle to the terminal.
     */
    public void printHistory() {
        History current = history;
        while (current != null) {
            System.out.println(current.getParking().toString());
            current = current.getNext();
        }
    }

    /**
     * Compares if two vehicles are equal based on their license plates.
     *
     * @param obj the reference object with which to compare
     * @return true if the license plates match; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vehicle vehicle = (Vehicle) obj;
        return plate.equalsIgnoreCase(vehicle.plate);
    }

    /**
     * Returns the textual representation of the vehicle.
     *
     * @return the license plate string
     */
    @Override
    public String toString() {
        return plate;
    }
}