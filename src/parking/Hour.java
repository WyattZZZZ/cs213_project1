package parking;

/**
 * Represents predefined deck operating hour codes and time intervals.
 *
 * @author Ethan Vu
 */
public enum Hour {
    HR5("5:00", "21:30", (byte) 5, (byte) 0, (byte) 21, (byte) 30),
    HR6("6:30", "18:30", (byte) 6, (byte) 30, (byte) 18, (byte) 30),
    HR7("7:00", "18:00", (byte) 7, (byte) 0, (byte) 18, (byte) 0);

    private final String startTime;
    private final String endTime;
    private final byte startHour;
    private final byte startMinute;
    private final byte endHour;
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
}