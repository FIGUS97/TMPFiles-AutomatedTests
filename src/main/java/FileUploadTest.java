
import Pages.UploadPage;
import Util.DriverManager;
import Util.LoggerClass;
import Util.TestFile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class FileUploadTest {

    private UploadPage uploadPage;
    private TestFile testFile;

    @Parameters({"fileSize"})
    @BeforeTest
    public void setUpTest(int fileSize) {

        uploadPage = new UploadPage("chrome");

        LoggerClass.logInfo("\nFile Upload test beginning.");
        LoggerClass.logInfo("File Size: " + fileSize + " MB.");
        try {
             testFile = new TestFile(fileSize);
        } catch (Exception e) {
            LoggerClass.logError("File creation failed.\n" + e.getMessage() + "\n" + e.getCause());
            e.printStackTrace();
        }
    }

    @Parameters({"downloadNumber", "timeLimit"})
    @Test
    public void singleUpload(String downloadNumber, String timeLimit, ITestContext context) throws InterruptedException {
        ISuite iSuiteContext = context.getSuite();
        uploadPage.openUploadPage();

        try {
            uploadPage.fileChoose(testFile.getFile().getCanonicalPath());
        } catch (IOException exc) {
            LoggerClass.logError("[ SingleUpload ] Wrong filepath!");
            Assert.fail();
            return;
        }
        uploadPage.setDownloadLimit(downloadNumber);
        uploadPage.setTimeLimitInMinutes(timeLimit);
        testFile.setTimeLimitInMinutes(Integer.parseInt(timeLimit));

        Thread.sleep(5000);
        uploadPage.clickUploadButton();
        System.out.println("MD5: " + testFile.getMd5sum());

        iSuiteContext.setAttribute("md5", testFile.getMd5sum());
        iSuiteContext.setAttribute("providedAddress", DriverManager.getDriver().getCurrentUrl());

        System.out.println("DL page from upload: " + DriverManager.getDriver().getCurrentUrl());
    }

    @Test
    public void uploadAboveMbLimit() {
        uploadPage.openUploadPage();

        try {
            uploadPage.fileChoose(testFile.getFile().getCanonicalPath());
        } catch (IOException exc) {
            LoggerClass.logError("[ SingleUpload ] Wrong filepath!");
            Assert.fail();
            return;
        }

        uploadPage.clickUploadButton();

        // To Be Finished
        
    }

    @Parameters({"connectedTests"})
    @AfterTest
    public void cleanAfterTest(Boolean connectedTests, ITestContext context) {
        ISuite iSuiteContext = context.getSuite();
        if(connectedTests) {
            iSuiteContext.setAttribute("dummyFilePath", testFile.getFile().getPath());
            System.out.println("File 1 path: " + iSuiteContext.getAttribute("dummyFilePath"));
        } else {
            DriverManager.closeDriver();
            testFile.getFile().delete();
        }

    }
}
