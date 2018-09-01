package testng.over.cucumber.parallelization;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


@CucumberOptions(glue = {"main.app.steps"})
public class ScenarioRunnerForFactory {

    private TestNGCucumberRunner testNGCucumberRunner;

    private PickleEventWrapper pickleWrapper;
    private CucumberFeatureWrapper featureWrapper;

    private Runtime runtime;
    private RuntimeOptions runtimeOptions;
    private ResourceLoader resourceLoader;


    ScenarioRunnerForFactory(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) {
        this.pickleWrapper = pickleWrapper;
        this.featureWrapper = featureWrapper;

        ClassLoader classLoader = this.getClass().getClassLoader();
        resourceLoader = new MultiLoader(classLoader);
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(this.getClass());
        runtimeOptions = runtimeOptionsFactory.create();
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "local")
    public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
    }

    @org.testng.annotations.DataProvider(parallel = true)
    public Object[][] local() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[]{pickleWrapper, featureWrapper});
        return list.toArray(new Object[][]{});
    }


    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }

}
