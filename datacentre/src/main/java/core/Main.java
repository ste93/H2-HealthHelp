package core;

/**
 * It's Main class to start che main actor to start the data centre application.
 *
 * @author Giulia Lucchi
 */

public class Main {

    public static void main(String[] args) throws Exception {
        akka.Main.main(new String[]{MainActor.class.getName()});
    }

}
