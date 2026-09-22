package parking;

import java.text.DecimalFormat;
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
     * Returns the year of this date.
     *
     * @return the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Returns the month of this date.
     *
     * @return the month
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Returns the day of this date.
     *
     * @return the day
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Determines whether the year of this date is a leap year.
     *
     * @return true if the year is a leap year; false otherwise
     */
    private boolean isLeap() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0){
                return this.year % QUATERCENTENNIAL == 0;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine the number of days in the month of this date.
     *
     * @return 31 or 30 days
     */
    private int numberOfDays(){
        if (this.month == 2) {
            return this.isLeap()
                    ? 29
                    : 28;
        }
        return (this.month == 4
                || this.month == 6
                || this.month == 9
                || this.month == 11)
                ? 30
                : 31;
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
        int thisDate = this.year * 10000 + this.month * 100 + this.day;
        int otherDate = other.year * 10000 + other.month * 100 + other.day;
        if (thisDate < otherDate) {
            return -1;
        }
        if (thisDate > otherDate) {
            return 1;
        }
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

        return this.year == other.year
                && this.month == other.month
                && this.day == other.day;
    }

    /**
     * Returns a textual representation of this date.
     *
     * @return the date formatted as YYYY-MM-DD
     */
    @Override
    public String toString() {
        DecimalFormat twoDigits = new DecimalFormat("00");
        return this.year + "-"
                + twoDigits.format(this.month) + "-"
                + twoDigits.format(this.day);
    }

    /**
     * Determines whether this date is a valid calendar date.
     * This method checks the month, day, and leap-year rules.
     *
     * @return true if this date is valid; false otherwise
     */
    public boolean isValid() {
        if (this.year <= 0 || this.year >= 10000) {
            return false;
        }
        if (this.month < JAN || this.month >= JAN + 12) {
            return false;
        }
        return this.day >= 0 && this.day <= this.numberOfDays();
    }

    /**
     * Runs the required test cases for the Date class.
     * The tests should include four invalid dates and two valid dates.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Date[] testDates = {
                new Date(0, 1, 1),       // Invalid year
                new Date(2026, 13, 1),   // Invalid month
                new Date(2026, 4, 31),   // April has only 30 days
                new Date(2025, 2, 29),   // 2025 is not a leap year
                new Date(2024, 2, 29),   // Valid leap-year date
                new Date(2026, 9, 15)    // Valid regular date
        };

        boolean[] expectedResults = {
                false,
                false,
                false,
                false,
                true,
                true
        };

        for (int i = 0; i < testDates.length; i++) {
            boolean actualResult = testDates[i].isValid();

            System.out.println(
                    "Test " + (i + 1)
                            + ": Date = " + testDates[i]
                            + ", Expected = " + expectedResults[i]
                            + ", Actual = " + actualResult
                            + ", Result = "
                            + (actualResult == expectedResults[i] ? "PASS" : "FAIL")
            );
        }
    }
}