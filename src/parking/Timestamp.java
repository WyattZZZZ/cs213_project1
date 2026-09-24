package parking;

import java.text.DecimalFormat;

/**
 * Represents a date and time associated with a parking activity.
 *
 * @author wyattzhang
 */
public class Timestamp implements Comparable<Timestamp> {

    /** Number of hours in one day. */
    private static final int HOURS_PER_DAY = 24;

    /** Number of minutes in one hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The calendar date */
    private Date date;

    /** The hour in 24-hour format */
    private byte hour;

    /** The minute of the hour. */
    private byte minute;

    /**
     * Creates a timestamp.
     *
     * @param date   the date
     * @param hour   the hour in 24-hour format
     * @param minute the minute
     */
    public Timestamp(Date date, byte hour, byte minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Returns the year of this date.
     *
     * @return the year
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the month of this date.
     *
     * @return the month
     */
    public byte getHour() {
        return this.hour;
    }

    /**
     * Returns the day of this date.
     *
     * @return the day
     */
    public byte getMinute() {
        return this.minute;
    }

    /**
     * Compares this timestamp with another timestamp.
     *
     * @param other the timestamp to compare
     * @return -1, 0, or 1 according to chronological order
     */
    @Override
    public int compareTo(Timestamp other) {
        if (this.date.compareTo(other.date) == 0) {
            if (this.hour == other.hour) {
                if (this.minute == other.minute) {
                    return 0;
                } else {
                    return this.minute > other.minute ? 1 : -1;
                }
            } else {
                return this.hour > other.hour ? 1 : -1;
            }
        } else {
            return this.date.compareTo(other.date);
        }
    }

    /**
     * Determines whether the hour and minute are valid.
     *
     * @return true if the time is valid; false otherwise
     */
    public boolean isValidTime() {
        return this.hour >= 0
                && this.hour < HOURS_PER_DAY
                && this.minute >= 0
                && this.minute < MINUTES_PER_HOUR;
    }

    /**
     * Returns this timestamp as text.
     *
     * @return the timestamp in YYYY-MM-DD HH:MM format
     */
    @Override
    public String toString() {
        DecimalFormat twoDigits = new DecimalFormat("00");
        return this.date.toString()
                + " "
                + twoDigits.format(this.hour)
                + ":"
                + twoDigits.format(this.minute);
    }

    /**
     * Runs test cases for the compareTo method.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Timestamp[] firstTimestamps = {
                new Timestamp(new Date(2025, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2027, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 14), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 11, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 29),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 31)
        };

        Timestamp[] secondTimestamps = {
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30),
                new Timestamp(new Date(2026, 9, 15), (byte) 10, (byte) 30)
        };

        int[] expectedResults = {
                -1, // Earlier year
                1, // Later year
                -1, // Earlier day
                1, // Later hour
                -1, // Earlier minute
                0, // Equal timestamp
                1  // Later minute
        };

        for (int i = 0; i < firstTimestamps.length; i++) {
            int comparison = firstTimestamps[i].compareTo(secondTimestamps[i]);
            int actualResult = Integer.signum(comparison);

            System.out.println("Test " + (i + 1));
            System.out.println("First:    " + firstTimestamps[i]);
            System.out.println("Second:   " + secondTimestamps[i]);
            System.out.println("Expected: " + expectedResults[i]);
            System.out.println("Actual:   " + actualResult);
            System.out.println(
                    "Result:   "
                            + (actualResult == expectedResults[i] ? "PASS" : "FAIL")
            );
            System.out.println();
        }
    }

    /**
     * Parses date, hour, and minute in validation order.
     *
     * @param dateToken the calendar date
     * @param timeToken the hour and minute
     * @return the parsed timestamp
     * @throws IllegalArgumentException if a component is invalid
     */
    public static Timestamp parse(
            String dateToken,
            String timeToken
    ) {
        Date date = Date.parse(dateToken);
        String[] parts = timeToken.split(":", 2);
        byte hour = parseTimePart(
                parts[0],
                HOURS_PER_DAY,
                ErrorType.INVALID_HOUR
        );
        byte minute = parseTimePart(
                parts.length == 2 ? parts[1] : "",
                MINUTES_PER_HOUR,
                ErrorType.INVALID_MINUTE
        );
        return new Timestamp(date, hour, minute);
    }

    /**
     * Validates a numeric time component before converting it to a byte.
     *
     * @param token the component token
     * @param limit the exclusive upper bound
     * @param error the error associated with this component
     * @return the valid component
     * @throws IllegalArgumentException if the component is invalid
     */
    private static byte parseTimePart(
            String token,
            int limit,
            ErrorType error
    ) {
        try {
            int value = Integer.parseInt(token);
            if (value >= 0 && value < limit) {
                return (byte) value;
            }
        } catch (NumberFormatException exception) {
            // Malformed components use the same hour/minute error.
        }
        throw new IllegalArgumentException(
                error.format(token)
        );
    }

    /**
     * Returns a timestamp a specified number of calendar days later.
     *
     * @param days the nonnegative number of days to add
     * @return the later timestamp at the same time of day
     * @throws IllegalArgumentException if days is negative
     */
    public Timestamp plusDays(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Days must not be negative.");
        }
        Date later = this.date;
        for (int day = 0; day < days; day++) {
            later = later.nextDay();
        }
        return new Timestamp(later, this.hour, this.minute);
    }
}
