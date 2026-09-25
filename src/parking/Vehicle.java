package parking;

/**
 * Represents a registered vehicle with its license plate and parking history.
 *
 * @author Ethan Vu
 */
public class Vehicle {
    private static final int PLATE_LENGTH = 7;
    private static final int FIRST_HYPHEN_INDEX = 3;

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
     * Validates if a license plate matches the required format "Xdd-XXX".
     * Format rules: 7 total characters, index 0 is a letter, index 1-2 are digits,
     * index 3 is a hyphen, and index 4-6 are letters.
     *
     * @param plate the license plate string to validate
     * @return true if valid format; false otherwise
     */
    public static boolean isValidPlate(String plate) {
        if (plate == null || plate.length() != PLATE_LENGTH) {
            return false;
        }
        if (plate.charAt(FIRST_HYPHEN_INDEX) != '-') {
            return false;
        }
        if (!Character.isLetter(plate.charAt(0))) {
            return false;
        }
        if (!Character.isDigit(plate.charAt(1)) || !Character.isDigit(plate.charAt(2))) {
            return false;
        }
        for (int i = 4; i < PLATE_LENGTH; i++) {
            if (!Character.isLetter(plate.charAt(i))) {
                return false;
            }
        }
        return true;
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
     * Adds a parking activity to the vehicle's history in descending order.
     *
     * @param parking the parking activity to insert
     */
    public void addHistory(Parking parking) {
        History newNode = new History(parking);
        if (history == null) {
            history = newNode;
            return;
        }
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

    /**
     * Validates a plate while preserving the original token in errors.
     *
     * @param plate the plate to validate
     * @throws IllegalArgumentException if the plate format is invalid
     */
    public static void validatePlate(String plate) {
        if (!isValidPlate(plate)) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_PLATE.format(plate)
            );
        }
    }

    /**
     * Prints this vehicle's history with its heading and footer.
     */
    public void printHistoryReport() {
        if (!this.hasHistory()) {
            System.out.println(
                    ErrorType.NO_PARKING_HISTORY.format(this.plate)
            );
            return;
        }

        System.out.println(
                "** Parking history for " + this.plate + "**"
        );
        this.printHistory();
        System.out.println(
                "** end of list **"
        );
    }
}
