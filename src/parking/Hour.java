package parking;

/**
 * Represents predefined deck operating hour codes and time intervals.
 *
 * @author Ethan Vu, Wyatt Zhang
 */
public enum Hour {
    /** Operating interval from 5:00 through 21:30. */
    HR5("5:00", "21:30", (byte) 5, (byte) 0, (byte) 21, (byte) 30),

    /** Operating interval from 6:30 through 18:30. */
    HR6("6:30", "18:30", (byte) 6, (byte) 30, (byte) 18, (byte) 30),

    /** Operating interval from 7:00 through 18:00. */
    HR7("7:00", "18:00", (byte) 7, (byte) 0, (byte) 18, (byte) 0);

    /** Number of minutes in one hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** Formatted opening time. */
    private final String startTime;

    /** Formatted closing time. */
    private final String endTime;

    /** Hour component of the opening time. */
    private final byte startHour;

    /** Minute component of the opening time. */
    private final byte startMinute;

    /** Hour component of the closing time. */
    private final byte endHour;

    /** Minute component of the closing time. */
    private final byte endMinute;

    /**
     * Parameterized constructor for Hour enum constants.
     *
     * @param startTime string representation of start time
     * @param endTime string representation of end time
     * @param startHour hour component of start time
     * @param startMinute minute component of start time
     * @param endHour hour component of end time
     * @param endMinute minute component of end time
     */
    Hour(String startTime, String endTime, byte startHour, byte startMinute, byte endHour, byte endMinute) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    /**
     * Gets the hour matching the specified code.
     *
     * @param code the 3-character operating hour code
     * @return matching Hour enum constant; null if code is invalid
     */
    public static Hour getHour(String code) {
        for (Hour hour : Hour.values()) {
            if (hour.name().equalsIgnoreCase(code)) {
                return hour;
            }
        }
        return null;
    }

    /**
     * Gets the formatted start time string.
     *
     * @return start time string
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the formatted end time string.
     *
     * @return end time string
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Gets the start hour byte value.
     *
     * @return start hour
     */
    public byte getStartHour() {
        return startHour;
    }

    /**
     * Gets the start minute byte value.
     *
     * @return start minute
     */
    public byte getStartMinute() {
        return startMinute;
    }

    /**
     * Gets the end hour byte value.
     *
     * @return end hour
     */
    public byte getEndHour() {
        return endHour;
    }

    /**
     * Gets the end minute byte value.
     *
     * @return end minute
     */
    public byte getEndMinute() {
        return endMinute;
    }

    /**
     * Determines whether a timestamp is within these operating hours.
     *
     * @param timestamp the timestamp to validate
     * @return true if the time is within the operating hours;
     *         false otherwise
     */
    public boolean contains(Timestamp timestamp) {
        int currentTime =
                timestamp.getHour() * MINUTES_PER_HOUR
                        + timestamp.getMinute();

        int openingTime =
                this.startHour * MINUTES_PER_HOUR
                        + this.startMinute;

        int closingTime =
                this.endHour * MINUTES_PER_HOUR
                        + this.endMinute;

        return currentTime >= openingTime
                && currentTime <= closingTime;
    }
    /** Returns the operating-hours interval used when printing a deck. */
    @Override
    public String toString() {
        return this.startTime + " ~ " + this.endTime;
    }

    /**
     * Validates entry or exit against the inclusive operating interval.
     *
     * @param timestamp the timestamp to check
     * @param entering true for entry, false for exit
     * @throws IllegalArgumentException if the time is outside this interval
     */
    public void validate(
            Timestamp timestamp,
            boolean entering
    ) {
        if (!this.contains(timestamp)) {
            ErrorType error = entering ? ErrorType.ENTRY_OUTSIDE_OPERATING_HOURS
                    : ErrorType.EXIT_OUTSIDE_OPERATING_HOURS;
            throw new IllegalArgumentException(
                    error.format(
                            this.startTime,
                            this.endTime
                    )
            );
        }
    }
}
