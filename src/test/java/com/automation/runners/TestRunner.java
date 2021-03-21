package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/E2ETests" }, glue = { "com.automation.stepDefinitions" }, tags = "")
public class TestRunner extends AbstractTestNGCucumberTests {

}
