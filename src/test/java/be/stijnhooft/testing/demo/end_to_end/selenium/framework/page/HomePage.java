package be.stijnhooft.testing.demo.end_to_end.selenium.framework.page;

import be.stijnhooft.testing.demo.end_to_end.selenium.framework.Browser;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.component.RouteCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage {

    private static final String CLASS_NAME_OF_LOADING = "loading";
    private static final String CLASS_NAME_OF_ROUTE_CARD = "card";
    private static final String CLASS_NAME_OF_MANAGE_BUTTON = "manage";

    private final WebDriver driver;
    private final Browser browser;

    public HomePage(WebDriver driver, Browser browser) {
        this.driver = driver;
        this.browser = browser;
    }

    public void waitUntilRoutesHaveLoaded() {
        new WebDriverWait(driver, 1); // wait a second that loading at least has the chance to pop up, before continuing
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.className(CLASS_NAME_OF_LOADING)));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("card-deck")));
        new WebDriverWait(driver, 1); // wait a second, just to be sure
    }

    public List<RouteCard> getRoutes() {
        return driver.findElements(By.className(CLASS_NAME_OF_ROUTE_CARD))
        .stream()
        .map(element -> new RouteCard(element))
        .collect(Collectors.toList());
    }

    public ManagePage clickOnManageButton() {
        driver.findElement(By.className(CLASS_NAME_OF_MANAGE_BUTTON)).click();
        browser.waitForPageToLoad();
        return new ManagePage(driver, browser);
    }
}
