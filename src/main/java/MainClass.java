import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MainClass {
    private static WebDriver driverFirefox, driverChrome;

    public static WebDriver getDriverFirefox() { return driverFirefox; }

    public static WebDriver getDriverChrome() { return driverChrome;  }

    private static WebDriver createChromeDriver(){
        System.setProperty("webdriver.chrome.driver", ".\\src\\main\\resources\\BrowserDrivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    private static WebDriver createFirefoxDriver(){
        System.setProperty("webdriver.gecko.driver", ".\\src\\main\\resources\\BrowserDrivers\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        return driver;
    }

    public static void main(String[] args){
        System.out.println("holle");
    }

}
