package control_unit.hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;

/**
 * Class to control a physical Buzzer connected to Pin 13 (GPIO 01).
 * 
 * @author manuBottax
 * @version 1.0 - 02/03/18
 */
public class Buzzer implements SoundOutput
{
    private final GpioController gpio;
    private final GpioPinDigitalOutput buzzPin;

    public Buzzer()
    {
        gpio = GpioFactory.getInstance();
        buzzPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
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
