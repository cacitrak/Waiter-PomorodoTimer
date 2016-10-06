import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class WaiterApp extends Application {
	
	private int DEFAULT_HEIGHT = 640;
	private int DEFAULT_WIDTH = 360;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Waiter");
		primaryStage.setHeight(DEFAULT_HEIGHT);
		primaryStage.setWidth(DEFAULT_WIDTH);
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		addTimerBarsToGroup(root);
		addStartButton(root);
		primaryStage.show();
	}
	
	private void addStartButton(Group group) {
		int height = 70;
		int width = 70;
		int xPos = 40;
		int yPos = 40;
		
		Rectangle start = new Rectangle(width, height);
		start.setX(xPos);
		start.setY(yPos);
		start.setFill(Color.MEDIUMSLATEBLUE);
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("** Start button clicked ***");
			}
			
		});
		group.getChildren().add(start);
	}
	
	private void addTimerBarsToGroup(Group group) {
		int height = 30;
		int width = 250;
		
		int verticalOffset = 10;
		int xPos= 20;
		int yPos = 200;
		
		for(int i = 0; i < 8; i++) {
			Rectangle bar = new Rectangle(width, height);
			bar.setX(xPos);
			bar.setY(yPos);
			
			if(i % 2 == 0) { // Work timer bar
				bar.setFill(Color.ALICEBLUE);
				yPos += height;
			}
			else { //Break timer bar
				bar.setFill(Color.LAVENDER);
				yPos = yPos + height + verticalOffset;
			}
			group.getChildren().add(bar);
			
		}
	}

}
