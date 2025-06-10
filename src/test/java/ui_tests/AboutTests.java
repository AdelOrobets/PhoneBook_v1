package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AboutPage;
import pages.HomePage;

public class AboutTests extends ApplicationManager {

    @Test
    public void testAboutPageIsOpened() {
        HomePage homePage = new HomePage(getDriver());
        homePage.clickAboutHeaderLink();

        AboutPage aboutPage = new AboutPage(getDriver());
        Assert.assertTrue(aboutPage.isAboutPageDisplayed());
    }
}