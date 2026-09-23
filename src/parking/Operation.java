package parking;

import org.w3c.dom.html.HTMLObjectElement;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Processes commands entered through the terminal.
 *
 * @author wyattzhang, ethanvu
 */
public class Operation {

    /** The list of vehicles registered with the system. */
    private VehicleList vehicleList;

    /** The list of parking decks maintained by the system. */
    private DeckList deckList;

    /** Token number of openning a new deck */
    private static int OPEN_ARGUMENT_COUNT = 3;


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
                "Parking Management System is in operation.");

        while (running && scanner.hasNextLine()) {
            running = this.processCommand(scanner.nextLine());
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
            case "A": this.processAdd(tokens); break;
            case "R": this.processRemove(tokens); break;
            case "O": this.processOpen(tokens); break;
            case "C": this.processClose(tokens); break;
            case "E": this.processEnter(tokens); break;
            case "X": this.processExit(tokens); break;
            case "PP": this.processPrintVehicles(tokens); break;
            case "PD": this.processPrintDecks(tokens); break;
            case "PH": this.processPrintHistory(tokens); break;
            case "Q":
                System.out.println(
                        "Parking Management System is terminated.");
                return false;
            default:
                System.out.println(
                        ErrorType.INVALID_COMMAND.format(command));
                break;
        }
        return true;
    }

    /**
     * Processes an A command to register a vehicle.
     *
     * @param tokens tokens remaining after the command
     */
    private void processAdd(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            System.out.println(
                    ErrorType.INVALID_COMMAND.format("A")
            );
            return;
        }
        String plate = tokens.nextToken();
        if (!Vehicle.isValidPlate(plate)) {
            System.out.println(
                    ErrorType.INVALID_PLATE.format(plate)
            );
            return;
        }
        if (!VehicleList.search(plate)) {
            System.out.println(
                    ErrorType.VEHICLE_ALREADY_REGISTERED.format(plate)
            );
            return;
        }
        Vehicle new_vehicle = new Vehicle(plate);
        this.vehicleList.add(new_vehicle);
        System.out.println(
                plate + " registered."
        );
    }

    /**
     * Processes an R command to unregister a vehicle.
     *
     * @param tokens tokens remaining after the command
     */
    private void processRemove(StringTokenizer tokens) {
        // TODO: Read and validate plate.
        if (!tokens.hasMoreTokens()) {
            System.out.println(
                    ErrorType.INVALID_COMMAND.format("R")
            );
        }
        // TODO: Confirm registration and that the vehicle is not parked.
        String plate = tokens.nextToken();
        if (!Vehicle.isValidPlate(plate)) {
            System.out.println(
                    ErrorType.INVALID_PLATE.format(plate)
            );
        }
        if (!VehicleList.search(plate)) {
            System.out.println(
                    ErrorType.VEHICLE_NOT_FOUND_FOR_REMOVAL.format(plate)
            );
        }
        Vehicle template = new Vehicle(plate);
        if (this.deckList.findDeckByVehicle(template) != null) {
            System.out.println(
                    ErrorType.VEHICLE_CURRENTLY_PARKED.format(plate)
            );
        }
        if (this.vehicleList.getVehicle(template).hasHistory()) {
            System.out.println(
                    ErrorType.VEHICLE_HAS_HISTORY.format(plate)
            );
        }
        this.vehicleList.remove(template);
        System.out.println(
                plate + " unregistered."
        );
    }

    /**
     * Processes an O command to open or reopen a parking deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processOpen(StringTokenizer tokens) {
        String id;
        if (tokens.hasMoreTokens()) {
            id = tokens.nextToken();
        } else {
            id = "";
        }

        if (!Deck.isValidDeckNumber(id)) {
            System.out.println(
                    ErrorType.INVALID_DECK_NUMBER_OPEN.format(id)
            );
            return;
        }

        int idNumber = Integer.parseInt(id);
        Deck existingDeck = this.deckList.get(idNumber);

        if (existingDeck != null) {
            processExistingDeck(existingDeck, id);
            return;
        }

        if (tokens.countTokens() != OPEN_ARGUMENT_COUNT) {
            System.out.println(
                    ErrorType.OPEN_MISSING_TOKENS.format(id)
            );
            return;
        }

        openNewDeck(id, idNumber, tokens);
    }

    private void processExistingDeck(Deck deck, String id) {
        if (deck.isOpen()) {
            System.out.println(
                    ErrorType.DECK_ALREADY_OPEN.format(id)
            );
            return;
        }

        deck.setOpen(true);
        System.out.printf(
                "Deck#%s - was closed, now reopened.%n",
                id
        );
    }

    private void openNewDeck(
            String id,
            int idNumber,
            StringTokenizer tokens
    ) {
        String city = tokens.nextToken();
        String hourCode = tokens.nextToken();
        String capacityToken = tokens.nextToken();

        if (!Location.isValid(city)) {
            System.out.println(
                    ErrorType.INVALID_LOCATION.format(city)
            );
            return;
        }

        if (!Hour.isValid(hourCode)) {
            System.out.println(
                    ErrorType.INVALID_HOUR.format(hourCode)
            );
            return;
        }

        Integer capacity = parseCapacity(capacityToken, id);
        if (capacity == null) {
            return;
        }

        Deck deck = new Deck(idNumber,
                            Location.findByCity(city),
                            Hour.getHour(hourCode),
                            capacity
                        );
        this.deckList.open(deck);

        System.out.printf(
                "Deck#%s opened.%n",
                id
        );
    }

    private Integer parseCapacity(
            String capacityToken,
            String id
    ) {
        if (!capacityToken.matches("[0-9]+")) {
            System.out.println(
                    ErrorType.INVALID_CAPACITY.format(capacityToken)
            );
            return null;
        }

        try {
            int capacity = Integer.parseInt(capacityToken);
            if (capacity > Deck.MAXCAPACITY) {
                System.out.println(
                        ErrorType.CAPACITY_EXCEEDS_MAXIMUM.format(
                                capacityToken,
                                id
                        )
                );
                return null;
            }
            return capacity;
        } catch (NumberFormatException exception) {
            System.out.println(
                    ErrorType.INVALID_CAPACITY.format(capacityToken)
            );
            return null;
        }
    }

    /**
     * Processes a C command to close a parking deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processClose(StringTokenizer tokens) {
        String id = "";
        if (tokens.hasMoreTokens()) {
            id = tokens.nextToken();
        }

        if (!Deck.isValidDeckNumber(id)) {
            System.out.println(
                    ErrorType.INVALID_DECK_NUMBER_CHARACTERS.format(id)
            );
            return;
        }

        Deck existingDeck = this.deckList.get(Integer.parseInt(id));
        if (existingDeck != null) {
            if (!existingDeck.isOpen()) {
                System.out.println(
                        ErrorType.DECK_ALREADY_CLOSED.format(id)
                );
                return;
            }
        } else {
            System.out.println(
                    ErrorType.DECK_NOT_FOUND.format(id)
            );
            return;
        }
        // TODO: check ErrorType of CANNOT CLOSE SINCE CAR IS STILL PARKING
        if (existingDeck.getNumParked() > 0) {
            System.out.println(
                    "Cannot close"
            );
            return;
        }

        existingDeck.setOpen(false);
        System.out.printf(
                "Deck#%s - closed.%n",
                id
        );
    }

    /**
     * Processes an E command to record a vehicle entering a deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processEnter(StringTokenizer tokens) {
        // TODO: Read deck number, plate, date, and time.
        // TODO: Validate deck state and vehicle registration/state.
        // TODO: Validate Date, Timestamp, and operating hours.
        // TODO: Create Parking and call Deck.enter().
    }

    /**
     * Processes an X command to record a vehicle exiting a deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processExit(StringTokenizer tokens) {
        // TODO: Read plate, date, and time and locate current parking.
        // TODO: Validate date, time, operating hours, and duration.
        // TODO: Set exit, remove from Deck, and add Vehicle history.
    }

    /**
     * Processes a PP command to print registered vehicles.
     *
     * @param tokens tokens remaining after the command
     */
    private void processPrintVehicles(StringTokenizer tokens) {
        // TODO: Print the empty-list message or the required header.
        // TODO: Call vehicleList.printByPlate() and print the footer.
    }

    /**
     * Processes a PD command to print decks or vehicles in one deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processPrintDecks(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            this.deckList.printByLocation();
            return;
        }

        // TODO: Validate the optional deck number, existence, and status.
        // TODO: Call deckList.printVehicles(storedDeck).
    }

    /**
     * Processes a PH command to print parking history.
     *
     * @param tokens tokens remaining after the command
     */
    private void processPrintHistory(StringTokenizer tokens) {
        if (!tokens.hasMoreTokens()) {
            // TODO: Print the all-history header and footer.
            this.vehicleList.printHistory();
            return;
        }

        // TODO: Validate optional plate, existence, and history.
        // TODO: Print the selected vehicle's history.
    }
}
