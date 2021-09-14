package Pages;

import Util.DriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class DownloadPage {
    private final WebDriver driver;
    private String address;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/p[1]/a[1]")
    private static WebElement downloadButton;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/table[1]/tbody[1]/tr[1]/td[1]")
    private static WebElement fileNameLabel;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/table[1]/tbody[1]/tr[2]/td[1]")
    private static WebElement sizeLabel;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/table[1]/tbody[1]/tr[3]/td[1]/a[1]")
    private static WebElement urlLabel;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/table[1]/tbody[1]/tr[4]/td[1]")
    private static WebElement timeToDeleteLabel;

    public DownloadPage(String browser) {

        switch (browser) {
            case "chrome" -> this.driver = DriverManager.createChromeDriver();
            case "firefox" -> this.driver = DriverManager.createFirefoxDriver();
            default -> this.driver = DriverManager.createChromeDriver();
        }
        PageFactory.initElements(driver, this);
    }

    public String getDownloadLink() throws NoSuchElementException {
        return downloadButton.getAttribute("href");
    }

    public String getFileName() {
        return fileNameLabel.getText();
    }

    public String getFileSize() {
        return sizeLabel.getText();
    }

    public String getURL () { return urlLabel.getText();  }

    public String getTimeToDelete() {
        return timeToDeleteLabel.getText();
    }

    public void openDownloadPage() {
        driver.get(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {  return address; }

    public void closePage() {  driver.close(); }
}
