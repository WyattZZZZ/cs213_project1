package parking;

/**
 * Represents a date and time associated with a parking activity.
 *
 * @author wyattzhang
 */
public class Timestamp implements Comparable<Timestamp> {

    /** The calendar date. */
    private Date date;

    /** The hour in 24-hour format. */
    private byte hour;

    /** The minute of the hour. */
    private byte minute;

    /**
     * Creates a timestamp.
     *
     * @param date the date
     * @param hour the hour in 24-hour format
     * @param minute the minute
     */
    public Timestamp(Date date, byte hour, byte minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Compares this timestamp with another timestamp.
     *
     * @param other the timestamp to compare
     * @return -1, 0, or 1 according to chronological order
     */
    @Override
    public int compareTo(Timestamp other) {
        return 0;
    }

    /**
     * Returns this timestamp as text.
     *
     * @return the timestamp in YYYY-MM-DD HH:MM format
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Runs test cases for the compareTo method.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        // TODO: Implement seven required compareTo() test cases.
    }
}