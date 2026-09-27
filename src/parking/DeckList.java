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
        if (deck == null) {
            return NOTFOUND;
        }

        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].getNumber()
                    == deck.getNumber()) {
                return i;
            }
        }

        return NOTFOUND;
    }

    /**
     * Increases the array capacity by ARRAYLENGTH.
     */
    private void grow() {
        Deck[] newDecks =
                new Deck[this.decks.length + ARRAYLENGTH];

        for (int i = 0; i < this.numDecks; i++) {
            newDecks[i] = this.decks[i];
        }

        this.decks = newDecks;
    }

    /**
     * Returns the backing array containing the stored decks.
     *
     * @return the deck array
     */
    public Deck[] getDecks() {
        return this.decks;
    }

    /**
     * Opens a deck or adds it to the array.
     *
     * @param deck the deck to open
     */
    public void open(Deck deck) {
        if (deck == null) {
            return;
        }

        int index = this.find(deck);

        if (index != NOTFOUND) {
            this.decks[index].setOpen(true);
            return;
        }

        if (this.numDecks == this.decks.length) {
            this.grow();
        }

        this.decks[this.numDecks] = deck;
        this.numDecks++;
    }

    /**
     * Marks a deck as closed.
     *
     * @param deck the deck to close
     */
    public void close(Deck deck) {
        int index = this.find(deck);

        if (index == NOTFOUND) {
            return;
        }

        this.decks[index].setOpen(false);
    }

    /**
     * Determines whether a deck is in the list.
     *
     * @param deck the deck to locate
     * @return true if the deck exists; false otherwise
     */
    public boolean contains(Deck deck) {
        return this.find(deck) != NOTFOUND;
    }

    /**
     * Returns the deck stored in the list that has the same number.
     *
     * @param number the deck to locate
     * @return the stored deck, or null if it does not exist
     */
    public Deck get(int number) {
        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].getNumber()
                    == number) {
                return this.decks[i];
            }
        }
        return null;
    }

    /**
     * Compare by county and then by deck number
     *
     * @param a the first deck to compare
     * @param b the second deck to compare
     * @return a negative value if a comes before b,
     *         a positive value if a comes after b,
     *         or zero if they have the same ordering
     */
    private static int compareCountyThenId(
            Deck a, Deck b) {

        int countyResult = a.getLocation()
                .getCounty()
                .compareToIgnoreCase(
                        b.getLocation().getCounty());

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
        for (int i = 0;
             i < this.numDecks - 1;
             i++) {

            int smallest = i;

            for (int j = i + 1;
                 j < this.numDecks;
                 j++) {

                if (compareCountyThenId(
                        this.decks[j],
                        this.decks[smallest]) < 0) {

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
        boolean hasOpenDeck = false;

        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].isOpen()) {
                hasOpenDeck = true;
                break;
            }
        }

        if (!hasOpenDeck) {
            System.out.println(
                    ErrorType.DECK_LIST_EMPTY);
            return;
        }

        this.sortByLocation();

        System.out.println(
                "** List of decks, ordered by "
                        + "county/deck number **");

        for (int i = 0; i < this.numDecks; i++) {
            if (this.decks[i].isOpen()) {
                System.out.println(this.decks[i]);
            }
        }

        System.out.println("** end of list **");
    }

    /**
     * Sorts plates by strings
     * using selection sort.
     *
     * @param parkings the array of parking activities
     * @param size the number of active parking activities
     */
    private void sortVehicles(
            Parking[] parkings, int size) {

        for (int i = 0; i < size - 1; i++) {
            int smallest = i;

            for (int j = i + 1; j < size; j++) {
                String currentPlate = parkings[j]
                        .getVehicle()
                        .getPlate();

                String smallestPlate = parkings[smallest]
                        .getVehicle()
                        .getPlate();

                if (currentPlate.compareToIgnoreCase(
                        smallestPlate) < 0) {

                    smallest = j;
                }
            }

            if (smallest != i) {
                Parking temporary = parkings[i];
                parkings[i] = parkings[smallest];
                parkings[smallest] = temporary;
            }
        }
    }

    /**
     * Prints vehicles parked in a specified deck.
     *
     * @param deck the specified parking deck
     */
    public void printVehicles(Deck deck) {
        int index = this.find(deck);

        if (index == NOTFOUND) {
            System.out.println(
                    ErrorType.DECK_NOT_FOUND.format(
                            deck.getNumber()));
            return;
        }

        Deck storedDeck = this.decks[index];
        Parking[] parkings =
                storedDeck.getParkings();

        int numberParked =
                storedDeck.getNumParked();

        this.sortVehicles(
                parkings, numberParked);

        System.out.println(
                "** List of vehicles in Deck# "
                        + storedDeck.getNumber()
                        + ", ordered by plate **");

        for (int i = 0; i < numberParked; i++) {
            System.out.println(parkings[i]);
        }

        System.out.println("** end of list **");
    }

    /**
     * Finds the deck in which a vehicle is currently parked.
     *
     * @param vehicle the vehicle to locate
     * @return the deck containing the vehicle,
     *         or null if the vehicle is not currently parked
     */
    public Deck findDeckByVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        for (int i = 0; i < this.numDecks; i++) {
            Parking[] parkings =
                    this.decks[i].getParkings();

            int numParked =
                    this.decks[i].getNumParked();

            for (int j = 0; j < numParked; j++) {
                Vehicle parkedVehicle =
                        parkings[j].getVehicle();

                if (parkedVehicle.equals(vehicle)) {
                    return this.decks[i];
                }
            }
        }

        return null;
    }

    /**
     * Determines whether the list contains a deck number.
     *
     * @param id the deck number
     * @return true if a matching deck exists; false otherwise
     */
    public Boolean contains(int id) {
        return this.get(id) != null;
    }

    /**
     * Returns an existing deck or reports its absence.
     *
     * @param token the deck number token
     * @return the stored deck
     * @throws IllegalArgumentException if the number is invalid or absent
     */
    public Deck requireDeck(String token) {
        int number = Deck.parseNumber(
                token,
                ErrorType.INVALID_DECK_NUMBER_CHARACTERS
        );
        Deck deck = this.get(number);
        if (deck == null) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_NOT_FOUND.format(number)
            );
        }
        return deck;
    }

    /**
     * Returns an existing open deck.
     *
     * @param token the deck number token
     * @return the open deck
     * @throws IllegalArgumentException if the number is invalid, absent, or closed
     */
    public Deck requireOpenDeck(String token) {
        Deck deck = this.requireDeck(token);
        if (!deck.isOpen()) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_CLOSED_FOR_PARKING.format(deck.getNumber())
            );
        }
        return deck;
    }

    /**
     * Validates a plate and finds its current deck without checking registration.
     *
     * @param plate the plate token
     * @return the deck containing this vehicle
     * @throws IllegalArgumentException if the plate is invalid or is not parked
     */
    public Deck requireParkedDeck(String plate) {
        Vehicle.validatePlate(plate);
        Deck deck = this.findDeckByVehicle(new Vehicle(plate));
        if (deck == null) {
            throw new IllegalArgumentException(
                    ErrorType.VEHICLE_NOT_IN_DECK.format(plate)
            );
        }
        return deck;
    }

    /**
     * Reopens an existing closed deck.
     *
     * @param deck the stored deck
     * @param token the original deck token for error reporting
     * @throws IllegalArgumentException if the deck is already open
     */
    public void reopen(
            Deck deck,
            String token
    ) {
        if (deck.isOpen()) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_ALREADY_OPEN.format(token)
            );
        }
        this.open(deck);
    }

    /**
     * Closes a stored deck after checking that it is open and empty.
     *
     * @param token the deck number token
     * @throws IllegalArgumentException if the deck cannot be closed
     */
    public void close(String token) {
        Deck deck = this.requireDeck(token);
        if (!deck.isOpen()) {
            throw new IllegalArgumentException(
                    ErrorType.DECK_ALREADY_CLOSED.format(token)
            );
        }

        if (deck.getNumParked() > 0) {
            throw new IllegalArgumentException(
                    "Cannot close Deck#" + deck.getNumber() + " - vehicles are still parked."
            );
        }
        this.close(deck);
    }
}
