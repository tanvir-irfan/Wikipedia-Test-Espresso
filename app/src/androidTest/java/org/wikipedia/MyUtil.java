package org.wikipedia;

import android.support.test.espresso.ViewInteraction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by tanvi on 12/4/2016.
 */

public class MyUtil {
    public static int SLEEP_DURATION = 1;
    public static void pressBackButton(int numberOfBackPress) {
        for(int i = 1; i <= numberOfBackPress; i++) {
            pressBack();
            SleepUtil.sleep(SLEEP_DURATION);
        }
    }


    public static void clearHistory() {
        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("History"), isDisplayed()));
        appCompatTextView3.perform(click());
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_clear_all_history), withContentDescription("Clear history"), isDisplayed()));
        actionMenuItemView.perform(click());
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.history_empty_image),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
    }
}
