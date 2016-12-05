package org.wikipedia;

import static android.support.test.espresso.Espresso.pressBack;

/**
 * Created by tanvi on 12/4/2016.
 */

public class MyUtil {
    public static int SLEEP_DURATION = 2;
    public static void pressBackButton(int numberOfBackPress) {
        for(int i = 1; i <= numberOfBackPress; i++) {
            pressBack();
            SleepUtil.sleep(SLEEP_DURATION);
        }
    }
}
