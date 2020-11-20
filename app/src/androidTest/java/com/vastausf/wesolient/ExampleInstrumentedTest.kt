package com.vastausf.wesolient

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun useAppContext() {
        onView(withId(R.id.bCreate))
            .perform(click())

        onView(withId(R.id.etTitle))
            .perform(typeText("Websocket.org"))
        onView(withId(R.id.etUrl))
            .perform(typeText("wss://echo.websocket.org"))
    }
}