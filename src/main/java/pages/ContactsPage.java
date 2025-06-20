package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ContactsPage extends BasePage {

    public ContactsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[contains(text(), 'No Contacts here!')]")
    WebElement noContactsMsg;

    @FindBy(xpath = "//div[@class='contact-item_card__2SOIM']")
    List<WebElement> contactsCards;

    @FindBy(xpath = "//button[contains(text(), 'Edit')]")
    WebElement buttonEditContact;

    @FindBy(xpath = "//button[contains(text(), 'Remove')]")
    WebElement buttonDeleteContact;


    public boolean isContactsPageDisplayed() {
        return isElementPresent(noContactsMsg) || (contactsCards != null && !contactsCards.isEmpty());
    }

    public int getContactsListSize() {
        return contactsCards.size();
    }

    public void deleteFirstContact() {
        if (!contactsCards.isEmpty()) {
            contactsCards.get(0).click();
            buttonDeleteContact.click();
        }
    }
}