package testng.over.cucumber.parallelization;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.runtime.*;
import cucumber.runtime.Runtime;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;

import java.util.ArrayList;
import java.util.List;

@CucumberOptions(features = "src/test/resources/testfeatures")
public class DataProvider {

    private Runtime runtime;
    private RuntimeOptions runtimeOptions;
    private ResourceLoader resourceLoader;

    public DataProvider() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        resourceLoader = new MultiLoader(classLoader);
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(this.getClass());
        runtimeOptions = runtimeOptionsFactory.create();
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    @org.testng.annotations.DataProvider(parallel = true)
    public Object[][] scenarios() {
        return new TestNGCucumberRunner(this.getClass()).provideScenarios();
    }

    @org.testng.annotations.DataProvider(parallel = true)
    public Object[][] features() {
        try {
            List<CucumberFeature> features = getFeatures();
            List<Object[]> featuresList = new ArrayList<Object[]>(features.size());
            for (CucumberFeature feature : features) {
                featuresList.add(new Object[]{feature});
            }
            return featuresList.toArray(new Object[][]{});
        } catch (CucumberException e) {
            return new Object[][]{new Object[]{null}};
        }
    }

    private List<CucumberFeature> getFeatures() {
        return runtimeOptions.cucumberFeatures(resourceLoader, runtime.getEventBus());
    }
}
