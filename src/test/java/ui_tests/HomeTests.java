package ui_tests;

import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class HomeTests extends ApplicationManager {

    @Test
    public void homeHeaderLinkPositive() {
        HomePage homePage = new HomePage(getDriver());
        homePage.clickHomeHeaderLink();
        Assert.assertTrue(homePage.isHomePageDisplayed());
    }
}