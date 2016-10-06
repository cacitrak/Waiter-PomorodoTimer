import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaiterApp extends Application {
	
	private int DEFAULT_HEIGHT = 640;
	private int DEFAULT_WIDTH = 360;
	private int currentStep = 0;
	
	ArrayList<Rectangle> loadingBars = new ArrayList<>();
	
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
		
		Text text = new Text("Start");
		text.setFont(Font.font("Verdana", 20));
		text.setX(xPos);
		text.setY(yPos + height/2);
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("** Start button clicked ***");
			}
		});
		
		text.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("** Start Text clicked ***");
			}
		});
		
		group.getChildren().addAll(start, text);
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
			
			Text text;
			
			Rectangle lBar = new Rectangle(0, height);
			lBar.setX(xPos);
			lBar.setY(yPos);
			lBar.setOpacity(.5);

			if(i % 2 == 0) { // Work timer bar
				bar.setStroke(Color.DARKBLUE);
				bar.setFill(Color.ALICEBLUE);

				lBar.setStroke(Color.DARKBLUE);
				lBar.setFill(Color.PALETURQUOISE);

				text = new Text("Work");
				text.setFont(Font.font("Verdana", 20));
				text.setOpacity(.8);
				text.setX(xPos + width/2);
				text.setY(yPos + height);

				yPos += height;
			}
			else { //Break timer bar
				bar.setStroke(Color.DARKBLUE);
				bar.setFill(Color.LAVENDER);
				
				lBar.setStroke(Color.DARKBLUE);
				lBar.setFill(Color.LIGHTSKYBLUE);
				
				text = new Text("Break");
				text.setFont(Font.font("Verdana", 20));
				text.setOpacity(.8);
				text.setX(xPos + width/2);
				text.setY(yPos + height);
				
				yPos = yPos + height + verticalOffset;
			}
			
			loadingBars.add(lBar);
			group.getChildren().addAll(bar, text);
			group.getChildren().add(lBar);
		}
	}
}
