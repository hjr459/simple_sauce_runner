package com.saucelabs;

/**
 * @author Neil Manvar
 */

import org.testng.annotations.Test;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.saucelabs.saucerest.SauceREST;


public class TestNGWikipediaDemo {
    class Ctx {
        public WebDriver driver;
        public String jobId;
        public boolean passed;
    }

    private Random randomGenerator = new Random();
    private SauceREST sauceREST = new SauceREST(TestHelper.username, TestHelper.accessKey);

    private ThreadLocal threadLocal = new ThreadLocal();

    @BeforeMethod
    public void setUp(Method method) throws Exception {
        if(threadLocal.get() == null) threadLocal.set(new Ctx());
        Ctx ctx = (Ctx) threadLocal.get();
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("name", "TestNGWikipediaDemo - " + method.getName());
        ctx.driver = new RemoteWebDriver(
                new URL(TestHelper.onDemandUrl),
                capabilities);
        ctx.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        ctx.jobId = ((RemoteWebDriver) ctx.driver).getSessionId().toString();
        ctx.passed = true;
    }

    @AfterMethod
    public void tearDown() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        if (ctx.passed) {
            sauceREST.jobPassed(ctx.jobId);
        } else {
            sauceREST.jobFailed(ctx.jobId);
        }
        driver.quit();
    }


    @Test
    public void verifyLaunch() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15); // wait for a maximum of 5 seconds
            driver.get("http://staging.partsandservice.kenworth.com/register");
            if (!(driver.findElements(By.id("logo")).size() != 0)) {
                System.out.println("verifyElementPresent failed");
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("log in")));
            driver.findElement(By.linkText("log in")).click();
            if (!driver.findElement(By.tagName("html")).getText().contains("Log In")) {
                System.out.println("verifyTextPresent failed");
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            driver.findElement(By.id("email")).click();
            driver.findElement(By.id("email")).clear();
            driver.findElement(By.id("email")).sendKeys("harry@saucelabs.com");
            driver.findElement(By.id("password")).click();
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys("Saucelabs1!");
            driver.findElement(By.id("login-block-login")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("html")));
            if (!driver.findElement(By.tagName("html")).getText().contains("welcome, Harry | logout My Kenworth Dealer")) {
                System.out.println("verifyTextPresent failed");
            }

        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    public void verifyLaunchBis() throws Exception {
        verifyLaunch();
    }
 }
