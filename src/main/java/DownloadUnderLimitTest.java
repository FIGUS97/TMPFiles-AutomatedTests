import Pages.DownloadPage;
import Util.DriverManager;
import Util.TestFile;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DownloadUnderLimitTest {
    private DownloadPage downloadPage;
    private TestFile file;
    private String providedMD5;

    @Parameters({"address", "hashMD5", "connectedTests", "browser"})
    @BeforeTest
    public void setUpTest(String address, String hashMD5, Boolean connectedTests, String browser) {

        downloadPage = new DownloadPage("chrome");


    }

    @Test
    public void runTest() {

    }

    @AfterTest
    public void cleanAfterTest() {

    }
}
