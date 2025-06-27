package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class AboutTests extends ApplicationManager {

    @Test
    public void testAboutPageDisplayed() {
        openAboutPage();
        Assert.assertTrue(aboutPage.isAboutPageDisplayed(), "About Page is not displayed");
    }
}