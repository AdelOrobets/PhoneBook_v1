package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.HeaderMenuItem;

public class HomeTests extends ApplicationManager {

    @Test
    public void homeHeaderLinkPositive() {
        HomePage homePage = new HomePage(getDriver()).clickHeaderMenuItem(HeaderMenuItem.HOME);
        Assert.assertTrue(homePage.isHomePageDisplayed());
    }
}