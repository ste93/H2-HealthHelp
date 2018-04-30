package user_server;

import user_server.pub_sub.AdvicePublisher;
import user_server.pub_sub.HistoryRequestPublisher;

public class UserServerLauncher {
    public static void main(String[] args) {

        String patID = "pat" + Math.random() * 1000;

        AdvicePublisher advicePublisher = new AdvicePublisher(patID);
        HistoryRequestPublisher historyRequester = new HistoryRequestPublisher(patID);

        for (int i = 0; i < 200; i++) {
            if ( i % 2 == 0) {
                advicePublisher.sendAdvice("Warning, it will rain tomorrow !");
            }
            else {
                historyRequester.sendRequest("Give me my history, please !");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        advicePublisher.close();
        historyRequester.close();
    }
}
