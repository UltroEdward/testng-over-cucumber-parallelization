package testng.over.cucumber.parallelization;

import cucumber.api.testng.*;
import cucumber.runtime.model.CucumberFeature;
import org.testng.annotations.Factory;
import testng.over.cucumber.parallelization.runners.FeatureRunnerForFactory;
import testng.over.cucumber.parallelization.runners.ScenarioRunnerForFactory;


public class TestsFactory {

    @Factory(dataProvider = "features", dataProviderClass = DataProvider.class)
    public Object[] factoryFeature(CucumberFeature feature) {
        return new Object[]{new FeatureRunnerForFactory(feature)};
    }

    @Factory(dataProvider = "scenarios", dataProviderClass = DataProvider.class)
    public Object[] factoryScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) {
        return new Object[]{new ScenarioRunnerForFactory(pickleWrapper, featureWrapper)};
    }

}
