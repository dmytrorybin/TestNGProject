package ua.app.utilities;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import ua.app.base.TestBase;

/**
 * Created by Dmytro_Rybin on 11/1/2016.
 */
public class TestListener implements ITestListener {

    static Logger logger = Logger.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        logger.info("Test [" + iTestResult.getMethod().getMethodName() + "] has passed within [" + (iTestResult.getEndMillis() - iTestResult.getStartMillis()) + "]." + "Thread ID: " + Thread.currentThread().getId());
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        logger.error("Test [" + iTestResult.getMethod().getMethodName() + "] has failed within [" + (iTestResult.getEndMillis() - iTestResult.getStartMillis()) + "]." + "Thread ID: " + Thread.currentThread().getId());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        logger.info("Test [" + iTestResult.getMethod().getMethodName() + "] has been skipped within [" + (iTestResult.getEndMillis() - iTestResult.getStartMillis()) + "]." + "Thread ID: " + Thread.currentThread().getId());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("[START]: " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {

        System.out.println("[STOP]: " + iTestContext.getName());
    }
}
