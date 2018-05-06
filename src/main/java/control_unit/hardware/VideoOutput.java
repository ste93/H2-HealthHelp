package control_unit.hardware;

/**
 * Interface for a generic video output for raspberryPI.
 *
 * @author manuBottax
 */
public interface VideoOutput {

    /**
     * Write a message on the video screen.
     *
     * @param message - the message to be printed.
     */
    void write(String message);

    /**
     * Reset the state of the screen, deleting previous written message.
     */
    void reset();
}
