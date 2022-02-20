package nz.co.jonker.motors.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import nz.co.jonker.motors.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<SearchActivity>()

    @Test
    fun testSearchBottomSheetDisplaysCorrectly() {
        onView(withId(R.id.search_button)).perform(click())
        onView(withId(R.id.make)).check(matches(isDisplayed()))
        onView(withId(R.id.model)).check(matches(isDisplayed()))
        onView(withId(R.id.year)).check(matches(isDisplayed()))
        onView(withId(R.id.search_button)).check(matches(isDisplayed()))
    }
}
