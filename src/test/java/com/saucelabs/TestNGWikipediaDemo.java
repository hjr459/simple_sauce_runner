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
            driver.get(" http://staging.partsandservice.kenworth.com/register");

            // Make the browser get the page and check its title
            //Assert.assertEquals("Wikipedia", driver.getTitle());

            // Check if the launch page elements are there
            Assert.assertTrue(driver.findElement(By.id("login")).isDisplayed());
            driver.findElement(By.linkText("log in")).click();
            //Assert.assertTrue(driver.findElement(By.cssSelector("#searchInput")).isDisplayed());
            //Assert.assertTrue(driver.findElement(By.cssSelector("#searchLanguage")).isDisplayed());
            //Assert.assertTrue(driver.findElement(By.cssSelector(".search-form .formBtn")).isDisplayed());
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

