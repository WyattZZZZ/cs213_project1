package parking;

/**
 * Defines error messages used by the Parking Management System.
 *
 * @author wyattzhang
 */
public enum ErrorType {

    /** The entered command is not supported. */
    INVALID_COMMAND("%s is an invalid command!"),

    /** The license plate does not follow the required format. */
    INVALID_PLATE("%s - invalid license plate format."),

    /** The license plate is already registered. */
    VEHICLE_ALREADY_REGISTERED("%s is already registered."),

    /** The vehicle does not exist and cannot be unregistered. */
    VEHICLE_NOT_FOUND_FOR_REMOVAL(
            "%s does not exist; cannot be unregistered."),

    /** The vehicle cannot be removed because it is currently in a deck. */
    VEHICLE_CURRENTLY_PARKED(
            "Cannot be unregistered - %s is in a deck."),

    /** The vehicle cannot be removed because it has parking history. */
    VEHICLE_HAS_HISTORY(
            "Cannot be unregistered - %s has parking history."),

    /** The deck number used by the open command is invalid. */
    INVALID_DECK_NUMBER_OPEN(
            "%s - invalid deck number."),

    /** The deck number contains non-digit characters. */
    INVALID_DECK_NUMBER_CHARACTERS(
            "%s - invalid deck number; it contains characters."),

    /** The specified parking-deck location is invalid. */
    INVALID_LOCATION(
            "%s - invalid location."),

    /** The specified operating-hours code is invalid. */
    INVALID_OPERATION_HOURS(
            "%s - invalid operation hours"),

    /** The capacity is not an integer. */
    INVALID_CAPACITY(
            "%s - invalid capacity; it's not an integer."),

    /** The capacity exceeds the maximum of six vehicles. */
    CAPACITY_EXCEEDS_MAXIMUM(
            "%s - exceeds the maximum deck capacity 6"),

    /** The deck is already open. */
    DECK_ALREADY_OPEN(
            "Deck#%s - is already open."),

    /** Required tokens for opening a new deck are missing. */
    OPEN_MISSING_TOKENS(
            "Error opening Deck#%s - missing data tokens."),

    /** The requested deck does not exist. */
    DECK_NOT_FOUND(
            "Deck#%s - does not exist."),

    /** The requested deck is already closed. */
    DECK_ALREADY_CLOSED(
            "Deck#%s - is already closed."),

    /** The deck is closed and cannot accept a vehicle. */
    DECK_CLOSED_FOR_PARKING(
            "Deck#%s - is closed for parking."),

    /** The deck has reached its maximum capacity. */
    DECK_FULL(
            "Deck#%s - is full."),

    /** The vehicle is not registered. */
    VEHICLE_NOT_REGISTERED(
            "%s - is not registered."),

    /** The vehicle is already parked in a deck. */
    VEHICLE_ALREADY_IN_DECK(
            "Error entering - %s is already in a deck."),

    /** The calendar date is invalid. */
    INVALID_DATE(
            "%s - invalid calendar date."),

    /** The hour is outside the valid 24-hour range. */
    INVALID_HOUR(
            "%s - invalid hour."),

    /** The minute is outside the valid range. */
    INVALID_MINUTE(
            "%s - invalid minute."),

    /** The entry time is outside the deck's operating hours. */
    ENTRY_OUTSIDE_OPERATING_HOURS(
            "Error entering - not within the operating hours: %s ~ %s"),

    /** The vehicle is not currently parked in a deck. */
    VEHICLE_NOT_IN_DECK(
            "Error exiting - %s is not in a deck."),

    /** The exit time is outside the deck's operating hours. */
    EXIT_OUTSIDE_OPERATING_HOURS(
            "Error exiting - not within the operating hours: %s ~ %s"),

    /** The exit timestamp is earlier than the entry timestamp. */
    EXIT_BEFORE_ENTRY(
            "Exiting time %s before entering time %s"),

    /** The parking period exceeds two days. */
    EXIT_EXCEEDS_TWO_DAYS(
            "Invalid exiting time - exceeds two days."),

    /** The vehicle has no completed parking history. */
    NO_PARKING_HISTORY(
            "%s - no parking history."),

    /** No vehicle is currently registered. */
    VEHICLE_LIST_EMPTY(
            "Vehicle list is empty - no vehicle is registered."),

    /** No parking deck is currently open. */
    DECK_LIST_EMPTY(
            "Deck list is empty - no deck is open.");

    /** The output template associated with this error. */
    private final String template;

    /**
     * Creates an error type with its output template.
     *
     * @param template the error-message template
     */
    ErrorType(String template) {
        this.template = template;
    }

    /**
     * Formats this error message using the supplied values.
     *
     * @param values values inserted into the template
     * @return the formatted error message
     */
    public String format(Object... values) {
        return String.format(this.template, values);
    }

    /**
     * Returns the unformatted error-message template.
     *
     * @return the error-message template
     */
    @Override
    public String toString() {
        return this.template;
    }
}