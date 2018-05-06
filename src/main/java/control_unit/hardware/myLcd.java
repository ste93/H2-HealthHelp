package control_unit.hardware;

import com.pi4j.wiringpi.Lcd;

/**
 * Write a description of class Lcd here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myLcd
{
    private final static int LCD_ROWS = 2;
    private final static int LCD_COLUMNS = 16;
    private final static int LCD_BITS = 4;
    
    private final int lcdHandler;
    /**
     * Constructor for objects of class Lcd
     */
    public myLcd()
    {
        //System.out.println("[LCD] - LCD INIITIALIZATION STARTED");
        
        /*if (Gpio.wiringPiSetup() == -1) {
            System.out.println("[LCD] - WiringPi Setup failed");
        }*/
        
        lcdHandler = Lcd.lcdInit(LCD_ROWS, LCD_COLUMNS,LCD_BITS, 11,10,0,1,2,3,0,0,0,0);
        
        if(lcdHandler == -1 ) {
            System.out.println("[LCD] - LCD INIITIALIZATION FAILED");
        }
        else {
            System.out.println("[LCD] - LCD INIITIALIZATION COMPLETED");
        }
            
        Lcd.lcdClear(lcdHandler);
        Lcd.lcdPuts(lcdHandler, "H2 - Health Help");
    }

    public void testLCD() {
       System.out.println("[LCD] - Testing lcd");
       Lcd.lcdHome(lcdHandler);
       Lcd.lcdPuts(lcdHandler, "H2 - Health Help");
       
       Lcd.lcdPosition(lcdHandler,0,1); // line 2
       Lcd.lcdPuts(lcdHandler,"lcd test");
    }
    
    public void write(String str){
        System.out.println("[LCD] - WritingString");
        Lcd.lcdPosition(lcdHandler,0,1);
        Lcd.lcdPuts(lcdHandler,str);
    }
    
    public void reset(){
        Lcd.lcdClear(lcdHandler);
        Lcd.lcdPuts(lcdHandler, "H2 - Health Help");
    }
}
