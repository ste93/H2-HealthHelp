package control_unit.hardware;

import com.pi4j.wiringpi.Lcd;

/**
 * A simple handler for an hardware 16x2 LCD screen on raspberryPI.
 * 
 * @author  manuBottax
 */
public class SimpleLcd implements VideoOutput {

    private final static int LCD_ROWS = 2;
    private final static int LCD_COLUMNS = 16;
    private final static int LCD_BITS = 4;
    
    private final int lcdHandler;

    private String projectName;

    /**
     * Default constructor for SimpleLcd class
     *
     * @param projectName - the name of the project, printed on the first line of the LCD.
     */
    public SimpleLcd(String projectName)
    {
        this.projectName = projectName;

        this.lcdHandler = Lcd.lcdInit(LCD_ROWS, LCD_COLUMNS,LCD_BITS, 11,10,0,1,2,3,0,0,0,0);
        
        if(this.lcdHandler == -1 ) {
            System.out.println("[LCD] - LCD INITIALIZATION FAILED");
        }
        else {
            System.out.println("[LCD] - LCD INITIALIZATION COMPLETED");
        }

            
        Lcd.lcdClear(this.lcdHandler);
        Lcd.lcdPuts(this.lcdHandler, this.projectName);
    }

    /**
     * Method to test the hardware and the connection.
     */
    public void testLCD() {
       System.out.println("[LCD] - Testing lcd");
       Lcd.lcdHome(lcdHandler);
       Lcd.lcdPuts(lcdHandler, this.projectName);
       
       Lcd.lcdPosition(lcdHandler,0,1); // line 2
       Lcd.lcdPuts(lcdHandler,"lcd test");
    }

    /**
     * Write a message on the video screen.
     * This implementation write only on line 2 because keep the default project name on line 1.
     *
     * @param message - the message to be printed.
     */
    @Override
    public void write(String message){
        System.out.println("[LCD] - WritingString");
        //write on line 2
        Lcd.lcdPosition(lcdHandler,0,1);
        Lcd.lcdPuts(lcdHandler,message);
    }

    @Override
    public void reset(){
        Lcd.lcdClear(lcdHandler);
        Lcd.lcdPuts(lcdHandler, this.projectName);
    }
}
