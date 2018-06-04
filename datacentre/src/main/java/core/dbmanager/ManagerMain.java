package core.dbmanager;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import core.pubsub.EmergencyMessage;

/**
 * Created by lucch on 27/05/2018.
 */
public class ManagerMain {


        public static void main(String[] args) throws Exception {
            ActorSystem.apply("datacentre").actorSelection( "akka.tcp://datacentre@127.0.0.1:2552/user/messageReceiver").tell(new EmergencyMessage(1), ActorRef.noSender());;

            //     H2dbManager dbManager = new H2dbManagerImpl();
      //   System.out.println(dbManager.getDrugs("giulia.lucchi", Optional.empty(), Optional.empty()));
            //  System.out.println(dbManager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.empty(), Optional.empty()));

            //System.out.println(dbManager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1999-01-01 01:00"), Optional.empty()));

           // System.out.println(dbManager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1995-01-01 13:00"), Optional.of("2003-01-01 12:00")));

           //marghe
            // AssManager associationsManager = new AssociationsManagerImpl();
            //associationsManager.getPatientData("kksksksk.lucchi");
            //associationsManager.getDoctorData("mario.rossi");
            //System.out.println(associationsManager.createNewDoctor("kdkdkdk.pecos", "marghe", "pecos", "kjdjdjd"));

    }

}
