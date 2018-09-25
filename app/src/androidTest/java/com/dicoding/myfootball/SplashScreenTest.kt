package com.dicoding.myfootball


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.dicoding.myfootball.R.id.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @Rule
    @JvmField var mActivityTestRule = ActivityTestRule(SplashScreen::class.java)

    @Test
    fun splashScreenTest() {
        //SplashScreen
        Thread.sleep(2500)
        onView(withId(imgBtn_Team)).check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(imgBtn_Team)).perform(click())
        Thread.sleep(2500)
        onView(withId(list_league))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(list_league))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
        Thread.sleep(2500)
        onView(withId(list_league)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(17, click()))
        Thread.sleep(1500)
        onView(withId(list_team))
                .check(matches(isDisplayed()))
        Thread.sleep(2500)
        onView(withId(list_team)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))
        Thread.sleep(2500)
        onView(withId(listPlayer)).perform(scrollTo())
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(listPlayer)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        onView(withId(add_to_favorite)).perform(click())
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        onView(withId(imgBtn_Match)).check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(imgBtn_Match)).perform(click())
        Thread.sleep(2500)
        onView(withId(list_league))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(list_league))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(38))
        Thread.sleep(2500)
        onView(withId(list_league)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(32, click()))
        Thread.sleep(20500)
        onView(withId(next_match_list))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(next_match_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1500)
        onView(withId(add_to_favorite)).perform(click())
        pressBack()
        Thread.sleep(1500)
        onView(allOf(withText("Last Match"), isDescendantOfA(withId(tabs)))).perform(click())
        Thread.sleep(1500)
        onView(withId(last_match_list))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(last_match_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1500)
        onView(withId(add_to_favorite)).perform(click())
        pressBack()
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        onView(withId(imgBtn_Favorite)).check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(imgBtn_Favorite)).perform(click())
        Thread.sleep(1500)
        onView(allOf(withText("Next Match"), isDescendantOfA(withId(tabs)))).perform(click())
        Thread.sleep(1500)
        onView(allOf(withText("Last Match"), isDescendantOfA(withId(tabs)))).perform(click())
        Thread.sleep(1500)
        onView(allOf(withText("Team"), isDescendantOfA(withId(tabs)))).perform(click())
        Thread.sleep(1500)
        pressBack()
        Thread.sleep(1500)
        Thread.sleep(5500)
    }
}
