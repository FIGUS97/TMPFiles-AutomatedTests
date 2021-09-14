import Pages.DownloadPage;
import Util.LoggerClass;
import Util.TestFile;
import org.openqa.selenium.NoSuchElementException;
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
import java.util.concurrent.TimeUnit;

public class FileDownloadTest {

    private DownloadPage downloadPage;
    private TestFile file;
    private String providedMD5;


    @Parameters({"address", "hashMD5", "connectedTests"})
    @BeforeTest
    public void setUpTest(String address, String hashMD5, Boolean connectedTests, ITestContext context) {

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

    @Test (priority = 3)
    public void singleDownload() throws NoSuchElementException{
        downloadPage.openDownloadPage();
        file = new TestFile();

        try {
            file.downloadFile(downloadPage.getDownloadLink());
            Assert.assertTrue(file.testMD5sum(providedMD5));
            System.out.println("Download Test Passed. Hash comparison test passed.");
        } catch (AssertionError err) {
            System.out.println("Download Test Failed. Hash comparison test failed.");
            Assert.fail();
        } catch (NoSuchElementException ex) {

        }
    }

    @Parameters({"downloadNumber"})
    @Test (priority = 2)
    public void multipleDownloadsExactLimit(int downloadNumber) {
        for(int i = 1; i < downloadNumber; i++) {
            System.out.println("\nDownload " + i);
            try {
                singleDownload();
            } catch (NoSuchElementException ex) {
                LoggerClass.logInfo("Download Test Failed. Download Page not available.");
                Assert.fail();
            }
        }
    }

    @Test ( priority = 1 )
    public void downloadBeforeTimeLimit() {
        long minutesFromUpload = TimeUnit.MINUTES.toMinutes(System.currentTimeMillis() - file.getUploadTimestamp());

        if(minutesFromUpload < file.getTimeLimitInMinutes()){
            singleDownload();
        }else{
            LoggerClass.logError("Test Error: Time passed. \nDownload Before Limit test not possible.");
            Assert.fail();
        }
    }

    @Test ( priority = 4 )
    public void downloadAfterTimeLimit() {
        long milisFromUpload = System.currentTimeMillis() - file.getUploadTimestamp();

        try {
            LoggerClass.logInfo("Waiting for reaching time download limit. \nMinutes left: " + TimeUnit.MINUTES.toMinutes(milisFromUpload));
            Thread.sleep(milisFromUpload + 1000);
        } catch (InterruptedException ex) {
            LoggerClass.logError("Error when waiting for a download");
        }

        try {
            singleDownload();
            LoggerClass.logInfo("Page still available after Time limit. Test Failed.");
            Assert.fail();
        } catch (NoSuchElementException exc) {
            LoggerClass.logInfo("Page no longer available. Download after Time limit test passed.");
        }
    }

    @Parameters({"downloadNumber"})
    @Test
    public void downloadsAboveLimit(int downloadNumber) {
        multipleDownloadsExactLimit(downloadNumber + 1);
    }

    @Parameters({"downloadNumber"})
    @Test
    public void downloadUnderLimit(int downloadNumber) {
        multipleDownloadsExactLimit(downloadNumber - 1);
    }

    @Parameters({"connectedTests"})
    @AfterTest
    public void cleanAfterTest(Boolean connectedTests, ITestContext context) {
        downloadPage.closePage();

        try {
            if(connectedTests)
            {
                Files.delete(Paths.get((String) context.getSuite().getAttribute("dummyFilePath")));
            }
            file.getFile().deleteOnExit();
        } catch (NullPointerException | IOException ex) {
            System.out.println("Files to delete not found.");
        }
    }
}
