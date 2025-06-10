package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get("https://telranedu.web.app");
    }

    //Before authorization
    @FindBy(xpath = "//a[@href='/home']")
    WebElement homeHeaderLink;

    @FindBy(xpath = "//h1[text()='Home Component']")
    WebElement textHomePage;

    @FindBy(xpath = "//a[@href='/about']")
    WebElement aboutHeaderLink;

    @FindBy(xpath = "//a[@href='/login']")
    WebElement loginHeaderLink;

    //After authorization
    @FindBy(xpath = "//a[@href='/contacts']")
    WebElement contactsHeaderLink;

    @FindBy(xpath = "//a[@href='/add']")
    WebElement addHeaderLink;

    @FindBy(xpath = "//button[contains(text(), 'Sign Out')]")
    WebElement signOutHeaderButton;

    public boolean isHomePageDisplayed() {
        return isElementPresent(textHomePage);
    }

    public void clickHomeHeaderLink() {
        homeHeaderLink.click();
    }

    public void clickAboutHeaderLink() {
        aboutHeaderLink.click();
    }

    public void clickLoginHeaderLink() {
        loginHeaderLink.click();
    }

    public void clickContactsHeaderLink() {
        contactsHeaderLink.click();
    }

    public void clickAddHeaderLink() {
        addHeaderLink.click();
    }

    public void clickSignOutHeaderButton() {
        signOutHeaderButton.click();
    }
}