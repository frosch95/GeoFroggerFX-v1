import de.frosch95.geofrogger.plugins.Plugin
import javafx.collections.FXCollections
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.chart.PieChart
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.controlsfx.dialog.Dialog

import static javafx.scene.chart.PieChart.Data

class OwnStatisticsPlugin implements Plugin {

  private difficultyTerrainValues = ['1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5']

  final String name = "Own Statistic"
  final String version = "0.0.1"
  final CalculateService service = new CalculateService();

  OwnStatisticsPlugin() {
    service.onSucceeded = {
      showDialog(createHeader(), createContent(it.source.value))
    } as EventHandler<WorkerStateEvent>;
  }

  @Override
  void run(final Map context) {
    calculateStats(context.sessionContext)
  }

  private javafx.scene.Node createHeader() {
    def pane = new Pane()
    def Label label = new Label()
    label.text = "This example plugin shows some stats based on the current list.\nThese statistics are found statistics, based on all found caches in the list."
    pane.children.add(label)
    pane
  }

/**
 * creates the statistic charts to show
 * @param sessionContext context with the cache list in it
 * @return
 */
  private void calculateStats(sessionContext) {
    // get the cache list out of the context
    def cacheList = sessionContext.getData("cache-list")
    service.cacheList = cacheList;
    service.restart();
  }

  /**
   * creates the statistic charts to show
   * @param sessionContext context with the cache list in it
   * @return
   */
  private javafx.scene.Node createContent(result) {

    // create a vbox as layout container
    VBox contenPane = new VBox()
    contenPane.prefWidth = 600
    VBox.setVgrow(contenPane, Priority.ALWAYS);

    // create javafx chart
    def typeData = FXCollections.observableArrayList()
    result.typeStats.each() { key, value -> typeData.add(new Data(key.toString() + ' (' + value + ')', value as double)) }
    def typeChart = new PieChart(typeData);
    typeChart.setTitle("Spreading of cache types found (${result.foundCount}).");

    // create javafx chart
    def difficultyData = FXCollections.observableArrayList()
    difficultyTerrainValues.each {
      def value = result.difficultyStats[it]
      if (value) difficultyData.add(new Data(it+ ' (' + value + ')', value))
    }
    def difficultyChart = new PieChart(difficultyData);
    difficultyChart.setTitle("Spreading of difficulties found (${result.foundCount}).");

    // create javafx chart
    def terrainData = FXCollections.observableArrayList()
    difficultyTerrainValues.each {
      def value = result.terrainStats[it]
      if (value) terrainData.add(new Data(it+ ' (' + value + ')', value))
    }
    def terrainChart = new PieChart(terrainData);
    terrainChart.setTitle("Spreeading of terrain found (${result.foundCount}).");

    // add charts to layout container
    contenPane.children.addAll(typeChart, difficultyChart, terrainChart)

    // return the layout container
    def scrollPane = new ScrollPane(contenPane)
    scrollPane.minWidth = 640
    scrollPane.minHeight = 450
    scrollPane
  }

  private void showDialog(header, content) {
    Dialog dialog = new Dialog(null, name+" ("+version+")", true)
    dialog.setMasthead(header)
    dialog.setContent(content)
    dialog.show()
  }
}

class CalculateService extends Service {

  def cacheList

  @Override
  protected Task createTask() {
    return new Task() {
      @Override
      protected LinkedHashMap call() throws Exception {

        def typeStats = [:]
        def difficultyStats = [:]
        def terrainStats = [:]

        def foundCount = 0

        for (def cache in cacheList) {
          if (cache.found) {
            foundCount++
            incrementStats(typeStats, cache.type)
            incrementStats(difficultyStats, cache.difficulty)
            incrementStats(terrainStats, cache.terrain)
          }
        }
        ['foundCount': foundCount, 'typeStats': typeStats, 'difficultyStats': difficultyStats, 'terrainStats': terrainStats]
      }
    };
  }

  private void incrementStats(map, key) {
    map[key] = map[key] ? map[key] + 1 : 1
  }

}