package control_unit.hardware;

/**
 * Interface for a generic sound output for raspberryPI.
 * 
 * @author manuBottax
 */
public interface SoundOutput {

    /**
     * Start a continuous beep. If already beeping does nothing.
     */
    void beep();
    
    /**
     * Start a continuous beep for a certain time. If already beeping does nothing.
     *
     * @param msec - the duration of the sound.
     */
    void beep(int msec);
    
    /**
     * Stop the sound. If already stopped does nothing.
     */
    void stop();
}
