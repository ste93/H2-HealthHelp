package datacenter;

import datacenter.pub_sub.AdviceReceiver;
import datacenter.pub_sub.PatientDataReceiver;
import datacenter.pub_sub.RequestReceiver;

public class DataCentreLauncher {

    public static void main(String[] args) {
        PatientDataReceiver dataReceiver = new PatientDataReceiver();
        AdviceReceiver adviceReceiver = new AdviceReceiver();
        RequestReceiver historyRequestREceiver = new RequestReceiver();
    }

}
