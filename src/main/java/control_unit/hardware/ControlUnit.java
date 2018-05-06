package control_unit.hardware;

/**
 * The main class for the Control Unit.
 * 
 * @author manuBottax 
 * @version 1.0 - 02/03/18
 */
public class ControlUnit
{
    
    public static void main (String[] args) {
        EmergencyManager em = new EmergencyManager();        
        try{
            Thread.sleep(2000);
            em.startEmergency();
            Thread.sleep(1000);
             while(true){
               Thread.sleep(1000); 
            }
        }       
        catch (Exception e ) { e.printStackTrace();}        
    }
}
