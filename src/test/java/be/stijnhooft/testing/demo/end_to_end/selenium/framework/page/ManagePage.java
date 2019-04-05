package be.stijnhooft.testing.demo.end_to_end.selenium.framework.page;

import be.stijnhooft.testing.demo.end_to_end.selenium.framework.Browser;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.component.RouteListItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class ManagePage {

    private static final String CLASS_NAME_OF_LOADING = "loading";
    private static final String CLASS_NAME_OF_CREATE_BUTTON = "create";
    private static final String CLASS_NAME_OF_BACK_BUTTON = "back";

    private final WebDriver driver;
    private final Browser browser;

    ManagePage(WebDriver driver, Browser browser) {
        this.driver = driver;
        this.browser = browser;
    }

    public void waitUntilRoutesHaveLoaded() {
        new WebDriverWait(driver, 1); // wait a second that loading at least has the chance to pop up, before continuing
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.className(CLASS_NAME_OF_LOADING)));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tbody")));
        new WebDriverWait(driver, 1); // wait a second, just to be sure
    }

    public List<RouteListItem> getRoutes() {
        return driver.findElements(By.cssSelector("tbody tr"))
                .stream()
                .filter(tr -> tr.findElements(By.tagName("input")).isEmpty()) // we don't want the last row, containing inputs to create a new route
                .map(tr -> new RouteListItem(tr))
                .collect(Collectors.toList());
    }

    public void addRoute(String departureStation, String arrivalStation, String departureTime) {
        driver.findElement(By.id("newDepartureStation")).sendKeys(departureStation);
        driver.findElement(By.id("newArrivalStation")).sendKeys(arrivalStation);
        driver.findElement(By.id("newDepartureTime")).sendKeys(departureTime);
        driver.findElement(By.className(CLASS_NAME_OF_CREATE_BUTTON)).click();
    }

    public HomePage clickOnBackButton() {
        driver.findElement(By.className(CLASS_NAME_OF_BACK_BUTTON)).click();
        browser.waitForPageToLoad();
        return new HomePage(driver, browser);
    }
}
