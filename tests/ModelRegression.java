package parking;

/**
 * Verifies model validation and state changes independently of Operation.
 *
 * @author wyattzhang
 */
public class ModelRegression {

    /** Checks an invariant without requiring the JVM assertion flag. */
    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError("Model invariant failed.");
        }
    }

    /** Validates entry, failed exits, completion, and repeat-exit protection. */
    public static void main(String[] args) {
        VehicleList vehicles = new VehicleList();
        DeckList decks = new DeckList();
        Vehicle vehicle = vehicles.register("r48-jik");
        Deck deck = Deck.create(110, "princeton", "hr6", "2");
        decks.open(deck);
        Timestamp enter = Timestamp.parse("2024-02-28", "06:30");
        deck.recordEntry(vehicles.requireEnteringVehicle("R48-JIK", decks), enter);
        Parking parking = deck.getParking(vehicle);
        check(parking != null && parking.getExit() == null);

        String[][] invalidExits = {
                {"2024-02-28", "06:29",
                        "Error exiting - not within the operating hours: 6:30 ~ 18:30"},
                {"2024-02-27", "06:30",
                        "Exiting time 2024-02-27 06:30 before entering time 2024-02-28 06:30"},
                {"2024-03-01", "06:31", "Invalid exiting time - exceeds two days."}
        };
        for (String[] invalid : invalidExits) {
            try {
                deck.recordExit(vehicle, Timestamp.parse(invalid[0], invalid[1]));
                throw new AssertionError("Invalid exit accepted.");
            } catch (IllegalArgumentException exception) {
                check(exception.getMessage().equals(invalid[2]));
            }
            check(deck.getNumParked() == 1);
            check(deck.getParking(vehicle) == parking);
            check(parking.getExit() == null && !vehicle.hasHistory());
        }

        Timestamp exit = Timestamp.parse("2024-03-01", "06:30");
        check(deck.recordExit(vehicle, exit) == parking);
        check(deck.getNumParked() == 0 && deck.getParking(vehicle) == null);
        check(vehicle.getHistory().getParking() == parking);
        check(vehicle.getHistory().getNext() == null);
        check(parking.getExit() == exit);
        try {
            deck.recordExit(vehicle, exit);
            throw new AssertionError("Repeated exit accepted.");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().equals("Error exiting - R48-JIK is not in a deck."));
        }
        check(vehicle.getHistory().getNext() == null);

        try {
            vehicles.unregister("r48-jik", decks);
            throw new AssertionError("Vehicle with history removed.");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().equals(
                    "Cannot be unregistered - r48-jik has parking history."));
        }
        check(vehicles.getVehicle(new Vehicle("R48-JIK")) == vehicle);
        check(enter.toString().equals("2024-02-28 06:30"));
        check(enter.plusDays(2).toString().equals("2024-03-01 06:30"));
        check(Timestamp.parse("2026-12-31", "18:30").plusDays(2)
                .toString().equals("2027-01-02 18:30"));

        try {
            Timestamp.parse("2026-02-29", "256:60");
            throw new AssertionError("Invalid date accepted.");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().equals("2026-02-29 - invalid calendar date."));
        }
        try {
            Timestamp.parse("2026-03-01", "256:60");
            throw new AssertionError("Overflowing hour accepted.");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().equals("256 - invalid hour."));
        }
        try {
            Deck.create(200, "invalid", "invalid", "7");
            throw new AssertionError("Invalid location accepted.");
        } catch (IllegalArgumentException exception) {
            check(exception.getMessage().equals("invalid - invalid location."));
        }
    }
}
