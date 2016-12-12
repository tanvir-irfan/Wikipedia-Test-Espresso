package org.wikipedia;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class JumpingBetweenSectionsTest {

    private static String ARTICLE_NAME_ENGLISH = "Hello";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void jumpingBetweenDifferentSections() {
        searchArticleWithName(ARTICLE_NAME_ENGLISH);

        ViewInteraction appCompatTextView = onView(
                allOf(withText("b#5"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(R.id.page_toc_list),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("b#5"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(R.id.page_toc_list),
                        3),
                        isDisplayed()));
        linearLayout2.perform(click());

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("b#5"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        withId(R.id.page_toc_list),
                        4),
                        isDisplayed()));
        linearLayout3.perform(click());

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction webView = onView(
                allOf(withId(R.id.page_web_view),
                        childAtPosition(
                                allOf(withId(R.id.page_contents_container),
                                        childAtPosition(
                                                withId(R.id.page_refresh_container),
                                                0)),
                                0),
                        isDisplayed()));
        webView.check(matches(isDisplayed()));

        pressBack();
    }

    /**
     * This function will search for the article "Hell0" and go there.
     */
    private static void searchArticleWithName(String articleName) {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.search_container), isDisplayed()));
        linearLayout.perform(click());

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(articleName), closeSoftKeyboard());

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.page_list_item_container),
                        childAtPosition(
                                allOf(withId(R.id.search_results_list),
                                        withParent(withId(R.id.search_results_container))),
                                0),
                        isDisplayed()));
        linearLayout2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
