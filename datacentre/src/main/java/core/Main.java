package core;

import core.pubsub.core.PatientDataReceiver;

public class Main {

    public static void main(String[] args) throws Exception {
        akka.Main.main(new String[] { MainActor.class.getName() });
    }

}
