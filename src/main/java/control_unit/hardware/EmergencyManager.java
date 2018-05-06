package control_unit.hardware;;

import java.util.concurrent.Callable;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
/**
 * Class to handle emergency protocol.
 * 
 * @author manubottax
 * @version 1.0 - 02/03/18
 */
public class EmergencyManager
{
    private SimpleLed emergencyLed;
    private SimpleLed callLed;
    private Buzzer buzz;
    private myLcd lcd;
    final GpioController gpio;
    final GpioPinDigitalInput button;
    
    private boolean emergency;

    /**
     * Constructor for objects of class EmergencyManager
     */
    public EmergencyManager()
    {
        this.emergency = false;
        emergencyLed = new SimpleLed(5);
        callLed = new SimpleLed(29);
        buzz = new Buzzer();
        lcd = new myLcd();
        gpio = GpioFactory.getInstance();
        button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
        button.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        button.addTrigger(new GpioCallbackTrigger(new Callable<Void>(){
            public Void call() throws Exception {
                if (emergency) {
                    emergency = false;
                    reset();
                    lcd.write("NO EMERGENCY");
                }
                return null;
            }
        }));
    }
    
    private void reset(){
        emergencyLed.turnOff();
        buzz.stop();
        lcd.reset();
    }
        
    public void startEmergency() {
        this.emergency = true;
        this.emergencyLed.blink();
        this.buzz.beep();
        this.lcd.write("EMERGENCY");
        
        try{
            Thread.sleep(10000);
        }       
        catch (Exception e ) { e.printStackTrace(); }
        
        // if alarm is not stopped in this time call emergency rescuers;
        if(this.emergency){
            callRescuers();
        }
        else {
            System.out.println("NO MORE EMERGENCY");
            try{
                Thread.sleep(1000);
            }       
            catch (Exception e ) { e.printStackTrace(); }
            lcd.reset();
        }
    }
    
    private void callRescuers(){
        System.out.println("CALLING EMERGENCY RESCUERS");
        lcd.write("CALLING RESCUERS");
        this.buzz.stop();
        this.emergencyLed.turnOn();
        this.callLed.turnOn();
        emergency = false;
        try{
                Thread.sleep(5000);
            }       
            catch (Exception e ) { e.printStackTrace(); }
        reset();
    }     
}
