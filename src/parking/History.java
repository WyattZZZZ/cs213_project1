package parking;

/**
 * Represents a node in a singly linked list for vehicle parking history.
 *
 * @author Ethan Vu
 */
public class History {
    private Parking parking;
    private History next;

    /**
     * Parameterized constructor to initialize a History node.
     *
     * @param parking the parking activity to store in this node
     */
    public History(Parking parking) {
        this.parking = parking;
        this.next = null;
    }

    /**
     * Gets the parking activity stored in this node.
     *
     * @return the parking activity
     */
    public Parking getParking() {
        return parking;
    }

    /**
     * Gets the reference to the next node in the list.
     *
     * @return the next History node
     */
    public History getNext() {
        return next;
    }

    /**
     * Sets the reference to the next node in the list.
     *
     * @param next the next History node to set
     */
    public void setNext(History next) {
        this.next = next;
    }
}