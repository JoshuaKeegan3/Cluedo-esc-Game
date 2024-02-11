package nz.ac.vuw.ecs.swen225.gp21.fuzz;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import nz.ac.vuw.ecs.swen225.gp21.app.App;
import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Move;
import org.junit.Test;

/**
 * FuzzTest is a class devoted to the testing
 * of Domain functionality for the Chap Challenge assignment.
 * Contains two methods, test1 and test2 which initalises tests.
 *
 * @author Thomas 300533037
 *
 */

public class FuzzTest {

  // Create variables to help keep track of time
  private Timer fuzzTime = new Timer();
  private int timeLimit = 59;
  private int time = 0;

  // All possible movements by Chap
  private List<Move> searchMoves = new ArrayList<Move>(Arrays.asList(
          new Move(-1, 0), // Representation of left movement
          new Move(1, 0), // Representation of right movement
          new Move(0, -1), // Representation of up movement
          new Move(0, 1) // Representation of down movement
  ));

  /**
   * startFuzzTimer initialises the timer to ensure each tests
   * is completed within the 60 second time limit and also
   * informs the user if any test is over 60 seconds.
   *
   * @author Thomas 300533037
   */
  private void startFuzzTimer() {
    // Set the timer to update every 1 second
    fuzzTime.scheduleAtFixedRate(
            new TimerTask() {

              /**
               * run method is called every 1000 milliseconds and increases
               * the timer. Also contains a conditional to check if timer is
               * over the 60 second time limit.
               *
               * @author Thomas 300533037
               */
              public void run() {
                time++;
                // If time is over the limit, inform the user and stop timer
                if (time >= timeLimit) {
                  System.out.println("Test went over the 60 second time limit!");
                  this.cancel();
                }
              }
            }, 0, 1000);
  }


  /**
   * This method involves using Depth First Search (DFS) recursion to find
   * the appropriate keys and enter through doors to find the exit.
   *
   * @param visitedLocations
   *       A List of Points which represent all locations that are already visited by Chap
   * @param origin
   *       The original location of Chap before movement stored as a Point
   * @param m
   *       The change in x and y from the origin stored as a Move
   *
   * @author Thomas 300533037
   *
   * @throws ChapOnInvalidTileException
   *       Testing
   */
  private void tileTest(List<Point> visitedLocations, Point origin,
                        Move m, App appTest) throws ChapOnInvalidTileException {
    // Check if the provided move is logically possible
    boolean validMove = appTest.domainMove(m);
    // Apply the movement in terms of the origin point
    Point newPos = new Point((int) (origin.getX() + m.dx),
            (int) (origin.getY() + m.dy));
    // Ensure we are visiting a location that has not been visited
    if (!visitedLocations.contains(newPos) && validMove) {

      // Add the location to the visited locations
      visitedLocations.add(new Point(newPos));
      // Recursively call all tiles that border Chap's new location
      tileTest(visitedLocations, newPos, searchMoves.get(3), appTest); // Down
      tileTest(visitedLocations, newPos, searchMoves.get(0), appTest); // Left
      tileTest(visitedLocations, newPos, searchMoves.get(1), appTest); // Right
      tileTest(visitedLocations, newPos, searchMoves.get(2), appTest); // Up

    }
    // If the move is valid but has been visited, reverse the move
    if (validMove) {
      // If movement is left, invert to a right movement
      if (searchMoves.get(0).equals(m)) {
        appTest.keyPressed(new KeyEvent(appTest.frame, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));
        // If movement is right, invert to a left movement
      } else if (searchMoves.get(1).equals(m)) {
        appTest.keyPressed(new KeyEvent(appTest.frame, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));
        // If movement is up, invert to a down movement
      } else if (searchMoves.get(2).equals(m)) {
        appTest.keyPressed(new KeyEvent(appTest.frame, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED));
        // If movement is down, invert to a up movement
      } else if (searchMoves.get(3).equals(m)) {
        appTest.keyPressed(new KeyEvent(appTest.frame, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED));
      }
    }
  }

  /**
   * test1 method involves initialising the DFS recursion to test
   * level 1 of Chap's challenge, also utilises a timer to ensure the
   * the test does not past 60 seconds of run time.
   *
   * @author Thomas 300533037
   */
  @Test
  public void test1() {
    // Initialise the timer
    this.time = 0;
    this.startFuzzTimer();
    // Set Chap's initial point as (0, 0) and add to the visitedLocations
    Point origin = new Point(0, 0);
    List<Point> visistedLocations = new ArrayList<Point>();
    visistedLocations.add(origin);
    App appTest = new App();
    try {
      // Get timer and set so this while loop runs for 59 seconds
      while (time < timeLimit - 1 && !appTest.game.checkFinish()) {
        // Start the recursive search
        tileTest(visistedLocations, origin, searchMoves.get(0), appTest);
        visistedLocations.clear();
      }
      // End the timer
      this.fuzzTime.cancel();
      // Inform the user if Chap makes an illegal move
    } catch (ChapOnInvalidTileException e) {
      System.out.println("Chap made an illegal move");
    }
  }

  /**
   * test2 method involves initialising the DFS recursion to test
   * level 2 of Chap's challenge, also utilises a timer to ensure the
   * the test does not past 60 seconds of run time.
   *
   * @author Thomas 300533037
   */
  @Test
  public void test2() {
    // Initialise the timer
    this.time = 0;
    this.startFuzzTimer();
    // Set Chap's initial point as (0, 0) and add to the visitedLocations
    Point origin = new Point(0, 0);
    List<Point> visistedLocations = new ArrayList<Point>();
    visistedLocations.add(origin);
    App appTest = new App();
    try {
      // Get timer and set so this while loop runs for 59 seconds
      while (time < timeLimit - 1) {
        tileTest(visistedLocations, origin, searchMoves.get(0), appTest);
        visistedLocations.clear();
      }
      // End the timer
      this.fuzzTime.cancel();
      // Inform the user if Chap makes an illegal move
    } catch (ChapOnInvalidTileException e) {
      System.out.println("Chap made an illegal move");
    }
  }
}
