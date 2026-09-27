package parking;

/**
 * Represents a supported parking-deck location.
 *
 * @author wyattzhang
 */
public enum Location {
    /** Bridgewater in Somerset County. */
    BRIDGEWATER("Bridgewater", "Somerset", "08807"),

    /** Piscataway in Middlesex County. */
    PISCATAWAY("Piscataway", "Middlesex", "08854"),

    /** Edison in Middlesex County. */
    EDISON("Edison", "Middlesex", "08817"),

    /** Princeton in Mercer County. */
    PRINCETON("Princeton", "Mercer", "08542"),

    /** Morristown in Morris County. */
    MORRISTOWN("Morristown", "Morris", "07960"),

    /** Clark in Union County. */
    CLARK("Clark", "Union", "07066");

    /**
     * The city in which the parking deck is located.
     */
    private final String city;

    /**
     * The county in which the parking deck is located.
     */
    private final String county;

    /**
     * The ZIP code of the location.
     */
    private final String zipCode;

    /**
     * Creates a location constant.
     *
     * @param city the city name
     * @param county the county name
     * @param zipCode the ZIP code
     */
    Location(String city, String county, String zipCode) {
        this.city = city;
        this.county = county;
        this.zipCode = zipCode;
    }


    /**
     * Finds a location by city name.
     *
     * @param city the city name
     * @return the matching location, or null if no location matches
     */
    public static Location findByCity(String city) {
        if (city == null) {
            return null;
        }

        for (Location location : Location.values()) {
            if (location.city.equalsIgnoreCase(city)) {
                return location;
            }
        }

        return null;
    }

    /**
     * Returns the city name.
     *
     * @return the city name
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Returns the county name.
     *
     * @return the county name
     */
    public String getCounty() {
        return this.county;
    }

    /**
     * Returns the ZIP code.
     *
     * @return the ZIP code
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * Returns the location's city name in uppercase.
     *
     * @return the uppercase city name
     */
    @Override
    public String toString() {
        return name();
    }
}
