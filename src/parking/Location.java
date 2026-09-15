package parking;

/**
 * Represents a supported parking-deck location.
 *
 * @author wyattzhang
 */
public enum Location {
    BRIDGEWATER("Bridgewater", "Somerset", "08807"),
    PISCATAWAY("Piscataway", "Middlesex", "08854"),
    EDISON("Edison", "Middlesex", "08817"),
    PRINCETON("Princeton", "Mercer", "08542"),
    MORRISTOWN("Morristown", "Morris", "07960"),
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
     * Returns the city name.
     *
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the county name.
     *
     * @return the county name
     */
    public String getCounty() {
        return county + " County";
    }

    /**
     * Returns the ZIP code.
     *
     * @return the ZIP code
     */
    public String getZipCode() {
        return zipCode;
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