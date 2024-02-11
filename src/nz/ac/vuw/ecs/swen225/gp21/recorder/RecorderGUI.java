package nz.ac.vuw.ecs.swen225.gp21.recorder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Recorder GUI provides user controls for replay functions, uses Recorder for functionality after listening to actions
 *
 * Author: Johanah Gloria
 * ID: 300466914
 */
public abstract class RecorderGUI extends JFrame implements ActionListener, ChangeListener {

    protected abstract void resumeGame();
    protected abstract void stepByStep();
    protected abstract void autoReply(int speed);

    private JFrame replay;
    private String stepByStepString;
    private String autoReplyString;
    private String stepByStepButtonString;
    private String resumeButtonString;
    private JButton stepByStepButton;
    private JButton resumeButton;
    private JRadioButton stepByStep;
    private JRadioButton autoReply;
    private JSlider slider;

    private boolean isAutoReply = false;
    private int speed = 3;

    /**
     * Constructor for RecorderGUI class
     */
    public RecorderGUI() {
    }

    /**
     * Displays a given message with JOptionPane
     * @param body, title
     */
    public void displayMessage(String body, String title) {
        JOptionPane.showMessageDialog(replay, body, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates JFrame that holds replay control
     */
    public void createReplayFrame() {
        displayMessage("Select Replay Mode", "Select");
        replay = new JFrame("Replay");

        // create step by step radio button
        JPanel stepByStepRadioPanel = new JPanel();
        stepByStep = new JRadioButton("Step By Step");
        stepByStepString = stepByStep.getText();
        stepByStep.setActionCommand(stepByStepString);
        stepByStep.addActionListener(this);
        stepByStep.setSelected(true);
        isAutoReply = false;
        stepByStepRadioPanel.add(stepByStep);
        replay.add(stepByStepRadioPanel);

        // add step by step button for stepping back
        JPanel stepByStepButtonPanel = new JPanel();
        stepByStepButton = new JButton("Step Back");
        stepByStepButtonString = stepByStepButton.getText();
        stepByStepButton.setActionCommand(stepByStepButtonString);
        stepByStepButton.setEnabled(true);
        stepByStepButton.addActionListener(this);
        stepByStepButtonPanel.add(stepByStepButton);
        replay.add(stepByStepButtonPanel);

        // create auto reply radio button
        JPanel autoReplyRadioPanel = new JPanel();
        autoReply = new JRadioButton("Auto Reply");
        autoReplyString = autoReply.getText();
        autoReply.setActionCommand(autoReplyString);
        autoReply.addActionListener(this);
        autoReply.setSelected(false);
        autoReplyRadioPanel.add(autoReply);
        replay.add(autoReplyRadioPanel);
        replay.setLayout(new GridLayout(3, 1));

        // create slider for set reply speed
        JPanel sliderPanel = new JPanel();
        JLabel sliderLabel = new JLabel("Set Auto-Reply Speed (fast to slow)");
        sliderPanel.add(sliderLabel);
        slider = new JSlider(JSlider.HORIZONTAL, 600, 1000, 800);
        slider.setMinorTickSpacing(100);
        slider.setMajorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        sliderPanel.add(slider);
        slider.addChangeListener(this);
        replay.add(sliderPanel);
        replay.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        replay.setSize(600,300);
        replay.setVisible(true);

        // add resume button for after replay
        JPanel resumePanel = new JPanel();
        resumeButton = new JButton("Resume Game");
        resumeButtonString = resumeButton.getText();
        resumeButton.setActionCommand(resumeButtonString);
        resumeButton.setEnabled(true);
        resumeButton.addActionListener(this);
        resumePanel.add(resumeButton);
        replay.add(resumePanel);
    }

    /**
     * Listens to step by step and auto-reply radiobutton and button actions, calls corresponding classes from Recorder
     * for functionality
     * @param e action event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // step by step
        if (e.getActionCommand().equals(stepByStepString)) {
            stepByStep.setSelected(true);
            autoReply.setSelected(false);
            stepByStepButton.setEnabled(true);
            isAutoReply = false;
        }
        if (e.getActionCommand().equals(stepByStepButtonString) && !isAutoReply) {
            stepByStep();
        }

        // auto-reply
        else if (e.getActionCommand().equals(autoReplyString)) {
            autoReply.setSelected(true);
            stepByStep.setSelected(false);
            stepByStepButton.setEnabled(false);
            isAutoReply = true;
        }

        // resume game
        else if (e.getActionCommand().equals(resumeButtonString)) {
            resumeGame();
        }
    }

    /**
     * Listens to auto-reply slider actions, takes the selected speed and passes to Recorder method to add functionality
     * @param e action event object
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting() && isAutoReply) {
            speed = source.getValue();
            autoReply(speed);
        }
    }

    public void closeWindow() {
        replay.setVisible(false);
    }
}