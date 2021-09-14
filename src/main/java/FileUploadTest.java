
import Pages.UploadPage;
import Util.DriverManager;
import Util.LoggerClass;
import Util.TestFile;
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

        System.out.println("\nFile Download test beginning.");
        System.out.println("File Size: " + fileSize + " MB.");
        try {
             testFile = new TestFile(fileSize);
        } catch (Exception e) {
            System.out.println("File creation failed.\n" + e.getMessage() + "\n" + e.getCause());
            e.printStackTrace();
        }
    }

    @Parameters({"downloadNumber", "timeLimit"})
    @Test
    public void singleUpload(String downloadNumber, String timeLimit, ITestContext context) {
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
        
    }

    @Parameters({"connectedTests"})
    @AfterTest
    public void cleanAfterTest(Boolean connectedTests, ITestContext context) {
        ISuite iSuiteContext = context.getSuite();
        if(connectedTests) {
            iSuiteContext.setAttribute("dummyFilePath", testFile.getFile().getPath());
            System.out.println("File 1 path: " + iSuiteContext.getAttribute("dummyFilePath"));
        }
        else testFile.getFile().delete();
    }
}
