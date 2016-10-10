import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaiterApp extends Application {
	
	private int DEFAULT_HEIGHT = 640;
	private int DEFAULT_WIDTH = 360;
	private int currentStep = 0;
	
	//1500 - work time
	//300 pause time
	//900 long pause
	
	private int workTime =  5; //Seconds
	private int pauseTime = 3;
	private int longPauseTime = 4;
	private Integer timeSeconds = 0;
	
	public Timeline timeline;
	public Text timerText;
	
	Map<Integer, PomodoroPhase> phaseByStep = new HashMap<>();
	ArrayList<Rectangle> loadingBars = new ArrayList<>();
	
	int fullBarWidth = 250;
	
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
		addTimerText(root);
		
		phaseByStep.put(0, PomodoroPhase.WORK);
		phaseByStep.put(1, PomodoroPhase.PAUSE);
		phaseByStep.put(2, PomodoroPhase.WORK);
		phaseByStep.put(3, PomodoroPhase.PAUSE);
		phaseByStep.put(4, PomodoroPhase.WORK);
		phaseByStep.put(5, PomodoroPhase.PAUSE);
		phaseByStep.put(6, PomodoroPhase.WORK);
		phaseByStep.put(7, PomodoroPhase.LONGPAUSE);
		
		primaryStage.show();
	}
	
	private void addTimerText(Group group) {
		timerText = new Text("00:00:00");
		timerText.setX(150);
		timerText.setY(100);
		timerText.setFont(new Font("Verdana", 20));
		
		group.getChildren().add(timerText);
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
				startTimer();
			}
		});
		
		text.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("** Start Text clicked ***");
				startTimer();
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
			
			Rectangle lBar = new Rectangle(25, height);
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
			} else { //Break timer bar
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startTimer() {
		if (timeline != null) timeline.stop();
		else timeline = new Timeline();
		
		timeSeconds = workTime;
		timeline.setCycleCount(workTime);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(1), 
						new EventHandler() {
					@Override
					public void handle(Event event) {
						timeSeconds--;
						timerText.setText(getRemainingTime());
						handleLoadingBar();
						
						if (timeSeconds <= 0) {
							timeline.stop();
							int nextPhase = getNextPhaseTime();
							timeline.setCycleCount(nextPhase);
							timeSeconds = nextPhase;
							timeline.playFromStart();
						}
					}
				}));
		timeline.playFromStart();
	}
	
	public void handleLoadingBar() {
		int currPhaseTime = getCurrentPhaseTime();
		int elapsedTime = currPhaseTime - timeSeconds;
		
		double fractionElapsed = (double)elapsedTime/(double)currPhaseTime;
		System.out.println(currPhaseTime + ", " + elapsedTime + ", " + fractionElapsed);
		
		loadingBars.get(currentStep).setWidth(fullBarWidth * fractionElapsed);
	}
	
	public int getCurrentPhaseTime()
	{
		switch(phaseByStep.get(currentStep)) {
		case WORK:
			return workTime;
		case PAUSE:
			return pauseTime;
		case LONGPAUSE:
			return longPauseTime;
		}
		return 0;
	}
	
	public int getNextPhaseTime() {
		currentStep += 1;
		if(currentStep > 7) { 
			currentStep = 0;
			for(Rectangle r : loadingBars)
				r.setWidth(0);
		}
		
		switch(phaseByStep.get(currentStep)) {
		case WORK:
			return workTime;
		case PAUSE:
			return pauseTime;
		case LONGPAUSE:
			return longPauseTime;
		}
		return 0;
	}
	
	public String getRemainingTime() {
		int tempRem = timeSeconds;
		int hours = timeSeconds / 3600;
		tempRem = tempRem % 3600;
		int minutes = tempRem / 60;
		tempRem = tempRem % 60;
		
		return hours + ":" + minutes + ":" + tempRem;
	}
	
	enum PomodoroPhase {
		WORK,
		PAUSE,
		LONGPAUSE
	}
}
