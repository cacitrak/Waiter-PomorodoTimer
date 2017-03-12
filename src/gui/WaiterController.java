package gui;

import java.net.URL;
import java.util.ResourceBundle;

import PomodoroPhase;
import fonts.AwesomeIcons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

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
	
	private int workTime;
	private int shortPauseTime;
	private int longPauseTime;
	
	private app.PomodoroPhase currentPhase = app.PomodoroPhase.WORK;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startPause.setText(AwesomeIcons.ICON_PLAY);
		restart.setText(AwesomeIcons.ICON_REFRESH);
		back.setText(AwesomeIcons.ICON_STEP_BACKWARD);
		forward.setText(AwesomeIcons.ICON_STEP_FORWARD);
		
		timerText.setText("00:00:00");
	}
	
	@FXML
	private void startPauseMouseClick() {
		if(AwesomeIcons.ICON_PLAY.equals(startPause.getText()))
				startPause.setText(AwesomeIcons.ICON_PAUSE);
		else
				startPause.setText(AwesomeIcons.ICON_PLAY);
	}

}
