package server;

import java.util.Timer;
import java.util.TimerTask;

public class test {
    public static void main(String[] args) {
        Timer timer = new Timer();
        final int[] i = {10};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i[0] < 0){
                    timer.cancel();

                }
                System.out.println(i[0]);
                i[0]--;
            }
        }, 0, 1000);
    }
}
