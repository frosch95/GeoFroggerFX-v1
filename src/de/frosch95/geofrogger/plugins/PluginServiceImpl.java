package de.frosch95.geofrogger.plugins;

import de.frosch95.geofrogger.application.ServiceManager;
import de.frosch95.geofrogger.application.SessionContext;
import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: class description
 *
 * @author abi
 */
public class PluginServiceImpl implements PluginService {

  private final GroovyClassLoader gcl = new GroovyClassLoader();
  private final ServiceManager serviceManager = ServiceManager.getInstance();


  @Override
  public List<Plugin> getAllPlugins() {

    List<Plugin> plugins = new ArrayList<>();

    try {
      File file = new File("./plugins");
      if (!file.exists()) {
        throw new IllegalArgumentException("plugins folder does not exit");
      }

      File[] pluginFiles = file.listFiles((dir, name) -> name.endsWith("Plugin.groovy"));
      for (File pluginFile: pluginFiles) {
        Class clazz = gcl.parseClass(pluginFile);
        for (Class interf: clazz.getInterfaces()) {
          if (interf.equals(Plugin.class)) {
            plugins.add((Plugin)clazz.newInstance());
            break;
          }
        }
      }

    } catch (IOException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return plugins;
  }

  @Override
  public void executePlugin(final Plugin plugin) {
    Map<String, Object> context = new HashMap<>();
    context.put("sessionContext", SessionContext.getInstance());
    context.put("cacheService", serviceManager.getCacheService());
    plugin.run(context);
  }
}
