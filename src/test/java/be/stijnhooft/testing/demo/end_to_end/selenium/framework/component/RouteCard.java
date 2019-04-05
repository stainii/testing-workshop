package be.stijnhooft.testing.demo.end_to_end.selenium.framework.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RouteCard {

    private final WebElement element;

    public RouteCard(WebElement element) {
        this.element = element;
    }


    public String getDepartureAndArrivalStation() {
        return element.findElement(By.tagName("h4")).getText();
    }

    public String getDepartureTime() {
        return element.findElement(By.className("departure-time")).getText();
    }


    public String getCurrentDelay() {
        return element.findElement(By.className("current-delay")).getText();
    }

    public String getAverageDelay() {
        return element.findElement(By.className("average-delay")).getText();
    }

    public WebElement getCheckAgainButton() {
        return element.findElement(By.className("check-again"));
    }
}
