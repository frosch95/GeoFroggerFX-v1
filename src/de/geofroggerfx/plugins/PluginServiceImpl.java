package de.geofroggerfx.plugins;

import de.geofroggerfx.application.SessionContext;
import de.geofroggerfx.service.CacheService;
import groovy.lang.GroovyClassLoader;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service find, load and executes plugins based on the plugin interface.
 *
 * @author abi
 */
public class PluginServiceImpl implements PluginService {

  private final GroovyClassLoader gcl = new GroovyClassLoader();

  @Inject
  private CacheService cacheService;

  @Inject
  private SessionContext sessionContext;


  @Override
  public List<Plugin> getAllPlugins() {

    List<Plugin> plugins = new ArrayList<>();

    try {
      File file = new File("./plugins");
      if (!file.exists()) {
        throw new IllegalArgumentException("plugins folder does not exist");
      }

      File[] pluginFiles = file.listFiles((dir, name) -> name.endsWith("Plugin.groovy"));
      for (File pluginFile : pluginFiles) {
        Class clazz = gcl.parseClass(pluginFile);
        for (Class interf : clazz.getInterfaces()) {
          if (interf.equals(Plugin.class)) {
            plugins.add((Plugin) clazz.newInstance());
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
    context.put("sessionContext", sessionContext);
    context.put("cacheService", cacheService);
    plugin.run(context);
  }
}
