package control_unit.hardware;

/**
 * Interface for a generic led for raspberryPI.
 * 
 * @author manuBottax.
 */
public interface Led
{
    /**
     * Turn on the led; If it is already on nothing happen.
     */
    void turnOn();
    
    /**
     * Turn off the led; If it is already off nothing happen.
     */
    void turnOff();
    
    /**
     * Change the led state, if it is on this turn it off, else it turn it on.
     */
    void switchState();
    
     /**
     * make the led blinking.
     */
    void blink();
    
    /**
     * Get the state of the led. 
     * @return state : true if the led is on, false elsewhere.
     */
    boolean getState();
}
