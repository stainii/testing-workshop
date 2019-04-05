package be.stijnhooft.testing.demo.end_to_end.selenium;

import be.stijnhooft.testing.demo.end_to_end.selenium.framework.Browser;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.component.RouteCard;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.component.RouteListItem;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.page.HomePage;
import be.stijnhooft.testing.demo.end_to_end.selenium.framework.page.ManagePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.Assert.*;

/* Getting java.lang.IllegalStateException: The path to the driver executable must be set by the webdriver.chrome.driver system property?
    1. Download the driver at https://sites.google.com/a/chromium.org/chromedriver/downloads
    2. Place in a folder
    3. Add that folder to your system path
 */
// BEFORE RUNNING THESE TESTS, A WORKING APPLICATION MUST RUN AT HTTP://LOCALHOST:8080
public class RoutesSeleniumE2E {

    private Browser browser;

    @Before
    public void setUp() {
        WebDriver driver = new ChromeDriver();
        browser = new Browser(driver);
    }

    @Test
    public void viewAllRoutesAndTheirStatus() {
        HomePage homePage = browser.openTrainDelayWatcher();
        assertEquals("Train delay watcher", browser.getTitle());

        homePage.waitUntilRoutesHaveLoaded();

        List<RouteCard> routes = homePage.getRoutes();
        assertTrue(routes.size() > 0);

        for (RouteCard route: routes) {
            assertNotNull(route.getDepartureAndArrivalStation());
            assertNotNull(route.getDepartureTime());
            assertNotNull(route.getCurrentDelay());
            assertNotNull(route.getAverageDelay());
            assertNotNull(route.getCheckAgainButton());
        }
    }

    @Test
    public void addRoute() {
        HomePage homePage = browser.openTrainDelayWatcher();
        final int numberOfRoutesOnBeforeAdding = homePage.getRoutes().size();

        // go to the manage page
        ManagePage managePage = homePage.clickOnManageButton();
        managePage.waitUntilRoutesHaveLoaded();

        // add a route and check if it has been added
        assertEquals(numberOfRoutesOnBeforeAdding, managePage.getRoutes().size());
        managePage.addRoute("Aalst", "Gent-Sint-Pieters", "12:15");
        managePage.waitUntilRoutesHaveLoaded();

        List<RouteListItem> routesOnManagePage = managePage.getRoutes();
        assertEquals(numberOfRoutesOnBeforeAdding + 1, routesOnManagePage.size());
        RouteListItem newRouteOnManagePage = routesOnManagePage.get(routesOnManagePage.size() - 1);
        assertEquals("Aalst", newRouteOnManagePage.getDepartureStation());
        assertEquals("Gent-Sint-Pieters", newRouteOnManagePage.getArrivalStation());
        assertEquals("12:15", newRouteOnManagePage.getDepartureTime());

        // go to the home page and check if the new route has been added here too
        homePage = managePage.clickOnBackButton();
        homePage.waitUntilRoutesHaveLoaded();
        List<RouteCard> routesOnHomePage = homePage.getRoutes();
        final int numberOfRoutesAfterAdding = routesOnHomePage.size();

        assertEquals(numberOfRoutesOnBeforeAdding + 1, numberOfRoutesAfterAdding);

        RouteCard newRouteOnHomePage = routesOnHomePage.get(routesOnHomePage.size() - 1);
        assertEquals("Aalst ▶ Gent-Sint-Pieters", newRouteOnHomePage.getDepartureAndArrivalStation());
        assertEquals("12:15", newRouteOnHomePage.getDepartureTime());
        assertNotNull(newRouteOnHomePage.getCurrentDelay());
        assertNotNull(newRouteOnHomePage.getAverageDelay());
        assertNotNull(newRouteOnHomePage.getCheckAgainButton());
    }

/*
    @Test
    public void deleteRoute() {
        // try it out yourself! :)
    }
*/

    @After
    public void cleanUp() {
        browser.close();
    }
}
