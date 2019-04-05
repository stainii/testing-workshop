package be.stijnhooft.testing.demo.end_to_end.selenium.framework.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RouteListItem {
    private final WebElement row;

    public RouteListItem(WebElement row) {
        this.row = row;
    }

    public String getDepartureStation() {
        return row.findElements(By.tagName("td")).get(0).getText();
    }

    public String getArrivalStation() {
        return row.findElements(By.tagName("td")).get(1).getText();
    }

    public String getDepartureTime() {
        return row.findElements(By.tagName("td")).get(2).getText();
    }
}
