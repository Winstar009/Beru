package ru.beru;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
    @Attachment(value = "Page screenshot failure", type = "image/png")
    private byte[] saveScreenshot(byte[] screenshot) {
        return screenshot;
    }

    private void makeScreenShoot(ChromeDriver driver) {
        byte[] srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        saveScreenshot(srcFile);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("onTestFailure");
        Object currentClass = result.getInstance();
        ChromeDriver driver = ((WebDriverInit) currentClass).driver;
        makeScreenShoot(driver);
    }

    @Override
    public void onStart(ITestContext testContext) {
        System.out.println(testContext.getName());
    }
}
