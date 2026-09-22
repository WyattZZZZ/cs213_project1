package parking;

import java.lang.*;

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
        this.decks = new Deck[ARRAYLENGTH];
        this.numDecks = 0;
    }

    /**
     * Finds a deck in the array.
     *
     * @param deck the deck to find
     * @return the deck's index, or NOTFOUND if it is absent
     */
    private int find(Deck deck) {
        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].getNumber() == deck.getNumber()) {
                return i;
            }
        }
        return NOTFOUND;
    }

    /**
     * Increases the array capacity by ARRAYLENGTH.
     */
    private void grow() {
        Deck[] new_decks = new Deck[this.decks.length + ARRAYLENGTH];
        for (int i = 0; i < this.decks.length; i++){
            new_decks[i] = this.decks[i];
        }
        this.decks = new_decks;
    }

    /**
     * Opens a deck or adds it to the array.
     *
     * @param deck the deck to open
     */
    public void open(Deck deck) {
        if (this.numDecks == this.decks.length){
            this.grow();
        }
        if (this.find(deck) != -1){
            deck.setOpen(true);
        } else {
            this.decks[this.numDecks] = deck;
        }
        this.numDecks++;
    }

    /**
     * Marks a deck as closed.
     *
     * @param deck the deck to close
     */
    public void close(Deck deck) throws IllegalArgumentException{
        if (this.find(deck) != -1){
            deck.setOpen(false);
        } else {
            throw new IllegalArgumentException("Deck not found.");
        }

    }

    /**
     * Determines whether a deck is in the list.
     *
     * @param deck the deck to locate
     * @return true if the deck exists; false otherwise
     */
    public boolean contains(Deck deck) {
        return this.find(deck) != -1;
    }

    /**
     * Compare by county and then by deck number
     *
     * @param a the first deck to compare
     * @param b the second deck to compare
     */
    private static int compareCountyThenId(Deck a, Deck b) {
        int countyResult = a.getLocation().getCounty()
                .compareToIgnoreCase(b.getLocation().getCounty());
        if (countyResult != 0) {
            return countyResult;
        }
        if (a.getNumber() < b.getNumber()) {
            return -1;
        }
        if (a.getNumber() > b.getNumber()) {
            return 1;
        }
        return 0;
    }

    /**
     * Sorts decks by county and then by deck number
     * using selection sort.
     */
    private void sortByLocation() {
        for (int i = 0; i < this.numDecks - 1; i++) {
            int smallest = i;

            for (int j = i + 1; j < this.numDecks; j++) {
                if (compareCountyThenId(this.decks[j], this.decks[smallest]) < 0) {
                    smallest = j;
                }
            }

            if (smallest != i) {
                Deck temporary = this.decks[i];
                this.decks[i] = this.decks[smallest];
                this.decks[smallest] = temporary;
            }
        }
    }

    /**
     * Prints open decks ordered by county and then deck number.
     */
    public void printByLocation() {
        sortByLocation();

        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].isOpen()) {
                System.out.println(this.decks[i]);
            }
        }
    }

    /**
     * Sorts plates by strings
     * using selection sort.
     */
    private void sortVehicles(String[] plates) {
        for (int i = 0; i < plates.length; i++){
            int min = i;

            for (int j = i + 1; j < plates.length; j++){
                if (plates[i].compareTo(plates[j]) < 0){
                    min = j;
                }
            }

            if (min != i) {
                String temporary = plates[i];
                plates[i] = plates[min];
                plates[min] = temporary;
            }
        }
    }


    /**
     * Prints vehicles parked in a specified deck.
     *
     * @param deck the specified parking deck
     */
    public void printVehicles(Deck deck) {
        Parking[] parkings = deck.getParkings();
        String[] plates = new String[parkings.length];
        for (int i = 0; i < parkings.length; i++) {
            plates[i] = parkings[i].getVehicle().getPlate();
        }
        sortVehicles(plates);
        for (String s : plates) {
            System.out.println(s);
        }

    }
}