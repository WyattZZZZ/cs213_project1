package parking;

import java.text.DecimalFormat;

/**
 * Represents a date and time associated with a parking activity.
 *
 * @author wyattzhang
 */
public class Timestamp implements Comparable<Timestamp> {

    /**
     * The calendar date.
     */
    private Date date;

    /**
     * The hour in 24-hour format.
     */
    private byte hour;

    /**
     * The minute of the hour.
     */
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
}