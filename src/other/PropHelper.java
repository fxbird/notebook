package other;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropHelper {
    private static PropertiesConfiguration propConfig;
    public static final String CONFIG_PATH = "config.properties";

    public static PropertiesConfiguration getPropConfigInstance() {
        try {
            propConfig = new PropertiesConfiguration(CONFIG_PATH);
        } catch (ConfigurationException e) {
            return null;
        }

        return propConfig;

    }


}
