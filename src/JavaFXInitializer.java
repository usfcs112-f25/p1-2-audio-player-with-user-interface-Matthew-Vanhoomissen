import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/*
 * Class that initializes JavaFX which is used to read mp3 files
 * @var boolean to see if initialized
 */
public class JavaFXInitializer {
    private static boolean initialize = false;

    /*
     * Initializes the JFXPanel 
     */
    public static void intializing() {
        if(!initialize) {
            new JFXPanel();
            initialize = true;
        }
    }
    /*
     * Quits the JFXPanel when done
     */
    public static void quitting() {
        if(initialize) {
            Platform.exit();
            initialize = true;
        }
    }
}
