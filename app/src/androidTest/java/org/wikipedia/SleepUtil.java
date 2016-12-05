package org.wikipedia;

/**
 * Created by sean on 10/14/2016.
 */

public class SleepUtil {
    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
