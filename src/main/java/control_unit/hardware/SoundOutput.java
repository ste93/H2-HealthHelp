package control_unit.hardware;

/**
 * A generic sound output.
 * 
 * @author manuBottax 
 * @version 1.0 - 02/03/18
 */
public interface SoundOutput
{
    /**
     * Start a continuous beep. if already beeping does nothing.
     */
    void beep();
    
    /**
     * Start a continuous beep for a certain time. if already beeping does nothing.
     * @param msec - the duration of the sound.
     */
    void beep(int msec);
    
    /**
     * stop the sound. if already stopped does nothing.
     */
    void stop();
}
