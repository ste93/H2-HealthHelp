package control_unit.hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;

/**
 * A simple handler for an hardware buzzer on raspberryPI.
 * 
 * @author manuBottax
 */
public class Buzzer implements SoundOutput
{
    private final GpioController gpio;
    private final GpioPinDigitalOutput buzzPin;

    /**
     * Default constructor for a buzzer connected to pin 16 ( GPIO_04 ).
     */
    public Buzzer()
    {
        gpio = GpioFactory.getInstance();
        buzzPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        buzzPin.setShutdownOptions(true, PinState.LOW);
        System.out.println("[BUZZ] initialized");
    }

    /**
     * Default constructor for a buzzer connected to the specified pin.
     *
     * @param pin - the pin number in which the physical buzzer is connected.
     */
    public Buzzer(int pin){
        gpio = GpioFactory.getInstance();
        buzzPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        buzzPin.setShutdownOptions(true, PinState.LOW);
        System.out.println("[BUZZ] initialized");
    }
    
    @Override
    public void beep(){
        System.out.println("[BUZZ] Turning on buzzer ");
        buzzPin.high();
    }
    
    @Override
    public void beep(int msec){
        System.out.println("[BUZZ] Turning on buzzer for " + msec + " msec");
        buzzPin.pulse(msec);
    }
    
    @Override
    public void stop(){
        System.out.println("[BUZZ] Turning off buzzer");
        buzzPin.low();
    }
}
