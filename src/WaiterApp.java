import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.PomodoroPhase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaiterApp extends Application {
	
	public Text timerText;
	
	Map<Integer, PomodoroPhase> phaseByStep = new HashMap<>();
	ArrayList<ProgressBar> loadingBars = new ArrayList<>();
	
	int fullBarWidth = 300;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Font.loadFont(WaiterApp.class.getResource("/fonts/fontawesome.ttf").toExternalForm(), 12);
		primaryStage.setTitle("Waiter");
		
		Pane root = FXMLLoader.load(getClass().getResource("gui/waiterApp.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
