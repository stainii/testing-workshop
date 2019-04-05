package be.stijnhooft.testing.demo.end_to_end.selenium.framework;

import be.stijnhooft.testing.demo.end_to_end.selenium.framework.page.HomePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Browser {

    private final WebDriver driver;

    public Browser(WebDriver driver) {
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); // make selenium go a bit slower, so that we won't get "no such element found" exceptions that easily.
    }

    public HomePage openTrainDelayWatcher() {
        goTo("http://localhost:8080");
        return new HomePage(driver, this);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void close() {
        driver.close();
    }

    public void waitForPageToLoad() {
        ExpectedCondition<Boolean> pageLoadCondition =
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    private void goTo(String url) {
        driver.get(url);
    }
}
