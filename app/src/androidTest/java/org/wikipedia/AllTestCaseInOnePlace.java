package org.wikipedia;


import android.support.test.espresso.NoMatchingViewException;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AllTestCaseInOnePlace {
    private static int SLEEP_DURATION = 2;
    private static String ARTICLE_NAME_ENGLISH = "Hello";
    private static String DANSK_TEXT = "Hallo";

    private static String ENGLISH = "English";
    private static String DANSK = "Dansk";


    private static String READING_LIST_NAME = "Reading List";
    private static String READING_LIST_NAME_NEW = "Reading List Edited";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    //############################## START ChangeLanguageAndAssertTrueTest
    @Test
    public void changeLanguageAndAssertTrueTest() {

        searchArticleWithName(ARTICLE_NAME_ENGLISH);
        SleepUtil.sleep(SLEEP_DURATION);

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

        SleepUtil.sleep(SLEEP_DURATION);

        //CHANGE BACK TO ENGLISH
        changeLanguage(ENGLISH);

        //ASSERT WHETHER WE GOT THE RIGHT TEXT
        textView = onView(
                allOf(withId(R.id.view_article_header_text), withText(containsString(ARTICLE_NAME_ENGLISH)),
                        isDisplayed()));
        textView.check(matches(withText( containsString(ARTICLE_NAME_ENGLISH) )));
        pressBack();

        //new addition
        clearHistory();

    }


    /**
     * This function will search for the article "Hell0" and go there.
     */
    private static void searchArticleWithName(String articleName) {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.search_container), isDisplayed()));
        linearLayout.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(articleName), closeSoftKeyboard());

        SleepUtil.sleep(SLEEP_DURATION);
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

        SleepUtil.sleep(SLEEP_DURATION);
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

    //############################## END ChangeLanguageAndAssertTrueTest

    //############################## START FindInPageTest
    @Test
    public void findHelloInPageTest() {
        goToAnyArticle();

        searchForString("Hello");

        pressBackButton(2);

        searchForString("The");
        pressBackButton(4);
        clearHistory();
    }

    private static void goToAnyArticle() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));
    }

    private void searchForString (String searchStr) {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("b#4"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(searchStr), closeSoftKeyboard());

        ViewInteraction textView = onView(
                allOf(withId(R.id.find_in_page_match), childAtPosition(
                        allOf(withId(R.id.find_in_page_container),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.support.v7.widget.LinearLayoutCompat.class),
                                        0)),
                        1),
                        isDisplayed()));
        textView.check( matches(isDisplayed()));
    }

    //############################## END FindInPageTest

    //############################## START GeneralSettingsTest
    @Test
    public void generalSettingsTest() {
        generalSettingsShowImage();

        generalSettingsShowImage();

        clearHistory();

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"), isDisplayed()));
        actionMenuItemView2.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.explore_overflow_settings), withText("Settings"), isDisplayed()));
        appCompatTextView2.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.list),
                        withParent(withId(android.R.id.list_container)),
                        isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(11, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction textView = onView(
                allOf(withText("About"),
                        isDisplayed()));
        SleepUtil.sleep(SLEEP_DURATION);
        textView.check(matches(withText("About")));

        pressBack();
    }

    private static void generalSettingsShowImage() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.explore_overflow_settings), withText("Settings"), isDisplayed()));
        appCompatTextView.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.list),
                        withParent(withId(android.R.id.list_container)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        pressBack();

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.page_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction imageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.view_news_fullscreen_toolbar)),
                        isDisplayed()));
        imageButton2.perform(click());
    }
    //############################## END GeneralSettingsTest


    //############################## START HistoryCheckTest
    @Test
    public void historyCheckTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("History"), isDisplayed()));
        appCompatTextView.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction imageView = onView(
                allOf(withId(R.id.history_empty_image),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.page_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction imageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.view_news_fullscreen_toolbar)),
                        isDisplayed()));
        imageButton2.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);


        clearHistory();
    }
    //############################## END HistoryCheckTest


    //############################## START JumpingBetweenSectionsTest
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
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("b#5"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(R.id.page_toc_list),
                        3),
                        isDisplayed()));
        linearLayout2.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("b#5"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        withId(R.id.page_toc_list),
                        4),
                        isDisplayed()));
        linearLayout3.perform(click());

        SleepUtil.sleep(SLEEP_DURATION);
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

        clearHistory();
    }

    //############################## END JumpingBetweenSectionsTest


    //############################## START ManageReadListTest
    @Test
    public void addDeleteReadListTest() {

        addToReadingList(READING_LIST_NAME, 0);
        checkArticle(READING_LIST_NAME, "1 article", false);
        //now it is time to delete the list.
        cleanUp(READING_LIST_NAME);
        clearHistory();
    }

    @Test
    public void addItemToExistingReadListTest() {

        addToReadingList(READING_LIST_NAME, 0);
        addToExixtingReadingList(READING_LIST_NAME, 1);

        checkArticle(READING_LIST_NAME, "2 articles", false);

        //now it is time to delete the list.
        cleanUp(READING_LIST_NAME);
        clearHistory();
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
        clearHistory();
    }

    private void cleanUp(String listName) {
        //now it is time to delete the list.
        goToList(listName);
        openEditOption();
        deleteReadListItem();

        SleepUtil.sleep(SLEEP_DURATION);
        SleepUtil.sleep(SLEEP_DURATION);
        SleepUtil.sleep(SLEEP_DURATION);
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
    //############################## END ManageReadListTest

    //############################## START SearchAtricleTest
    @Test
    public void searchAtricleTest() {
        searchArticleWithName(ARTICLE_NAME_ENGLISH);

        //ASSERT WHETHER WE GOT THE RIGHT TEXT
        ViewInteraction textView = onView(
                allOf(withId(R.id.view_article_header_text), withText(containsString(ARTICLE_NAME_ENGLISH)),
                        isDisplayed()));
        textView.check(matches(withText( containsString(ARTICLE_NAME_ENGLISH) )));
        pressBack();

        clearHistory();
    }
    //############################## END SearchAtricleTest

    //############################## START ThemeChangeTest
    @Test
    public void themeChangeTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.view_list_card_list), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.view_news_fullscreen_link_card_list), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Font and theme"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonColorsDark), withText("Dark"), isDisplayed()));
        appCompatButton.perform(click());

        pressBack();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Font and theme"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonColorsLight), withText("Light"), isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.page_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        pressBack();

        clearHistory();

    }
    //############################## END ThemeChangeTest

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
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_clear_all_history), withContentDescription("Clear history"), isDisplayed()));
        actionMenuItemView.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.history_empty_image),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));
        SleepUtil.sleep(SLEEP_DURATION);

        ViewInteraction appCompatTextView4 = onView(
                allOf(withText("Explore"), isDisplayed()));
        appCompatTextView4.perform(click());
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
