package com.example.recyclerviewissue

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val grantPermission = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @get:Rule
    val screenshot = ScreenshotTestRule()

    @Test
    fun shows_correct_items() {
        val scenario = launchActivity<MainActivity>()
        for (i in 0 until 100) {
            onView(withText("Button"))
                .perform(ViewActions.click())
            onView(withText("[Item D]"))
                .check(matches(isDisplayed()))
            Espresso.pressBack()
        }
        scenario.close()
    }
}