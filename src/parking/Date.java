package parking;

import java.util.Calendar;

/**
 * Represents a calendar date used in a parking timestamp.
 * Provides methods for validating, comparing, and formatting dates.
 *
 * @author wyattzhang
 */
public class Date implements Comparable<Date> {

    /** Number of years in the standard leap-year cycle. */
    public static final int QUADRENNIAL = 4;

    /** Number of years in a century. */
    public static final int CENTENNIAL = 100;

    /** Number of years in four centuries. */
    public static final int QUATERCENTENNIAL = 400;

    /** Integer value representing January. */
    public static final int JAN = Calendar.JANUARY + 1;

    /** The year of this date. */
    private int year;

    /** The month of this date. */
    private int month;

    /** The day of this date. */
    private int day;

    /**
     * Creates a Date object with the specified year, month, and day.
     *
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Determines whether the year of this date is a leap year.
     *
     * @return true if the year is a leap year; false otherwise
     */
    private boolean isLeap() {
        return false;
    }

    /**
     * Compares this date with another date in chronological order.
     *
     * @param other the date to compare with this date
     * @return -1 if this date is earlier, 1 if this date is later,
     *         or 0 if the dates are equal
     */
    @Override
    public int compareTo(Date other) {
        return 0;
    }

    /**
     * Determines whether this date is equal to another object.
     * Two dates are equal when their years, months, and days are equal.
     *
     * @param obj the object to compare with this date
     * @return true if the object represents the same date; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Date)) {
            return false;
        }

        Date other = (Date) obj;

        return year == other.year
                && month == other.month
                && day == other.day;
    }

    /**
     * Returns a textual representation of this date.
     *
     * @return the date formatted as YYYY-MM-DD
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Determines whether this date is a valid calendar date.
     * This method checks the month, day, and leap-year rules.
     *
     * @return true if this date is valid; false otherwise
     */
    public boolean isValid() {
        return false;
    }

    /**
     * Runs the required test cases for the Date class.
     * The tests should include four invalid dates and two valid dates.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        // TODO: Add four invalid and two valid test cases.
    }
}