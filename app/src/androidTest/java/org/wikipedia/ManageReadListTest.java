package org.wikipedia;


import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManageReadListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static String READING_LIST_NAME = "Reading List";
    private static String READING_LIST_NAME_NEW = "Reading List Edited";
    private static int SLEEP_DURATION = MyUtil.SLEEP_DURATION;

    @Test
    public void addDeleteReadListTest() {

        addToReadingList(READING_LIST_NAME, 0);
        checkArticle(READING_LIST_NAME, "1 article", false);
        //now it is time to delete the list.
        cleanUp(READING_LIST_NAME);
        MyUtil.clearHistory();
    }

    @Test
    public void addItemToExistingReadListTest() {

        addToReadingList(READING_LIST_NAME, 0);
        addToExixtingReadingList(READING_LIST_NAME, 1);

        checkArticle(READING_LIST_NAME, "2 articles", false);

        //now it is time to delete the list.
        cleanUp(READING_LIST_NAME);
        MyUtil.clearHistory();
    }

    @Test
    public void editReadListNameTest() {

        addToReadingList(READING_LIST_NAME, 0);
        goToList(READING_LIST_NAME);
        openEditOption();

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction plainPasteEditText = onView(
                allOf(withId(R.id.reading_list_title)));
        plainPasteEditText.perform(scrollTo(), replaceText(READING_LIST_NAME_NEW), closeSoftKeyboard());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        pressBack();
        SleepUtil.sleep(SLEEP_DURATION);

        try {
            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.snackbar_action), withText("Got it"),
                            isDisplayed()));
            appCompatButton.perform(click());
            SleepUtil.sleep(SLEEP_DURATION);
        }catch(NoMatchingViewException e) {
            SleepUtil.sleep(SLEEP_DURATION);
        }

        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);

        checkArticle(READING_LIST_NAME_NEW, null, true);
        //now it is time to delete the list.
        cleanUp(READING_LIST_NAME_NEW);
        MyUtil.clearHistory();
    }

    private void cleanUp(String listName) {
        //now it is time to delete the list.
        goToList(listName);
        openEditOption();
        deleteReadListItem();

        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        SleepUtil.sleep(MyUtil.SLEEP_DURATION);
        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
    }

    /**
     * this will work only when am in the EDIT_OPTION menu.
     * that's why it is ensured that i am in the EDIT_OPTION menu
     * by calling goToEditOption().
     */
    private void deleteReadListItem() {
        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.reading_list_delete_link), withText("Delete reading list")));
        appCompatTextView3.perform(scrollTo(), click());
        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton3.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);;
        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());
    }

    private void addToExixtingReadingList(String listName, int position) {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(position, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));
        SleepUtil.sleep(SLEEP_DURATION);;

        ViewInteraction articleTabLayout = onView(
                allOf(withText("b#1"), isDisplayed()));
        articleTabLayout.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);


        try {
            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.onboarding_button), withText("Got it"),
                            withParent(withId(R.id.onboarding_container)),
                            isDisplayed()));
            appCompatButton.perform(click());
            SleepUtil.sleep(SLEEP_DURATION);
        }catch(NoMatchingViewException e) {
            ViewInteraction linearLayout = onView(
                    allOf(withText(listName),
                            isDisplayed()));
            linearLayout.perform(click());
            SleepUtil.sleep(SLEEP_DURATION);
        }

        pressBack();
        SleepUtil.sleep(SLEEP_DURATION);

        pressBack();
        SleepUtil.sleep(SLEEP_DURATION);
    }

    private void addToReadingList(String listName, int position) {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(position, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));
        SleepUtil.sleep(SLEEP_DURATION);;

        ViewInteraction articleTabLayout = onView(
                allOf(withText("b#1"), isDisplayed()));
        articleTabLayout.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);


        try {
            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.onboarding_button), withText("Got it"),
                            withParent(withId(R.id.onboarding_container)),
                            isDisplayed()));
            appCompatButton.perform(click());
            SleepUtil.sleep(SLEEP_DURATION);
        }catch(NoMatchingViewException e) {
            ViewInteraction linearLayout = onView(
                    allOf(withId(R.id.create_button),
                            withParent(withId(R.id.lists_container)),
                            isDisplayed()));
            linearLayout.perform(click());
            SleepUtil.sleep(SLEEP_DURATION);
        }

        ViewInteraction plainPasteEditText = onView(
                allOf(withId(R.id.reading_list_title)));
        plainPasteEditText.perform(scrollTo(), replaceText(listName), closeSoftKeyboard());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        pressBack();
        SleepUtil.sleep(SLEEP_DURATION);

        pressBack();
        SleepUtil.sleep(SLEEP_DURATION);

        //check whether added properly or not!
        SleepUtil.sleep(SLEEP_DURATION);

    }

    private void checkArticle(String listName, String textToMatch, boolean isTitle) {

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("My lists"), isDisplayed()));
        appCompatTextView2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction textView2 = onView(
                allOf(withId(isTitle ? R.id.item_title : R.id.item_count), withText(isTitle ? listName : textToMatch),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_container),
                                        1),
                                isTitle ? 0 : 1),
                        isDisplayed()));
        textView2.check(matches(withText(isTitle ? listName : textToMatch)));

        //go back to explore.
        ViewInteraction appCompatTextView4 = onView(
        allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());
    }

    private void openEditOption() {

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.item_container), isDisplayed()));
        linearLayout2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.button_edit), withContentDescription("Edit reading list details"), isDisplayed()));
        floatingActionButton.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);
    }

    private void goToList(String listName) {
        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("My lists"), isDisplayed()));
        appCompatTextView2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_title), withText(listName),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_container),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(listName)));
        SleepUtil.sleep(SLEEP_DURATION);
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
