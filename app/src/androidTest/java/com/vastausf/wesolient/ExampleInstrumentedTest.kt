package com.vastausf.wesolient

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    private lateinit var testScopeTitle: String
    private lateinit var testScopeUrl: String

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Before
    fun initializeStrings() {
        testScopeTitle = "testScopeTitle"
        testScopeUrl = "testScopeUrl"
    }

    @Test
    fun createNewScope() {
        onView(withId(R.id.fabCreateScope))
            .perform(click())

        onView(withId(R.id.etScopeTitle))
            .perform(typeText(testScopeTitle))
        onView(withId(R.id.etScopeUrl))
            .perform(typeText(testScopeUrl))

        onView(withId(R.id.bCreateScope))
            .perform(click())
    }
}