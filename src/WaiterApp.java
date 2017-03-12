import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.PomodoroPhase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WaiterApp extends Application {
	
	private int DEFAULT_HEIGHT = 450;
	private int DEFAULT_WIDTH = 380;
	private int currentStep = 0;
	
	/** To see the progress bars step, decrease the 3 time variables below */
	private int workTime =  1500; //Seconds
	private int pauseTime = 300;
	private int longPauseTime = 900;
	private Integer timeSeconds = 0;
	
	public Timeline timeline;
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
		/*
		primaryStage.setHeight(DEFAULT_HEIGHT);
		primaryStage.setWidth(DEFAULT_WIDTH);
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		Rectangle bg = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.ALICEBLUE);
		root.getChildren().add(bg);
		
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
		*/
		primaryStage.show();
	}
	
	private void addTimerText(Group group) {
		timerText = new Text("00:00:00");
		timerText.setX(150);
		timerText.setY(60);
		timerText.setFont(new Font("Verdana", 20));
		
		group.getChildren().add(timerText);
	}
	
	private void addStartButton(Group group) {
		int height = 70;
		int width = 70;
		int xPos = 40;
		int yPos = 20;
		
		Circle start = new Circle(70, 50, width/2, Color.LIGHTSTEELBLUE);
		
		Text text = new Text("Start");
		text.setFont(Font.font("Verdana", 20));
		text.setX(xPos);
		text.setY(yPos + height/2);
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				startTimer();
			}
		});
		
		text.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				startTimer();
			}
		});
		
		group.getChildren().addAll(start, text);
	}
	
	private void addTimerBarsToGroup(Group group) {
		int height = 30;
		int width = 300;
		
		int verticalOffset = 10;
		int xPos= 20;
		int yPos = 100;
		
		for(int i = 0; i < 8; i++) {
			
			ProgressBar pb = new ProgressBar(0);
			pb.setLayoutX(xPos);
			pb.setLayoutY(yPos);
			pb.setPrefHeight(height);
			pb.setPrefWidth(width);
			
			Text indic = new Text();
			indic.setFont(Font.font("Comic Sans", 20));
			indic.setX(xPos + width + 5);
			indic.setY(yPos + 21);
			
			if(i % 2 == 0) { // Work timer bar
				indic.setText("W");
				yPos += height;
			} else { //Break timer bar
				indic.setText("P");
				indic.setX(xPos + width + 7);
				yPos = yPos + height + verticalOffset;
			}
			
			loadingBars.add(pb);
			group.getChildren().add(pb);
			group.getChildren().add(indic);
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
		
		loadingBars.get(currentStep).setProgress(fractionElapsed);
	}
	
	public int getCurrentPhaseTime()
	{
		switch(phaseByStep.get(currentStep)) {
		case WORK:		return workTime;
		case PAUSE:	    return pauseTime;
		case LONGPAUSE:	return longPauseTime;
		}
		return 0;
	}
	
	public int getNextPhaseTime() {
		currentStep += 1;
		if(currentStep > 7) { 
			currentStep = 0;
			for(ProgressBar r : loadingBars)
				r.setProgress(0);
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
		String hours = String.format("%02d", timeSeconds / 3600);
		tempRem = tempRem % 3600;
		String minutes = String.format("%02d", tempRem / 60);
		tempRem = tempRem % 60;
		String seconds = String.format("%02d", tempRem);
		
		return hours + ":" + minutes + ":" + seconds;
	}
}
