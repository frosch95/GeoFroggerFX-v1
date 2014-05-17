package de.geofroggerfx.plugins;

import java.util.Map;

/**
 * This interface defines the method a plugin has to provide to be called correctly
 *
 * @author abi
 */
public interface Plugin {

    /**
     * @return name
     */
    String getName();

    /**
     * @return version
     */
    String getVersion();

    /**
     * Run the main method of the plugin. All the logic is done in this method.
     * Every run method will get a context map, with all the services inside,
     * to use them.
     *
     * @param context services and data
     */
    void run(Map context);

}
