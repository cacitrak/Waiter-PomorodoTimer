package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import app.PomodoroPhase;
import fonts.AwesomeIcons;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class WaiterController implements Initializable {

	@FXML private Label startPause;
	@FXML private Label restart;
	@FXML private Label back;
	@FXML private Label forward;
	@FXML private Label timerText;
	
	@FXML private ProgressBar work1;
	@FXML private ProgressBar work2;
	@FXML private ProgressBar work3;
	@FXML private ProgressBar work4;
	
	@FXML private ProgressBar pause1;
	@FXML private ProgressBar pause2;
	@FXML private ProgressBar pause3;
	@FXML private ProgressBar pause4;
	
	private HashMap<String, ProgressBar> barMap;
	
	private int workTime = 10; 
			//1500;
	private int shortPauseTime = 5;
	//300;
	private int longPauseTime = 8;
	//900;
	
	private app.PomodoroPhase currentPhase = app.PomodoroPhase.WORK;
	private int currentStep = 1;
	
	private Timeline timeline;
	private Integer timeSeconds = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startPause.setText(AwesomeIcons.ICON_PLAY);
		Tooltip.install(startPause, new Tooltip("Start/Pause the timer"));
		restart.setText(AwesomeIcons.ICON_REFRESH);
		Tooltip.install(restart, new Tooltip("Reset the timer completely"));
		back.setText(AwesomeIcons.ICON_STEP_BACKWARD);
		Tooltip.install(back, new Tooltip("Step backward one phase"));
		forward.setText(AwesomeIcons.ICON_STEP_FORWARD);
		Tooltip.install(forward, new Tooltip("Step forward one phase"));
		
		timerText.setText("00:00:00");
		
		barMap = new HashMap<>();
		barMap.put(PomodoroPhase.WORK.name() + 1, work1);
		barMap.put(PomodoroPhase.WORK.name() + 2, work2);
		barMap.put(PomodoroPhase.WORK.name() + 3, work3);
		barMap.put(PomodoroPhase.WORK.name() + 4, work4);
		
		barMap.put(PomodoroPhase.PAUSE.name() + 1, pause1);
		barMap.put(PomodoroPhase.PAUSE.name() + 2, pause2);
		barMap.put(PomodoroPhase.PAUSE.name() + 3, pause3);
		barMap.put(PomodoroPhase.LONGPAUSE.name() + 4, pause4);
	}
	
	@FXML
	private void startPauseMouseClick() {
		if(AwesomeIcons.ICON_PLAY.equals(startPause.getText())){
			startPause.setText(AwesomeIcons.ICON_PAUSE);
			startTimer();
		}
		else {
			startPause.setText(AwesomeIcons.ICON_PLAY);
			pauseTimer();
		}
	}
	
	private ProgressBar currentPhase() {
		return barMap.get(currentPhase.name() + currentStep);
	}
	
	public void startTimer() {
		if (timeline != null) timeline.play();
		else {
			timeline = new Timeline();

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
								prepNextPhase();
								timeline.setCycleCount(timeSeconds);
								timeline.playFromStart();
							}
						}
					}));
			timeline.playFromStart();
		}
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
	
	@FXML
	public void resetTimer() {
		prepFirstPhase();
	}
	
	private void prepNextPhase() {
		PomodoroPhase nextPhase = nextPhaseType();
		if(nextPhase == PomodoroPhase.WORK) {
			currentStep++;
			if(currentStep > 4) {
				currentStep = 1;
				resetBars();
			}
		}
		timeSeconds = nextPhaseTime();
		currentPhase = nextPhase;
	}
	
	private void prepFirstPhase() {
		if(timeline != null){
			timeline.stop();
			currentPhase = PomodoroPhase.WORK;
			currentStep = 1;
			timeSeconds = workTime;
			timeline.setCycleCount(workTime);
			resetBars();
			timerText.setText("00:00:00");
			if(AwesomeIcons.ICON_PAUSE.equals(startPause.getText()))
				startPause.setText(AwesomeIcons.ICON_PLAY);
		}
	}
	
	public void handleLoadingBar() {
		int currPhaseTime = currentPhaseTime();
		int elapsedTime = currPhaseTime - timeSeconds;
		
		double fractionElapsed = (double)elapsedTime/(double)currPhaseTime;
		
		currentPhase().setProgress(fractionElapsed);
	}
	
	public int currentPhaseTime()
	{
		switch(currentPhase) {
		case WORK:		return workTime;
		case PAUSE:	    return shortPauseTime;
		case LONGPAUSE:	return longPauseTime;
		}
		return -1;
	}
	
	private PomodoroPhase nextPhaseType() {
		switch(currentPhase) {
		case WORK:
			if(currentStep == 4) return PomodoroPhase.LONGPAUSE;
			return PomodoroPhase.PAUSE;
		case PAUSE:
		case LONGPAUSE:
			return PomodoroPhase.WORK;
		}
		return null;
	}
	
	public int nextPhaseTime() {
		switch(currentPhase) {
		case WORK:
			if(currentStep == 4) return longPauseTime;
			return shortPauseTime;
		case PAUSE:
		case LONGPAUSE:
			return workTime;
		}
		return -1;
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
	
	private void resetBars() {
		for(ProgressBar r : barMap.values())
			r.setProgress(0);
	}
}
