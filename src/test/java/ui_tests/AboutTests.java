package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AboutPage;
import pages.HomePage;
import utils.HeaderMenuItem;

public class AboutTests extends ApplicationManager {

    @Test
    public void testAboutPageIsOpened() {
        AboutPage aboutPage = new HomePage(getDriver()).clickHeaderMenuItem(HeaderMenuItem.ABOUT);
        Assert.assertTrue(aboutPage.isAboutPageDisplayed());
    }
}