
import Pages.UploadPage;
import Util.DriverManager;
import Util.TestFile;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.logging.Level;

public class FileUploadTest {

    private UploadPage uploadPage;
    private TestFile testFile;

    @Parameters({"fileSize"})
    @BeforeTest
    public void setUpTest(int fileSize) {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        System.out.println("File Size: " + fileSize + " MB.");

        uploadPage = new UploadPage("chrome");
        try {
             testFile = new TestFile(fileSize);
        } catch (Exception e) {
            System.out.println("File creation failed.\n" + e.getMessage() + "\n" + e.getCause());
            e.printStackTrace();
        }
    }

    @Test
    public void singleUpload(ITestContext context) throws IOException {
        uploadPage.openUploadPage();
        uploadPage.fileChoose(testFile.getFile().getCanonicalPath());
        uploadPage.clickUploadButton();
        System.out.println("MD5: " + testFile.getMd5sum());
        ISuite iSuiteContext = context.getSuite();
        iSuiteContext.setAttribute("md5", testFile.getMd5sum());
        iSuiteContext.setAttribute("providedAddress", DriverManager.getDriver().getCurrentUrl());
        System.out.println("DL page from upload: " + DriverManager.getDriver().getCurrentUrl());
        //uploadPage.closePage();
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
