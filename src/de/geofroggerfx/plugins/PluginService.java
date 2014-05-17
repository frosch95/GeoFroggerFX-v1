package de.geofroggerfx.plugins;

import java.util.List;

/**
 * TODO: class description
 *
 * @author abi
 */
public interface PluginService {

    List<Plugin> getAllPlugins();

    void executePlugin(Plugin plugin);


}
