/*
 *
 *   Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.am.integration.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.core.BrowserManager;
import org.wso2.carbon.automation.core.ProductConstant;

public class APIMANAGER2604ApplicationPaginationTestCase extends AMIntegrationUiTestBase {

    private WebDriver driver;
    private final String TEST_DATA_USERNAME = "admin";
    private final String TEST_DATA_PASSWORD = "admin";

    private static final Log log = LogFactory.getLog(APIMANAGER2604ApplicationPaginationTestCase.class);

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init();
        driver = BrowserManager.getWebDriver();
    }

    @Test(groups = "wso2.am", description = "verify pagination in application creation page")
    public void appCreationPaginationTest() throws Exception {
        driver.get(getStoreURL(ProductConstant.AM_SERVER_NAME));

        log.info("Started to Login to Store");
        driver.findElement(By.id("login-link")).click();
        WebElement userNameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));

        userNameField.sendKeys(TEST_DATA_USERNAME);
        passwordField.sendKeys(TEST_DATA_PASSWORD);
        driver.findElement(By.id("loginBtn")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //check the presence of admin name in store home page to verify the user has logged to store.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(TEST_DATA_USERNAME)));
        log.info("Logging to store is successful");

        log.info("browsing the application adding page");
        //browsing the application adding page.
        driver.get(getStoreURL(ProductConstant.AM_SERVER_NAME) + "/site/pages/applications.jag?tenant=carbon.super");

        for (int i = 0; i <= 20; i++) {
            createApplication("testApp" + i);
        }

        //check the presence of "page=1" hyperlink in application adding page that is related to pagination will ensure
        // pagination will work properly
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='page=1']")));
        log.info("pagination works correctly.user can create more than 20 applications");

    }

    /*
       create application for the given application name
     */
    private void createApplication(String appName) {
        WebElement name = driver.findElement(By.id("application-name"));
        name.sendKeys(appName);
        WebElement tier = driver.findElement(By.id("appTier"));
        tier.sendKeys("Gold");
        WebElement callBackUrl = driver.findElement(By.id("callback-url"));
        callBackUrl.sendKeys("");
        WebElement description = driver.findElement(By.id("description"));
        description.sendKeys("this-is-test");

        driver.findElement(By.id("application-add-button")).click();
        log.info("added application " + appName + " to store");

        driver.navigate().refresh();

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }

}
