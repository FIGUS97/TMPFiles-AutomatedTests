package Pages;

import Util.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UploadPage {
    private WebDriver driver;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/form[1]/div[1]/input[1]")
    WebElement fileInput;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/form[1]/div[2]/input[1]")
    WebElement maxViewsInput;

    @FindBy(xpath="/html[1]/body[1]/div[1]/section[1]/form[1]/div[3]/input[1]")
    WebElement maxTimeInput;

    @FindBy(name="upload")
    WebElement uploadButton;

    public UploadPage(String browser) {
        switch (browser) {
            case "chrome" -> this.driver = DriverManager.createChromeDriver();
            case "firefox" -> this.driver = DriverManager.createFirefoxDriver();
            default -> this.driver = DriverManager.createChromeDriver();
        }
        PageFactory.initElements(driver, this);
    }

    public void openUploadPage() {
        driver.get("https://tmpfiles.org/");
    }
    
    public void fileChoose(String filePath){
        fileInput.sendKeys(filePath);
    }

    public void setDownloadLimit(String downloadLimit){
        maxViewsInput.clear();
        maxViewsInput.sendKeys(downloadLimit);
    }

    public void setTimeLimitInMinutes(String timeLimit){
        maxTimeInput.clear();
        maxTimeInput.sendKeys(timeLimit);
    }

    public void clickUploadButton() {
        uploadButton.click();
    }

    public void closePage() {
        driver.close();
    }
}
