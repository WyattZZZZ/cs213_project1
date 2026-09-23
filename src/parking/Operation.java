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
    private VehicleList vehicle_list;

    /** The list of parking decks maintained by the system. */
    private DeckList deck_list;

    /**
     * Creates an operation controller with empty vehicle and deck lists.
     */
    public Operation() {
        this.vehicle_list = new VehicleList();
        this.deck_list = new DeckList();
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
        if (tokens.countTokens() != 1) {
            System.out.println(
                    ErrorType.INVALID_COMMAND.format("A")
            );
        }
        String plate = tokens.nextToken();
        if (!Vehicle.isValidPlate(plate)) {
            System.out.println(
                    ErrorType.INVALID_PLATE.format(plate)
            );
        }
        if (!VehicleList.search(plate)) {
            System.out.println(
                    ErrorType.VEHICLE_ALREADY_REGISTERED.format(plate)
            );
        } else {
            Vehicle new_vehicle = new Vehicle(plate);
            this.vehicle_list.add(new_vehicle);
        }
        return;
    }

    /**
     * Processes an R command to unregister a vehicle.
     *
     * @param tokens tokens remaining after the command
     */
    private void processRemove(StringTokenizer tokens) {
        // TODO: Read and validate plate.
        if (tokens.countTokens() != 1) {
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
        if (this.deck_list.findDeckByVehicle(template) != null) {
            System.out.println(
                    ErrorType.VEHICLE_CURRENTLY_PARKED.format(plate)
            );
        }
        if (this.vehicle_list.getVehicle(template).hasHistory()) {
            System.out.println(
                    ErrorType.VEHICLE_HAS_HISTORY.format(plate)
            );
        }
        this.vehicle_list.remove(template);
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
        // TODO: Validate deck number and check whether it already exists.
        // TODO: Reopen a closed deck without requiring other tokens.
        // TODO: For a new deck, validate location, hour, and capacity.
    }

    /**
     * Processes a C command to close a parking deck.
     *
     * @param tokens tokens remaining after the command
     */
    private void processClose(StringTokenizer tokens) {
        // TODO: Validate deck number, existence, and open status.
        // TODO: Reject closing when a vehicle remains in the deck.
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
