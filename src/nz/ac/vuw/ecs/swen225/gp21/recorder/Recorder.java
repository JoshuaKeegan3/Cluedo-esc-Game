package nz.ac.vuw.ecs.swen225.gp21.recorder;

import nz.ac.vuw.ecs.swen225.gp21.app.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import static java.lang.Thread.sleep;

/**
 * The recorder module records game play, and stores the recorded games in an xml file. It also adds the functionality
 * to load a recorded game, and to replay it using step-by-step, auto-reply and set replay speed functions.
 * Author: Johanah Gloria
 * ID: 300466914
 */

public class Recorder extends RecorderGUI {

  protected Listener listener;

  private Document currentGame; // xml document of current game
  private Element gameRoot; // game root for current game

  // actor root for current game
  private Element chapRoot;
  private Element bug1Root;
  private Element bug2Root;
  private Element bug3Root;

  private final App app; // app object
  private int gameCount = 1; // identifier for each game

  // loading recorded game
  private int gameID = 0;
  private int level = 0;
  private ArrayList<String> chapMoves;
  private Stack<String> chapMovesStack;

  // level 2
  private List<String> bug1moves;
  private List<String> bug2moves;
  private List<String> bug3moves;
  private Stack<String> bug1movesStack;
  private Stack<String> bug2movesStack;
  private Stack<String> bug3movesStack;

  /**
   * Initialises fields, creates new documents to record the current game and adds to the game history document.
   * Only in the first game will the history document be created.
   *
   * @param a the current app object
   */
  public Recorder(App a) {
    app = a;
    listener = new Listener(this);
    listener.addListeners(a);
    currentGame = createGameRoot();
  }

  /* =============== WRITING AND READING XML =============== */

  /**
   * Initialises a new document for the current game, creates a current game root, and adds level and actor node to it.
   * Also adds a the current game root to the history document.
   */
  public Document createGameRoot() {
    Document document = DocumentHelper.createDocument();
    gameRoot = document.addElement("game").addAttribute("id", String.valueOf(gameCount));
    Element level = gameRoot.addElement("level").addText(String.valueOf(app.game.getLevel()));
    chapRoot = gameRoot.addElement("actor").addAttribute("name", "chap");

    // if it's level 2, add the enemy actors to the tree
    if (app.game.getLevel() == 2) {
      bug1Root = gameRoot.addElement("actor").addAttribute("name", "bug1");
      bug2Root = gameRoot.addElement("actor").addAttribute("name", "bug2");
      bug3Root = gameRoot.addElement("actor").addAttribute("name", "bug3");
    }

    return document;
  }

  /**
   * Writes the moves to the current game document, and generates an XML file.
   *
   * @param document takes the document to generate into an xml file
   * @throws IOException throws IOException
   */
  public void write(Document document) throws IOException {
    // writes to a file
    XMLWriter writer;
    try (FileWriter fileWriter = new FileWriter("./recorded_game_" + gameCount + ".xml")) {
      writer = new XMLWriter(fileWriter);
      writer.write(document);
      writer.close();
    }

    // Pretty print the document to System.out (used for testing)
    OutputFormat format = OutputFormat.createPrettyPrint();
    writer = new XMLWriter(System.out, format);
    writer.write(document);
  }

  /**
   * When the game is saved, finished or ended abruptly, this method adds the time and chips-left nodes to the current
   * game root to store in the xml file. Afterwards an xml file is generated using write() method.
   */
  public void stopRecording() {
    try {
      write(currentGame);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /* =============== SAVING AND LOADING GAMES =============== */

  /**
   * Loads a saved game for a player to resume playing. Uses Persistency class to load the initial level state and
   * parses through the given XML document to restore it's current game state. Passes information to the App for display.
   *
   * @param file takes the file selected from recorded games
   */
  public void loadRecorded(File file) throws DocumentException {
    // converts File to Document
    SAXReader reader = new SAXReader();
    Document document = reader.read(file);

    // parse the XML to find the attributes and set loading fields
    parse(document);

    // load the initial state of selected level where the user last left off (pass to app, app loads persistency)
    loadMoves(chapMoves);

    // adds moves from list into stack
    chapMovesStack = addMovesToStack(chapMoves);

//		// add actors if level 2
//		if (level == 2 ) {
////			 gets enemy moves list from app - not implemented
//			bug1moves = app.getEnemies();
//			bug2moves = app.getEnemies();
//			bug3moves = app.getEnemies();
//			loadMoves(bug1moves);
//			loadMoves(bug2moves);
//			loadMoves(bug3moves);
//			bug1movesStack = addMovesToStack(bug1moves);
//			bug2movesStack = addMovesToStack(bug2moves);
//			bug2movesStack = addMovesToStack(bug3moves);
//		}

    // add and allow user to replay moves
    createReplayFrame();
  }

  /**
   * Loads the moves from the xml file using the app move method to restore a save recorded game state
   * (app loads initial state from persistency prior to this)
   *
   * @param moves takes the selected character moves
   */
  public void loadMoves(ArrayList<String> moves) {
    for (String move : moves) {
      switch (move) {
				case "left":
					app.move(-1, 0);
				case "right":
					app.move(1, 0);
				case "up":
					app.move(0, -1);
				case "down":
					app.move(0, 1);
      }
    }
  }

  /**
   * Adds the list of moves to a stack for replay functionality
   *
   * @param list takes a list of moves
   * @return stack
   * returns a stack of moves
   */
  public Stack<String> addMovesToStack(ArrayList<String> list) {
    Stack<String> stack = new Stack<>();
    for (String move : list) {
      stack.push(move);
    }
    return stack;
  }

  /**
   * Parses through a recorded game xml document, to update loading data fields
   *
   * @param document takes the xml document to parse
   */
  public void parse(Document document) {
    Element root = document.getRootElement();

    // gets id
    gameID = Integer.parseInt(root.attributeValue("id"));
    System.out.println(gameID);

    // iterate through 2nd child elements of root
    for (Iterator<Element> it_root = root.elementIterator(); it_root.hasNext(); ) {
      Element snd_child_element = it_root.next();

      // gets level
      if (snd_child_element.getName().equals("level")) {
        level = Integer.parseInt(snd_child_element.getText());
        System.out.println(level);
      }

      // gets actor and their moves
      else if (snd_child_element.getName().equals("actor")) {
        chapMoves = new ArrayList<>();

        // if there are enemy actors
        if (snd_child_element.attributeValue("name").equals("bug1")
            || snd_child_element.attributeValue("name").equals("bug2")
            || snd_child_element.attributeValue("name").equals("bug3")) {
          bug1moves = new ArrayList<>();
          bug2moves = new ArrayList<>();
          bug3moves = new ArrayList<>();
        }

        // iterate through move attributes of child element (actor) to create actors moves
        for (Iterator<Element> it_moves = snd_child_element.elementIterator(); it_moves.hasNext(); ) {
          Element move_element = it_moves.next();
          if (snd_child_element.attributeValue("name").equals("chap")) {
            chapMoves.add(move_element.attributeValue("dir"));
          } else if (snd_child_element.attributeValue("name").equals("bug1")) {
            bug1moves.add(move_element.attributeValue("dir"));
          } else if (snd_child_element.attributeValue("name").equals("bug2")) {
            bug2moves.add(move_element.attributeValue("dir"));
          } else if (snd_child_element.attributeValue("name").equals("bug2")) {
            bug2moves.add(move_element.attributeValue("dir"));
          }
        }
        System.out.println(chapMoves);
        System.out.println(bug1moves);
        System.out.println(bug2moves);
        System.out.println(bug3moves);
      }
    }
  }

  /* =============== REPLAY FUNCTIONS =============== */

  /**
   * Step by step function: when replay is enabled, the user uses arrow keys to manually replay their last move.
   * When there are no more moves remaining, the game resumes
   */
  protected void stepByStep() {
    if (!chapMovesStack.empty()) {
      moveActor(chapMovesStack);
      if (level == 2) {
        if (!bug1movesStack.empty()) {
          moveActor(bug1movesStack);
        }
        if (!bug2movesStack.empty()) {
          moveActor(bug2movesStack);
        }
        if (!bug3movesStack.empty()) {
          moveActor(bug3movesStack);
        }
      }
    } else {
      resumeGame();
    }
  }

  /**
   * Auto-reply function: when replay is enabled, automatically replays every move made by the user depending on the
   * speed of the users choosing. Sleep delay is used to determine speed
   * <p>
   * This doesn't work step by step, however actors can move back from their path
   *
   * @param speed takes the amount of delay between the execution of moves
   */
  protected void autoReply(int speed) {
    while (!chapMovesStack.empty()) {
      moveActor(chapMovesStack);
      if (level == 2) {
        if (!bug1movesStack.empty()) {
          moveActor(bug1movesStack);
        }
        if (!bug2movesStack.empty()) {
          moveActor(bug2movesStack);
        }
        if (!bug3movesStack.empty()) {
          moveActor(bug3movesStack);
        }
        try {
          sleep(speed);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    if (chapMovesStack.empty()) {
      resumeGame();
    }
  }

  /**
   * Moves the actors on the board back by one move
   *
   * @param stack takes stack of moves
   */
  public void moveActor(Stack<String> stack) {
    if (!stack.empty()) {
      String move = stack.peek();
      switch (move) {
				case "left":
					app.move(1, 0);
				case "right":
					app.move(-1, 0);
				case "up":
					app.move(0, 1);
				case "down":
					app.move(0, -1);
      }
      stack.pop();
    }
  }

  /**
   * Closes GUI window and resumes the game
   */
  protected void resumeGame() {
    app.game.resume();
    closeWindow();
  }

  /* =============== GETTERS AND SETTERS =============== */

  /**
   * Returns actor root
   *
   * @return element
   * returns the actor root element
   */
  public Element getChapRoot() {
    return chapRoot;
  }
}
