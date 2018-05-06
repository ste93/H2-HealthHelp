package control_unit.hardware;;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;

/**
 * A Simple Led controller. it controll a physical led connected to the pin 11 (GPIO 00).
 * 
 * @author manuBottax 
 * @version 1.0 - 02/03/18
 */
public class SimpleLed implements Led
{
    private boolean state;
    private final GpioController gpio;
    private final GpioPinDigitalOutput ledPin;

    /**
     * Constructor for objects of class SimpleLed
     */
    public SimpleLed(int pin)
    {
        
        this.state = false;
        gpio = GpioFactory.getInstance();
        ledPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        ledPin.setShutdownOptions(true, PinState.LOW);
        System.out.println("[LED] initialized");
    }
    
    @Override
    public void turnOn(){
        System.out.println("[LED] Turning on emergency led");
        ledPin.high();
        this.state = true;
    }
    
    @Override
    public void turnOff(){
        System.out.println("[LED] Turning off emergency led");
        ledPin.low();
        this.state = false;
    }
    
    @Override
    public void switchState(){
        System.out.println("[LED] switching emergency led");
        ledPin.toggle();
        this.state = ! this.state;
    }
    
    @Override
    public void blink(){
        System.out.println("[LED] blinking emergency led");
        ledPin.blink(1000, 10000);
    }
    
    @Override
    public boolean getState(){
        return this.state;
    }
}
