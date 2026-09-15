package parking;

/**
 * Stores parking decks in a resizable array.
 *
 * @author wyattzhang
 */
public class DeckList {

    /** Initial capacity and growth amount of the array. */
    private static final int ARRAYLENGTH = 4;

    /** Value returned when a deck is not found. */
    private static final int NOTFOUND = -1;

    /** Array containing the parking decks. */
    private Deck[] decks;

    /** Number of decks currently stored. */
    private int numDecks;

    /**
     * Creates an empty deck list.
     */
    public DeckList() {
        decks = new Deck[ARRAYLENGTH];
        numDecks = 0;
    }

    /**
     * Finds a deck in the array.
     *
     * @param deck the deck to find
     * @return the deck's index, or NOTFOUND if it is absent
     */
    private int find(Deck deck) {
        return NOTFOUND;
    }

    /**
     * Increases the array capacity by ARRAYLENGTH.
     */
    private void grow() {
        // TODO: Resize without using System.arraycopy().
    }

    /**
     * Opens a deck or adds it to the array.
     *
     * @param deck the deck to open
     */
    public void open(Deck deck) {
        // TODO: Open or add the deck.
    }

    /**
     * Marks a deck as closed.
     *
     * @param deck the deck to close
     */
    public void close(Deck deck) {
        // TODO: Mark the deck as closed.
    }

    /**
     * Determines whether a deck is in the list.
     *
     * @param deck the deck to locate
     * @return true if the deck exists; false otherwise
     */
    public boolean contains(Deck deck) {
        return false;
    }

    /**
     * Prints open decks ordered by county and deck number.
     */
    public void printByLocation() {
        // TODO: Sort in place and print.
    }

    /**
     * Prints vehicles parked in a specified deck.
     *
     * @param deck the specified parking deck
     */
    public void printVehicles(Deck deck) {
        // TODO: Print vehicles ordered by plate.
    }
}