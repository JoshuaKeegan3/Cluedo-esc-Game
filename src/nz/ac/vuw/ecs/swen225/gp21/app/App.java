package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.domain.Move;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Enemy;
import nz.ac.vuw.ecs.swen225.gp21.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;

/**
 * App class.
 * Responsible for Instantiating all aspects of the application
 * Sets Up the GUI and creates other classes which are responsible for other aspects
 * Has all action listeners for all user input:
 * - Listeners for keyboard input and menu-bar interaction.
 * Uses Game class to communicate with other packages.
 *
 * @author Joel 300524008
 */
public class App implements KeyListener {
  public JFrame frame = new JFrame();
  public Game game;
  Board board;

  private Popup mainPanel;
  private final JMenuBar menu = new JMenuBar();
  private final int width = 768;
  private final int height = 576;

  private final Dimension window = new Dimension(width, height);
  final double aspectratio = 1.333;

  // Instances of imported packages
  private Domain domain = null;
  protected Persistency persistency = null;
  protected Recorder recorder = null;

  private final String instructions = "Instructions: \n"
      + "You are stuck in a maze. The only way to escape is by"
      + " collecting all the chips on your way out. \n"
      + "You must find the keys and open the corresponding coloured doors to "
      + "access all the hidden chips. This must all be done before time runs out."
      + "\nYou better get going now. \n\nGood luck adventurer!!"
      + "\n\nControls: \nMovement: ArrowKeys  \nPause: Spacebar\n"
      + "Ctrl+S: Save and Exit\nCtrl+X: Exit\nCtrl+R: Load Game\n"
      + "Ctrl+1: Level 1\nCtrl+2: Level 2";

  /**
   *  -Used by fuzz for testing an d also game.
   *
   * @return domain - returns the current domain.
   */
  public Domain getDomain() {
    return domain;
  }

  /**
   * setDomain method.
   * - Updates the domain linked to renderer
   * - Updates local domain
   * - Updates domain in game
   *
   * @param domain - Most recent domain
   */
  protected void setDomain(Domain domain) {
    this.domain = domain;
    game.domain = domain;
    if (this.board != null) {
      System.out.println();
      board.setDomain(domain);
    }
  }

  /**
   * App constructor.
   * - Creates a new componentListener in frame for resizing
   * - Initialize persistency
   * - Persistency should pass back the last saved game if there is one
   */
  public App() {
    try {
      // Dark mode from :
      //https://stackoverflow.com/questions/36128291/how-to
      // -make-a-swing-application-have-dark-nimbus-theme-netbeans
      UIManager.setLookAndFeel(new NimbusLookAndFeel());
      UIManager.put("control", new Color(20, 20, 20));
      UIManager.put("info", new Color(20, 20, 20));
      UIManager.put("nimbusBase", new Color(30, 30, 30));
      UIManager.put("nimbusDisabledText", new Color(20, 20, 20));
      UIManager.put("nimbusInfoBlue", new Color(23, 85, 201));
      UIManager.put("nimbusLightBackground", new Color(30, 30, 30));
      UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
      UIManager.put("text", new Color(230, 230, 230));
      SwingUtilities.updateComponentTreeUI(frame);
    } catch (UnsupportedLookAndFeelException exc) {
      System.err.println("Nimbus: Unsupported Look and feel!");
    }
    frame.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        resize();  // Calls resize method to change dimensions proportionally
      }
    });

    // Window Listener to close all background processes
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        game.quitPlayingGame();
        System.exit(0);
        e.getWindow().dispose();
      }
    });

    game = new Game(this);  //Creates new game
    if (persistency == (null)) {
      String level = "levels/level1.xml";
      persistency = new Persistency();
      persistency.loadLevel(level);
      setDomain(persistency.getDomain());
    }

    setUpGui();
  }

  /**
   * setUpGUI method.
   * - Sets up the menubar
   * - Sets up frame
   * - Calls createSubPanel to setup panels inside the frame
   * - Calls update method on mainPanel
   */
  private void setUpGui() {
    setMenu();
    mainPanel = new MainWindow(frame, window);
    createSubPanel();

    // Setup frame
    frame.add(mainPanel);
    frame.setTitle("Chip's Challenge:LESSON 1");
    frame.addKeyListener(this);
    frame.pack();
    frame.setVisible(true);
    game.setPlay(true);
  }



  /**
   * createSubPanel.
   * - Instantiates renderer object
   * - Creates the smaller panels inside mainPanel
   */
  private void createSubPanel() {
    //Add smaller panes inside mainPanel
    this.board = new Board(mainPanel, domain);
    mainPanel.add(board);
    mainPanel.add(new Side(mainPanel, game));
  }

  protected void update() {
    //Redraw the whole board
    mainPanel.update();
  }


  /**
   * textWindow Method.
   * Creates a popup window that displays information passed in.
   * Pauses game and resumes game after window is closed.
   *
   * @param input - Message user wants to display
   * @param size - Size of text proportional
   */
  protected void textWindow(String input, int size) {
    game.setPlay(false);
    JTextPane textPane = new JTextPane();
    textPane.setText(input);

    Dimension fill = new Dimension((int) (frame.getWidth() * 0.8), (int) (frame.getHeight() * 0.8));
    textPane.setFont(new Font(textPane.getFont().getFontName(),
        Font.PLAIN, frame.getWidth() / size));
    textPane.setPreferredSize(fill);
    JOptionPane.showMessageDialog(frame, new JScrollPane(textPane));
    game.setPlay(true);
  }


  /**
   * method to only let the user resize the window proportionally.
   * Must still keep the same aspect ratio
   */
  private void resize() {
    int currWidth = frame.getWidth();
    int currHeight = frame.getHeight();
    double currentAspect = currWidth / currHeight;

    // Alter width and height if not proportional
    if (currentAspect > aspectratio) {
      currWidth = (int) (currHeight * aspectratio);
    } else {
      currHeight = (int) (currWidth / aspectratio);
    }
    frame.setSize(new Dimension(currWidth, currHeight));

    update();
  }


  @Override
  public void keyPressed(KeyEvent e) {
    int asciiRep = e.getKeyCode();
    if (e.isControlDown()) {
      switch ((char) e.getKeyCode()) {
        case ('X'):
          game.quitPlayingGame();
          return;

        case 'S':
          game.saveGame();
          return;

        case 'R':
          game.loadGame();
          return;

        case '1':
          game.resetLevel(1);
          return;

        case '2':
          game.resetLevel(2);
          return;

        default:
          System.out.println("invalid move");
      }
      


    }

    switch (asciiRep) {
      //Pause the game
      case 32:
        textWindow("Game is paused, press \"ESC\" to resume game", 10);
        return;

      case 38:  //Move down
        move(0, -1);
        return;

      case 37: //Move left
        move(-1, 0);
        return;

      case 40:  //Move up
        move(0, 1);
        return;

      case 39:  //Move right
        move(1, 0);
        return;

      default:
        System.out.println("Invalid move");
        return;

    }
  }


  /**
   * Move method.
   * Calls move method in domain.
   *
   * @param x -change in x direction.
   * @param y -change in y direction.
   */
  public void move(int x, int y) {

    //Move player using domain
    try {
      domain.move(new Move(x, y));
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
    update();
    if (game.checkFinish()) {
      game.nextGame();
    }
  }

  /**
   * domainMove method.
   * Used by Fuzz testing to interact with the Domain class
   *
   * @param move - Move object to pass to domain
   * @return - boolean passed back from domain.move
   */
  public boolean domainMove(Move move) {
    try {
      return domain.move(move);
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Creates menubar and adds listeners and dropdown when interacted with.
   * Need to add functionality
   */
  private void setMenu() {
    //Create Game submenu and add dropdown options
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newOption = new JMenuItem("New Game");
    newOption.addActionListener(e -> game.resetLevel(game.level));
    gameMenu.add(newOption);

    JMenuItem saveOption = new JMenuItem("Save");
    saveOption.addActionListener(e -> game.saveGame());
    gameMenu.add(saveOption);

    JMenuItem loadOption = new JMenuItem("Load");
    loadOption.addActionListener(e -> game.loadGame());
    gameMenu.add(loadOption);

    JMenuItem quitOption = new JMenuItem("Quit");
    quitOption.addActionListener(e -> game.quitPlayingGame());
    gameMenu.add(quitOption);

    JMenuItem recordLevel = new JMenuItem("Record");
    JMenuItem loadRecord = new JMenuItem("Load Record");

    JMenu recordMenu = new JMenu("Recorder");

    recordMenu.add(loadRecord);
    loadRecord.addActionListener(e -> game.loadRecord());

    recordLevel.addActionListener(e -> {
      // TODO Auto-generated method stub
      game.setPlay(false);
      game.recordGame();
      System.out.println("record now");
    });
    recordMenu.add(recordLevel);


    //Create option submenu and add dropdown options
    JMenu optionMenu = new JMenu("Options");
    JMenuItem soundOption = new JMenuItem("Background Music");
    optionMenu.add(soundOption);
    JMenuItem soundEffects = new JMenuItem("Sound Effects");
    optionMenu.add(soundEffects);
    //^^^^These will be implemented later on^^^^^



    JMenu levelMenu = new JMenu("Level");
    JMenuItem restartOption = new JMenuItem("Restart");
    restartOption.addActionListener(e -> game.resetLevel(1));
    levelMenu.add(restartOption);

    JMenu helpMenu = new JMenu("Help");
    JMenuItem instructionOption = new JMenuItem("Instructions");
    instructionOption.addActionListener(e -> {

      textWindow(instructions, 30);
    });
    helpMenu.add(instructionOption);



    menu.add(gameMenu);
    menu.add(recordMenu);
    menu.add(optionMenu);
    menu.add(levelMenu);
    menu.add(helpMenu);
    frame.setJMenuBar(menu);
  }



  public List<Enemy> getEnemies() {
    return domain.getEnemies();
  }


  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
