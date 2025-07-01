package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features= {"src/test/resources/features/BBC_One.feature"}, glue= {"stepdefinitions"},
                  plugin = {"pretty",
                            "json:target/MyReports/report.json",
                            "junit:target/MyReports/report.xml",
                  }
)

public class CucumberRunnerTests {

}
