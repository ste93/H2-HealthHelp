package control_unit.hardware;

/**
 * A simple Led interface.
 * 
 * @author manuBottax
 * @version 1.0 - 02/03/18
 */
public interface Led
{
    /**
     * Turn on the Led; If it is already on nothing happen.
     */
    void turnOn();
    
    /**
     * Turn off the Led; If it is already off nothing happen.
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
