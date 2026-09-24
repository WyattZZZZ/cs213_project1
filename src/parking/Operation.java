package parking;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Processes commands entered through the terminal.
 *
 * @author wyattzhang, ethanvu
 */
public class Operation {

    /** The list of vehicles registered with the system. */
    private final VehicleList vehicleList;

    /** The list of parking decks maintained by the system. */
    private final DeckList deckList;

    /** Number of data tokens required after a new deck number. */
    private static final int OPEN_ARGUMENT_COUNT = 3;


    /**
     * Creates an operation controller with empty vehicle and deck lists.
     */
    public Operation() {
        this.vehicleList = new VehicleList();
        this.deckList = new DeckList();
    }

    /**
     * Continuously reads and processes commands until the user enters Q.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println(
                "Parking Management System is in operation."
        );
        System.out.println();

        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                running = this.processCommand(line);
            } catch (IllegalArgumentException exception) {
                System.out.println(
                        exception.getMessage()
                );
            }
        }

        scanner.close();
    }

    /**
     * Processes one command line.
     *
     * @param line the command line entered by the user
     * @return false when Q is entered; true otherwise
     */
    private boolean processCommand(String line) {
        if (line == null || line.trim().isEmpty()) {
            return true;
        }

        StringTokenizer tokens = new StringTokenizer(line);
        String command = tokens.nextToken();

        switch (command) {
            case "A":
                this.processAdd(tokens);
                break;
            case "R":
                this.processRemove(tokens);
                break;
            case "O":
                this.processOpen(tokens);
                break;
            case "C":
                this.processClose(tokens);
                break;
            case "E":
                this.processEnter(tokens);
                break;
            case "X":
                this.processExit(tokens);
                break;
            case "PP":
                this.vehicleList.printVehiclesReport();
                break;
            case "PD":
                this.processPrintDecks(tokens);
                break;
            case "PH":
                this.processPrintHistory(tokens);
                break;
            case "Q":
                System.out.println(
                        "Parking Management System is terminated."
                );
                return false;
            default:
                System.out.println(
                        ErrorType.INVALID_COMMAND.format(command)
                );
                break;
        }
        return true;
    }

    /**
     * Registers a vehicle and displays the result.
     *
     * @param tokens tokens remaining after the command
     */
    private void processAdd(StringTokenizer tokens) {
        this.requireArguments(tokens, 1, "A");
        Vehicle vehicle = this.vehicleList.register(tokens.nextToken());
        System.out.println(
                vehicle.getPlate() + " registered."
        );
    }

    /**
     * Unregisters a vehicle and displays the result.
     *
     * @param tokens tokens remaining after the command
     */
    private void processRemove(StringTokenizer tokens) {
        this.requireArguments(tokens, 1, "R");
        Vehicle vehicle = this.vehicleList.unregister(
                tokens.nextToken(),
                this.deckList
        );
        System.out.println(
                vehicle.getPlate() + " unregistered."
        );
    }

    /**
     * Opens or reopens a deck using the supplied command data.
     *
     * @param tokens tokens remaining after the command
     */
    private void processOpen(StringTokenizer tokens) {
        String id = tokens.hasMoreTokens() ? tokens.nextToken() : "";
        int number = Deck.parseNumber(
                id,
                ErrorType.INVALID_DECK_NUMBER_OPEN
        );
        Deck existingDeck = this.deckList.get(number);
        if (existingDeck != null) {
            this.deckList.reopen(existingDeck, id);
            System.out.printf(
                    "Deck#%s - was closed, now reopened.%n",
                    id
            );
            return;
        }

        if (tokens.countTokens() != OPEN_ARGUMENT_COUNT) {
            throw new IllegalArgumentException(
                    ErrorType.OPEN_MISSING_TOKENS.format(id)
            );
        }
        Deck deck = Deck.create(
                number,
                tokens.nextToken(),
                tokens.nextToken(),
                tokens.nextToken()
        );
        this.deckList.open(deck);
        System.out.printf(
                "Deck#%s opened.%n",
                id
        );
    }

    /**
     * Closes a deck and displays the result.
     *
     * @param tokens tokens remaining after the command
     */
    private void processClose(StringTokenizer tokens) {
        String id = tokens.hasMoreTokens() ? tokens.nextToken() : "";
        this.deckList.close(id);
        System.out.printf(
                "Deck#%s - closed.%n",
                id
        );
    }

    /**
     * Resolves command data in validation order and records entry.
     *
     * @param tokens tokens remaining after the command
     */
    private void processEnter(StringTokenizer tokens) {
        this.requireArguments(tokens, 4, "E");
        Deck deck = this.deckList.requireOpenDeck(tokens.nextToken());
        deck.validateEntry();
        Vehicle vehicle = this.vehicleList.requireEnteringVehicle(
                tokens.nextToken(),
                this.deckList
        );
        Timestamp enter = Timestamp.parse(
                tokens.nextToken(),
                tokens.nextToken()
        );
        deck.recordEntry(vehicle, enter);
        System.out.printf(
                "%s entered Deck#%d on %s%n",
                vehicle.getPlate(),
                deck.getNumber(),
                enter
        );
    }

    /**
     * Locates the vehicle's deck and records exit.
     *
     * @param tokens tokens remaining after the command
     */
    private void processExit(StringTokenizer tokens) {
        this.requireArguments(tokens, 3, "X");
        String plate = tokens.nextToken();
        Deck deck = this.deckList.requireParkedDeck(plate);
        Timestamp exit = Timestamp.parse(
                tokens.nextToken(),
                tokens.nextToken()
        );
        Parking parking = deck.recordExit(new Vehicle(plate), exit);
        System.out.printf(
                "%s exited Deck#%d on %s%n",
                parking.getVehicle().getPlate(),
                deck.getNumber(),
                exit
        );
    }

    /**
     * Selects the deck list or one deck's vehicle report.
     *
     * @param tokens tokens remaining after the command
     */
    private void processPrintDecks(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            this.deckList.printByLocation();
            return;
        }

        Deck deck = this.deckList.requireOpenDeck(tokens.nextToken());
        this.deckList.printVehicles(deck);
    }

    /**
     * Selects all vehicles' history or one vehicle's history report.
     *
     * @param tokens tokens remaining after the command
     */
    private void processPrintHistory(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            this.vehicleList.printHistoryReport();
            return;
        }

        Vehicle vehicle = this.vehicleList.requireVehicle(
                tokens.nextToken(),
                ErrorType.VEHICLE_NOT_FOUND
        );
        vehicle.printHistoryReport();
    }

    /**
     * Checks command syntax before consuming required data tokens.
     *
     * @param tokens tokens remaining after the command
     * @param count the minimum number of required tokens
     * @param command the command used in the error message
     * @throws IllegalArgumentException if data tokens are missing
     */
    private void requireArguments(
            StringTokenizer tokens,
            int count,
            String command
    ) {
        if (tokens.countTokens() < count) {
            throw new IllegalArgumentException(
                    ErrorType.INVALID_COMMAND.format(command)
            );
        }
    }
}
