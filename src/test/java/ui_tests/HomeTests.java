package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class HomeTests extends ApplicationManager {

    @Test
    public void testHomePageDisplayed() {
        Assert.assertTrue(new HomePage(driver).isHomePageDisplayed(), "Home Page is not displayed");
    }
}