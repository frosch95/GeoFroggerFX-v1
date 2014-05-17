import de.geofroggerfx.plugins.Plugin
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.Pane
import org.controlsfx.dialog.Dialog

class Project81Plugin implements Plugin {

    final static DIFFICULTY_TERRAIN_VALUES = ['1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5']

    final String name = "Project81"
    final String version = "0.0.1"
    final CalculateService service = new CalculateService();

    Project81Plugin() {
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
        label.text = "Shows missing difficulty / terrain combination."
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
        service.cacheList = cacheList
        service.sessionContext = sessionContext
        service.restart()
    }

    /**
     * creates the statistic charts to show
     * @param sessionContext context with the cache list in it
     * @return
     */
    private javafx.scene.Node createContent(result) {

        // create javafx chart
        def text = new TextArea();
        result.each { text.appendText("$it.name ($it.difficulty/$it.terrain)\n") }
        text
    }

    private void showDialog(header, content) {
        Dialog dialog = new Dialog(null, name + " (" + version + ")", true)
        dialog.setMasthead(header)
        dialog.setContent(content)
        dialog.show()
    }
}

class CalculateService extends Service {

    def cacheList
    def sessionContext

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected ArrayList call() throws Exception {
                try {
                    def dtFound = [:]
                    for (def difficulty in Project81Plugin.DIFFICULTY_TERRAIN_VALUES) {
                        for (def terrain in Project81Plugin.DIFFICULTY_TERRAIN_VALUES) {
                            dtFound[difficulty + "_" + terrain] = false
                        }
                    }

                    for (def cache in cacheList) {
                        if (cache.found) {
                            dtFound[cache.difficulty + "_" + cache.terrain] = true
                        }
                    }

                    def filteredList = cacheList.findAll {
                        cache -> !(cache.found || dtFound[cache.difficulty + "_" + cache.terrain])
                    }

                    sessionContext.setData("cache-list", filteredList)

                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        };
    }

}