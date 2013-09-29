import de.frosch95.geofrogger.plugins.Plugin
import javafx.collections.FXCollections
import javafx.scene.chart.PieChart
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.controlsfx.dialog.Dialog

import static javafx.scene.chart.PieChart.Data

class BasicListStatisticsPlugin implements Plugin {

  private difficultyTerrainValues = ['1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5']

  final String name = "Basic List Statistic"
  final String version = "0.0.1"

  @Override
  void run(final Map context) {
    showDialog(createHeader(context.sessionContext), createContent(context.sessionContext))
  }

  private javafx.scene.Node createHeader(sessionContext) {
    def pane = new Pane()
    def Label label = new Label()
    label.text = "This example plugin shows some stats based on the current list.\nThese statistics are global statistics over all caches in list\nand not personalized statistics over own founds."
    pane.children.add(label)
    pane
  }

  /**
   * creates the statistic charts to show
   * @param sessionContext context with the cache list in it
   * @return
   */
  private javafx.scene.Node createContent(sessionContext) {

    // get the cache list out of the context
    def cacheList = sessionContext.getData("cache-list")

    // create a vbox as layout container
    VBox contenPane = new VBox()

    // groovy maps for selecting the statistic numbers
    def typeStats = [:]
    def difficultyStats = [:]
    def terrainStats = [:]

    // iterate over all the caches and count the data
    for (def cache in cacheList) {
      incrementStats(typeStats, cache.type)
      incrementStats(difficultyStats, cache.difficulty)
      incrementStats(terrainStats, cache.terrain)
    }

    // create javafx chart
    def typeData = FXCollections.observableArrayList()
    typeStats.each() { key, value -> typeData.add(new Data(key as String, value as double)) }
    def typeChart = new PieChart(typeData);
    typeChart.setTitle("Number of cache types in list.");

    // create javafx chart
    def difficultyData = FXCollections.observableArrayList()
    difficultyTerrainValues.each {
      def value = difficultyStats[it]
      if (value) difficultyData.add(new Data(it, value))
    }
    def difficultyChart = new PieChart(difficultyData);
    difficultyChart.setTitle("Number of difficulties in list.");

    // create javafx chart
    def terrainData = FXCollections.observableArrayList()
    difficultyTerrainValues.each {
      def value = terrainStats[it]
      if (value) terrainData.add(new Data(it, value))
    }
    def terrainChart = new PieChart(terrainData);
    terrainChart.setTitle("Number of terrain in list.");

    // add charts to layout container
    contenPane.children.addAll(typeChart, difficultyChart, terrainChart)

    // return the layout container
    def scrollPane = new ScrollPane(contenPane)
    scrollPane.minWidth = 600
    scrollPane.prefWidth = 600
    scrollPane.maxWidth = 600
    scrollPane.minHeight = 450
    scrollPane
  }

  private void incrementStats(map, key) {
    map[key] = map[key] ? map[key] + 1 : 1
  }

  private void showDialog(header, content) {
    Dialog dialog = new Dialog(null, name+" ("+version+")")
    dialog.setMasthead(header)
    dialog.setContent(content)
    dialog.show()
  }
}