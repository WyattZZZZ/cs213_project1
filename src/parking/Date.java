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

    /** Number of years in a standard leap-year cycle. */
    public static final int QUADRENNIAL = 4;

    /** Number of years in a century. */
    public static final int CENTENNIAL = 100;

    /** Number of years in four centuries. */
    public static final int QUATERCENTENNIAL = 400;

    /** Integer value representing January. */
    public static final int JAN = Calendar.JANUARY + 1;

    /** Integer value representing February. */
    public static final int FEB = Calendar.FEBRUARY + 1;

    /** Integer value representing April. */
    public static final int APR = Calendar.APRIL + 1;

    /** Integer value representing June. */
    public static final int JUN = Calendar.JUNE + 1;

    /** Integer value representing September. */
    public static final int SEP = Calendar.SEPTEMBER + 1;

    /** Integer value representing November. */
    public static final int NOV = Calendar.NOVEMBER + 1;

    /** Integer value representing December. */
    public static final int DEC = Calendar.DECEMBER + 1;

    /** The smallest valid day of a month. */
    private static final int MIN_DAY = 1;

    /** The smallest valid year. */
    private static final int MIN_YEAR = 1;

    /** The first invalid five-digit year. */
    private static final int MAX_YEAR_EXCLUSIVE = 10000;

    /** Number of days in February during a non-leap year. */
    private static final int DAYS_IN_FEB = 28;

    /** Number of days in February during a leap year. */
    private static final int DAYS_IN_LEAP_FEB = 29;

    /** Number of days in a short month. */
    private static final int DAYS_IN_SHORT_MONTH = 30;

    /** Number of days in a long month. */
    private static final int DAYS_IN_LONG_MONTH = 31;

    /** The year of this date. */
    private int year;

    /** The month of this date. */
    private int month;

    /** The day of this date. */
    private int day;

    /**
     * Creates a date with the specified year, month, and day.
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
        if (this.year % QUATERCENTENNIAL == 0) {
            return true;
        }

        return this.year % QUADRENNIAL == 0
                && this.year % CENTENNIAL != 0;
    }

    /**
     * Returns the number of days in the month of this date.
     *
     * @return the maximum number of days in the month
     */
    private int numberOfDays() {
        if (this.month == FEB) {
            return this.isLeap()
                    ? DAYS_IN_LEAP_FEB
                    : DAYS_IN_FEB;
        }

        if (this.month == APR
                || this.month == JUN
                || this.month == SEP
                || this.month == NOV) {
            return DAYS_IN_SHORT_MONTH;
        }

        return DAYS_IN_LONG_MONTH;
    }

    /**
     * Determines whether this date is a valid calendar date.
     *
     * @return true if this date is valid; false otherwise
     */
    public boolean isValid() {
        if (this.year < MIN_YEAR
                || this.year >= MAX_YEAR_EXCLUSIVE) {
            return false;
        }

        if (this.month < JAN || this.month > DEC) {
            return false;
        }

        if (this.day < MIN_DAY) {
            return false;
        }

        return this.day <= this.numberOfDays();
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
        if (this.year < other.year) {
            return -1;
        }

        if (this.year > other.year) {
            return 1;
        }

        if (this.month < other.month) {
            return -1;
        }

        if (this.month > other.month) {
            return 1;
        }

        if (this.day < other.day) {
            return -1;
        }

        if (this.day > other.day) {
            return 1;
        }

        return 0;
    }

    /**
     * Determines whether this date is equal to another object.
     * Two dates are equal when their year, month, and day are equal.
     *
     * @param obj the object to compare with this date
     * @return true if the object represents the same date;
     *         false otherwise
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
     * Returns this date in YYYY-MM-DD format.
     *
     * @return the formatted date
     */
    @Override
    public String toString() {
        DecimalFormat fourDigits =
                new DecimalFormat("0000");

        DecimalFormat twoDigits =
                new DecimalFormat("00");

        return fourDigits.format(this.year)
                + "-"
                + twoDigits.format(this.month)
                + "-"
                + twoDigits.format(this.day);
    }

    /**
     * Runs six test cases for the isValid method.
     * The testbed includes four invalid dates and two valid dates.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Date[] testDates = {
                new Date(0, JAN, MIN_DAY),
                new Date(2026, DEC + 1, MIN_DAY),
                new Date(2026, APR, DAYS_IN_LONG_MONTH),
                new Date(2025, FEB, DAYS_IN_LEAP_FEB),
                new Date(2024, FEB, DAYS_IN_LEAP_FEB),
                new Date(2026, SEP, 15)
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
            boolean actualResult =
                    testDates[i].isValid();

            String result = actualResult
                    == expectedResults[i]
                    ? "PASS"
                    : "FAIL";

            System.out.println(
                    "Test " + (i + 1)
                            + ": Date = " + testDates[i]
                            + ", Expected = "
                            + expectedResults[i]
                            + ", Actual = "
                            + actualResult
                            + ", Result = "
                            + result);
        }
    }

    /**
     * Parses a valid calendar date without performing terminal output.
     *
     * @param token the date token
     * @return the parsed date
     * @throws IllegalArgumentException if the token is not a valid date
     */
    public static Date parse(String token) {
        if (token != null && token.matches("[0-9]+-[0-9]+-[0-9]+")) {
            String[] parts = token.split("-", -1);
            try {
                Date date = new Date(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                );
                if (date.isValid()) {
                    return date;
                }
            } catch (NumberFormatException exception) {
                // Out-of-range components are invalid calendar dates.
            }
        }
        throw new IllegalArgumentException(
                ErrorType.INVALID_DATE.format(token)
        );
    }

    /**
     * Returns the next calendar day without modifying this date.
     *
     * @return the next date, including month and year rollover
     */
    public Date nextDay() {
        if (this.day < this.numberOfDays()) {
            return new Date(this.year, this.month, this.day + 1);
        }

        if (this.month < DEC) {
            return new Date(this.year, this.month + 1, MIN_DAY);
        }
        return new Date(this.year + 1, JAN, MIN_DAY);
    }
}
