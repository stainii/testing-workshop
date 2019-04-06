package be.stijnhooft.testing.demo.integration.bdd.controller;


import be.stijnhooft.testing.demo.TestingDemoApplication;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * In this test, we test the same things like in RouteControllerIT.
 * Only this time, our tests are defined in stories.
 *
 * Check out src/test/resources/stories/routes.story to see what this test does.
 * The code behind the test steps is defined in ./steps/TestSteps.java.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestingDemoApplication.class)
public class RouteStoryIT extends JBehaveBaseRunner {

    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(
                CodeLocations.codeLocationFromClass(this.getClass()),
                "**/routes.story", "**/excluded*.story");
    }
}
