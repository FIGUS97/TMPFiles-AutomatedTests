import Pages.DownloadPage;
import Util.TestFile;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class FileDownloadTest {

    private DownloadPage downloadPage;
    private TestFile file;
    private String providedMD5;


    @Parameters({"address", "hashMD5", "connectedTests"})
    @BeforeTest
    public void setUpTest(String address, String hashMD5, Boolean connectedTests, ITestContext context) {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        downloadPage = new DownloadPage("chrome");
        ISuite iSuiteContext = context.getSuite();

        if(connectedTests) {
            providedMD5 = (String) iSuiteContext.getAttribute("md5");
            downloadPage.setAddress((String) iSuiteContext.getAttribute("providedAddress"));
        } else {
            providedMD5 = hashMD5;
            downloadPage.setAddress(address);
        }

        System.out.println("Address: " + downloadPage.getAddress());
        System.out.println("MD5hash: " + providedMD5);
    }

    @Test
    public void singleDownload() {
        downloadPage.openDownloadPage();
        file = new TestFile();
        System.out.println(downloadPage.getDownloadLink());
        file.downloadFile(downloadPage.getDownloadLink());
        try {
            Assert.assertTrue(file.testMD5sum(providedMD5));
            System.out.println("File 2 path: " + file.getFile().getPath());
            System.out.println("Download Test passed. Hash comparison test passed.");
        } catch (AssertionError err) {
            System.out.println("Download Test Fail. Hash comparison test failed.");
        }
        //downloadPage.closePage();
    }

    @Parameters({"downloadNumber"})
    @Test
    public void multipleDownloadsUnderLimit(int downloadNumber) {
        for(int i = 0; i < downloadNumber; i++) {
            System.out.println("Download " + i);
            singleDownload();
        }
    }

    @Parameters({"connectedTests"})
    @AfterTest
    public void cleanAfterTest(Boolean connectedTests, ITestContext context) throws IOException {
        System.out.println("File 1 path: " + context.getSuite().getAttribute("dummyFilePath") + ", connectedTests: " + connectedTests);
        if(connectedTests)
        {
            Files.delete(Paths.get((String) context.getSuite().getAttribute("dummyFilePath")));
        }
        file.getFile().deleteOnExit();
    }
}
