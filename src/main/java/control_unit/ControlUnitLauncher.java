package control_unit;

import control_unit.pub_sub.PatientDataPublisher;

public class ControlUnitLauncher {

    public static void main(String[] args) {

        PatientDataPublisher publisher = new PatientDataPublisher("pat" + Math.random() * 1000);

        for(int i = 0; i < 100; i ++){
            publisher.sendData("heartBeat : " + Math.random() * 150);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        publisher.close();

    }
}
