package stepdefinitions;

import java.net.URI;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import testRunner.WebTestBase;

public class Hooks extends WebTestBase {
	public static Scenario sceanrio;
	public static String scenarioName;
	public static String scenarioPath;

	@Before
	public void beforeScenario(Scenario scenario) throws Throwable {
		
		Hooks.sceanrio = scenario;
		scenarioPath = scenario.getUri().toString();
		scenarioName = scenario.getName().toString();
		scenarioName = scenarioPath.concat(scenarioName);
		System.out.println("Started execution for sceanrio " + scenarioName);

	}
	
	@After
	public void afterScenario(Scenario scenario) throws Throwable {
		
	}

}
