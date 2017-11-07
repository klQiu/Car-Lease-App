package com.example.elvis.carleaseapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PostFormActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Test for regular string inputs
     */
    @Test
    public void basicPostFormActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editTitle));
        appCompatEditText.perform(scrollTo(), replaceText("testcarandroid"), closeSoftKeyboard())
                .check(matches(withText("testcarandroid")));

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.editYear));
        appCompatEditText2.perform(scrollTo(), replaceText("1997"), closeSoftKeyboard())
                .check(matches(withText("1997")));

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.editBrand));
        appCompatEditText3.perform(scrollTo(), replaceText("honda"), closeSoftKeyboard())
                .check(matches(withText("honda")));

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.editColour));
        appCompatEditText4.perform(scrollTo(), replaceText("black"), closeSoftKeyboard())
                .check(matches(withText("black")));

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.editMileage));
        appCompatEditText5.perform(scrollTo(), replaceText("1445"), closeSoftKeyboard())
                .check(matches(withText("1445")));

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.editPrice));
        appCompatEditText6.perform(scrollTo(), replaceText("6000"), closeSoftKeyboard())
                .check(matches(withText("6000")));

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.spinner));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("10 days"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                withId(R.id.editEmail));
        appCompatEditText7.perform(scrollTo(), replaceText("abc@gmail.com"), closeSoftKeyboard())
                .check(matches(withText("abc@gmail.com")));

        ViewInteraction appCompatEditText8 = onView(
                withId(R.id.editTelephone));
        appCompatEditText8.perform(scrollTo(), replaceText("7786811111"), closeSoftKeyboard())
                .check(matches(withText("7786811111")));

        onView(withId(R.id.submit_button)).perform(scrollTo(), click());

    }

    /**
     * Test for missing fields
     */
    @Test
    public void nullInputPostFormActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        onView(withId(R.id.submit_button)).perform(scrollTo(), click());

    }


    /**
     * Test for pressing upload image button
     */
    @Test
    public void imagePostFormActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

//        Intent resultData = new Intent();
//        String stubUri = "abc";
//        resultData.putExtra("img", stubUri);
//        Instrumentation.ActivityResult result =
//                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
//
//
//        Intents.intending(IntentMatchers.hasData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//                .respondWith(result);
        onView(withId(R.id.loadimage)).perform(scrollTo(), click());

    }

}
