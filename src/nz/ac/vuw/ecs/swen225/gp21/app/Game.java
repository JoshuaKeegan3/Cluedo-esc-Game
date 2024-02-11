package nz.ac.vuw.ecs.swen225.gp21.app;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.domain.TileColor;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;
import org.dom4j.DocumentException;


/**
 * The type Game.
 * Game class interacts with other parts of program while game is
 * running Fields are used by the Side class Timer runs while game is being played Methods
 * to interact with other packages in game.
 *
 * @author 300524008 chujoel
 */
public class Game {
  /**
   * The End.
   */
  protected int end = 60;
  /**
   * The App.
   */
  protected App app;
  /**
   * The Domain.
   */
  protected Domain domain;

  /**
   * The New game.
   */
  //Variable for first level
  final int newGame = 1;

  /**
   * The Level.
   */
  int level = 1;

  /**
   * The Time.
   */
  int time = 0;

  private boolean play;

  protected String message = "";

  private Timer timer = new Timer();


  /**
   * Game constructor.
   * - Sets the domain
   * - Setup timer which runs in background
   *
   * @param app - Passed in to keep reference back to "App" class
   */
  Game(App app) {
    //instantiate();
    this.app = app;
  }

  /**
   * startTimer Method.
   * Sets game timer and increments every second
   * Stops the game once it reaches 60 seconds
   */
  private void startTimer() {
    //Timer that increments time every second
    //Code from https://stackoverflow.com/questions/11520819/java-create-background-thread-which-does-something-periodically
    timer.scheduleAtFixedRate(
        new TimerTask() {
          // Should redraw time here
          public void run() {
            time++;
            message = "No message";
            if (domain.getDisplayedInformation() != null) {
              StringBuilder newMessage = new StringBuilder(domain.getDisplayedInformation());
              newMessage.insert(28,"z");
              message = (newMessage.toString()).replaceAll("z", "<br/>");
              message = "<html>" + message + "</html>";
            }
            app.update();
            domain.update();
            if (!domain.getChap().isAlive()) {  //Check if chap is dead
              app.textWindow("Muahhahahah, you died. better luck next time", 10);
              resetLevel(level);
            }
            if (!play) {
              this.cancel();  // Pause game
            }

            if (time >= end) {
              String message = "You are out of time, the level will reset";
              app.textWindow(message, 10);
              resetLevel(level);  // SHows message first
              this.cancel();  // Stops the timer after 60 seconds.
            }
          }
        }, 0, 1000);
  }

  /**
   * Method to start the game or pause the game.
   *
   * @param condition - boolean to determine if the game continues or stops
   */
  public void setPlay(boolean condition) {
    this.play = condition;
    if (condition) {
      timer = new Timer();
      startTimer();
    } else {
      timer.cancel();
    }
  }

  /**
   * Instantiate method.
   * - Sets fields:
   * time limit, level and time used
   * Game is resumed
   */
  private void setGame() {
    end=app.persistency.getTime(); //Get time limit of game
    setPlay(true);
  }


  /**
   * Method to reset level.
   * Calls Persistency to reset the game
   * -Domain is passed back and new domain is updated
   * The game is updated
   *
   * @param selectedLevel - level to reset game to
   */
  public void resetLevel(int selectedLevel) {
    setPlay(false);
    app.persistency.loadLevel("levels/level" + selectedLevel + ".xml");
    level = selectedLevel;
    app.setDomain(app.persistency.getDomain());
    time = 0;
    setGame();
  }

  /**
   * New game is loaded.
   * File pop-up to select an xml file
   * If Player pushes enter, load level from persistency
   * set domain and time played
   */
  public void loadGame() {
    setPlay(false);
    JFileChooser file = new JFileChooser("levels");
    int returnVal = file.showOpenDialog(app.frame);
    if (returnVal == 0) { //File selected
      app.persistency.loadLevel(file.getSelectedFile().getPath());
      app.setDomain(app.persistency.getDomain());

      time=0;
      setGame();
    }
  }

  /**
   * New game for Recorder.
   * File pop-up to select an xml file
   * loads Recorder
   */
  public void loadRecord() {
    setPlay(false);
    JFileChooser file = new JFileChooser("src");
    int returnVal = file.showOpenDialog(app.frame);
    app.recorder = new Recorder(app);
    try {
      app.recorder.loadRecorded(file.getSelectedFile());
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    setGame();
  }

  public void resume() {
    setPlay(true);
  }

  /**
   * save game method.
   * Current progress of game is saved
   */
  public void saveGame() {
    app.persistency.prettyPrintXML();//Save domain
    timer.cancel();
    System.exit(0);
  }

  /**
   * Method to quitGame.
   * Creates new game at current level
   * Save game level
   * Stop loop and quit game
   */
  public void quitPlayingGame() {
    if (app.recorder != null) {
      app.recorder.stopRecording();
    }

    setGame();
    resetLevel(level);
    app.persistency.prettyPrintXML();
    timer.cancel();
    System.exit(0);
  }


  /**
   * Method to record the game.
   * Created a new Recorder
   */
  public void recordGame() {
    app.recorder = new Recorder(app);  //Passing app instance to recorder
  }


  // Setters and getters for fields

  /**
   * Gets level.
   *
   * @return level level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets level.
   *
   * @param level the level
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Gets time.
   *
   * @return time time
   */
  public int getTime() {
    return time;
  }

  /**
   * Sets time.
   *
   * @param time the time
   */
  public void setTime(int time) {
    this.time = time;
  }


  /**
   * Gets remaining keys.
   *
   * @return time remaining keys
   */
  public int getRemainingKeys() {
    return Math.max(domain.getTreasuresToCollect() - domain.getChap().getTreasuresCollected(), 0);
  }

  /**
   * Gets hand.
   *
   * @return keys hand
   */
  public List<TileColor> getHand() {
    return domain.getChap().getHand();
  }

  public boolean checkFinish() {
    return domain.isFinished();
  }

  /**
   * Checks what level player is on.
   * Either goes to next level or displays winning message
   */
  public void nextGame() {
    if (level == 1) {
      app.textWindow("Not too shabby, your skills are impressive. Lets see how you do"
          + " against my minions. ", 10);
      resetLevel(2);
    } else {
      app.textWindow("You have finally escaped the maze. Well Done adventurer. Exit this"
          + " window to restart your journey.", 10);
      resetLevel(1);
    }
  }


}
