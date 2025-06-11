package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AboutTests extends ApplicationManager {

    @Test
    public void testAboutPageDisplayed() {
        openAboutPage();
        Assert.assertTrue(aboutPage.isAboutPageDisplayed(), "About Page is not displayed");
    }
}