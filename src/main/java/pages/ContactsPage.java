package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ContactsPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ContactsPage.class);

    public ContactsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[contains(text(), 'No Contacts here!')]")
    WebElement noContactsMsg;

    @FindBy(xpath = "//div[@class='contact-item_card__2SOIM']")
    List<WebElement> contactsCards;

    @FindBy(xpath = "//button[text()='Edit']")
    WebElement buttonEditContact;

    @FindBy(xpath = "//button[text()='Remove']")
    WebElement buttonDeleteContact;

    public boolean isContactsPageDisplayed() {
        return isElementPresent(noContactsMsg) || (contactsCards
                != null && !contactsCards.isEmpty());
    }

    public int getContactsListSize() {
        return contactsCards.size();
    }

    public void openContactCardByName(String name) {
        for (WebElement card : contactsCards) {
            WebElement nameElement = card.findElement(By.xpath(".//h2"));
            if (nameElement.getText().equals(name)) {
                wait.until(ExpectedConditions.elementToBeClickable(card)).click();
                return;
            }
        }
        throw new NoSuchElementException("Contact card with name " + name + " not found");
    }

    public void clickEditButton() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonEditContact)).click();
    }

    public void clickDeleteButton() {
        wait.until(ExpectedConditions.elementToBeClickable(buttonDeleteContact)).click();
    }

    public void deleteContactByName(String name) {
        openContactCardByName(name);
        clickDeleteButton();
    }

    public boolean deleteContactByInvalidName(String name) {
        List<WebElement> contacts = getContactElements();
        boolean found = contacts.stream().anyMatch(contact -> {
            String contactName = contact.findElement(By.tagName("h2")).getText();
            return contactName.equals(name);
        });

        if (!found) {
            logger.warn("Contact with name '{}' was not found", name);
            return false;
        }

        logger.info("Contact with name '{}' found. Proceeding to delete", name);
        openContactCardByName(name);
        clickDeleteButton();
        logger.info("🗑️ Contact with name '{}' was successfully deleted", name);
        return true;
    }

    public List<WebElement> getContactElements() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfAllElementsLocatedBy
                            (By.xpath("//div[@class='contact-item_card__2SOIM']")),
                    ExpectedConditions.presenceOfElementLocated
                            (By.xpath("//h1[contains(text(), 'No Contacts here!')]"))
            ));
        } catch (Exception e) {
            logger.warn("No contacts or header not found within timeout: " + e.getMessage());
        }
        return driver.findElements(By.xpath("//div[@class='contact-item_card__2SOIM']"));
    }

    public void waitContactIsUpdated(String oldName, String newName) {
        wait.until(driver -> {
            List<WebElement> list = getContactElements();
            return !isContactPresentInList(oldName, list) &&
                    isContactPresentInList(newName, list);
        });
    }

    public boolean isContactPresentInList(String name, List<WebElement> contacts) {
        for (WebElement contact : contacts) {
            String contactName = contact.findElement(By.tagName("h2")).getText();
            if (contactName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContactAbsentByName(String name, List<WebElement> contacts) {
        return !isContactPresentInList(name, contacts);
    }
}
