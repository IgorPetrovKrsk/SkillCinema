package igor.petrov.final_android_lvl1

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import igor.petrov.final_android_lvl1.data.DatabaseModule
import igor.petrov.final_android_lvl1.presentation.ui.home.HomeFragment
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest{
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun homeFragmentCreationTest(){
        launchFragmentInHiltContainer<HomeFragment>(){}
        onView(withId(R.id.btnShowAllPremires)).check(matches(isDisplayed()))
    }

    @Test
    fun clickShowAllPremieres_navigateToFragmentShowAll(){
        val navController = mockk<NavController>(relaxed = true)
        launchFragmentInHiltContainer<HomeFragment>(     ){
            Navigation.setViewNavController(requireView(),navController)
        }
        onView(withId(R.id.btnShowAllPremires)).perform(click())

        verify {
           navController.navigate(R.id.action_navigation_home_to_fragmentShowAll,match{it.getString("collection")=="Premieres" && it.getString("showType")=="premieres" && it.getString("label")=="Premieres"})
        }
    }


}