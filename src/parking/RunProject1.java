package parking;

/**
 * Provides the entry point for the Parking Management System.
 *
 * @author wyattzhang, ethanvu
 */

public class RunProject1 {

    /** Prevents creation of this driver-only class. */
    private RunProject1() {
    }

    /**
     * Starts the program.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        new Operation().run();
    }
}
