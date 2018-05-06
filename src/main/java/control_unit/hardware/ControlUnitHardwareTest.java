package control_unit.hardware;

/**
 * Class used to test the hardware part of control Unit.
 * If run on system without PI4J installed it crash.
 * 
 * @author manuBottax
 */
public class ControlUnitHardwareTest
{
    
    public static void main (String[] args) {
        EmergencyManager em = new EmergencyManager();

        try{
            Thread.sleep(2000);
            em.startEmergency();
             while(true){
               Thread.sleep(1000); 
            }
        }       
        catch (InterruptedException ex ) { ex.printStackTrace();}
    }
}
