package com.example.storyapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.storyapp.ui.activity.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    private val dummyEmail = "ryandaman@gamil.com"
    private val dummyPass = "qwerty123"

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginProcess_Success() {
        Espresso.onView(withId(R.id.ed_login_email)).perform(ViewActions.typeText(dummyEmail))
        Espresso.onView(withId(R.id.ed_login_password)).perform(ViewActions.typeText(dummyPass))
        Espresso.onView(withId(R.id.btn_login)).perform(ViewActions.click())
    }
}