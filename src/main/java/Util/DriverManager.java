package Util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class DriverManager {

    private static WebDriver driver = null;

    public static WebDriver createChromeDriver(){
        System.setProperty("webdriver.chrome.driver", ".\\src\\main\\resources\\BrowserDrivers\\chromedriver.exe");
        if(driver == null)  driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    public static WebDriver createFirefoxDriver(){
        System.setProperty("webdriver.gecko.driver", ".\\src\\main\\resources\\BrowserDrivers\\geckodriver.exe");
        if(driver == null)  driver = new FirefoxDriver();

        return driver;
    }

    public static WebDriver getDriver() {  return driver;  }

    public static void closeDriver() {
        driver.quit();
    }
}
