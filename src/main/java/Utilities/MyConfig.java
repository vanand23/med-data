package Utilities;

import org.skife.config.Config;
import org.skife.config.Default;

public interface MyConfig {
    @Config("Researcher Name")
    @Default("Default")
    String getResearcherName();

    @Config("Trial Number")
    @Default("0")
    int getTrialNumber();

    @Config("Sample Number")
    @Default("0")
    String getSampleNumber();

}
