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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangeLanguageAndAssertTrueTest {

    private static String ARTICLE_NAME_ENGLISH = "Hello";
    private static String DANSK_TEXT = "Hallo";

    private static String ENGLISH = "English";
    private static String DANSK = "Dansk";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void changeLanguageAndAssertTrueTest() {

        searchArticleWithName(ARTICLE_NAME_ENGLISH);
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        //ASSERT WHETHER WE GOT THE RIGHT TEXT
        ViewInteraction textView = onView(
                allOf(withId(R.id.view_article_header_text), withText(containsString(ARTICLE_NAME_ENGLISH)),
                        isDisplayed()));
        textView.check(matches(withText( containsString(ARTICLE_NAME_ENGLISH) )));

        changeLanguage(DANSK);

        //ASSERT WHETHER WE GOT THE RIGHT TEXT
        textView = onView(
                allOf(withId(R.id.view_article_header_text), withText(DANSK_TEXT),
                        isDisplayed()));
        textView.check(matches(withText(DANSK_TEXT)));

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);

        //CHANGE BACK TO ENGLISH
        changeLanguage(ENGLISH);

        //ASSERT WHETHER WE GOT THE RIGHT TEXT
        textView = onView(
                allOf(withId(R.id.view_article_header_text), withText(containsString(ARTICLE_NAME_ENGLISH)),
                        isDisplayed()));
        textView.check(matches(withText( containsString(ARTICLE_NAME_ENGLISH) )));
        pressBack();

        //new addition
        MyUtil.clearHistory();

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

    private static void changeLanguage(String languageName) {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("b#3"), isDisplayed()));
        appCompatTextView.perform(click());

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction plainPasteEditText = onView(
                allOf(withId(R.id.langlinks_filter), isDisplayed()));
        plainPasteEditText.perform(replaceText(languageName), closeSoftKeyboard());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.langlinks_list),
                                withParent(withId(R.id.langlinks_list_container))),
                        0),
                        isDisplayed()));
        linearLayout3.perform(click());

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
