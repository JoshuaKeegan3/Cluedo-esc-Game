package nz.ac.vuw.ecs.swen225.gp21.domain.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.domain.Location;
import nz.ac.vuw.ecs.swen225.gp21.domain.Move;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.EmptyTile;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Enemy;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Wall;
import nz.ac.vuw.ecs.swen225.gp21.persistency.*;
import org.junit.Test;


/**
 * Domain module tests.
 * 80% coverage.

 * @author joshkeegan
 *
 */
public class DomainTests {

  // ================================================
  // Valid Tests
  // ================================================

  String level1 = "wwwwwwwwwww\n"
      + "wwwwwewwwww\n"
      + "wwwwwlwwwww\n"
      + "wwwwwdwwwww\n"
      + "wwwwi__wwww\n"
      + "w__w_k_w__w\n"
      + "wk_d_c_d_kw\n"
      + "w__w___w__w\n"
      + "wwwwwdwwwww\n"
      + "wwwwtttwwww\n"
      + "wwwwtttwwww\n"
      + "wwwwtktwwww\n"
      + "wwwwwwwwwww\n";

  /**
   * tests move up.
   */
  @Test
  public void test_valid_01() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -1));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_c_w__w\n"
        + "wk_d___d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests move down.
   */
  @Test
  public void test_valid_02() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, 1));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d___d_kw\n"
        + "w__w_c_w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests move left.
   */
  @Test
  public void test_valid_03() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(-1, 0));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_dc__d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests move right.
   */
  @Test
  public void test_valid_04() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(1, 0));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d__cd_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving onto a treasure.
   */
  @Test
  public void test_valid_05() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, 3));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d___d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtctwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving onto a treasure and off a treasure.
   */
  @Test
  public void test_valid_06() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, 3));
    moves.add(new Move(0, -3));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d_c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwt_twwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving onto a information tile.
   */
  @Test
  public void test_valid_07() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level1.xml");
    Domain game = loader.getDomain();
    try {
      game.move(new Move(-1, -2));
    } catch (ChapOnInvalidTileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertEquals(game.getDisplayedInformation(), "Music make you lose control. Music make you lose control");
  }

  /**
   * tests moving onto a key tile.
   */
  @Test
  public void test_valid_08() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -1));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_c_w__w\n"
        + "wk_d___d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving onto and off a key tile.
   */
  @Test
  public void test_valid_09() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -1));
    moves.add(new Move(0, 1));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w___w__w\n"
        + "wk_d_c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests collecting a key and opening a door.
   */
  @Test
  public void test_valid_10() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -1));
    moves.add(new Move(-2, 1));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w___w__w\n"
        + "wk_c___d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests collecting a key, opening a door and moving away from the door.
   */
  @Test
  public void test_valid_11() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -1));
    moves.add(new Move(-2, 1));
    moves.add(new Move(2, 0));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w___w__w\n"
        + "wk___c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests winning.
   */
  @Test
  public void test_valid_12() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level1.xml");
    Domain game = loader.getDomain();
    assertFalse(game.isFinished());
    try {
      game.move(new Move(0, -5));
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
    assertTrue(game.isFinished());
  }
  
  /**
   * tests chap dying.
   */
  @Test
  public void test_valid_13() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level2.xml");
    Domain game = loader.getDomain();
    try {
      
      game.update();
      
      assertTrue(game.getChap().isAlive());
      game.update();
      game.move(new Move(2, 0));
      System.out.println(game.getBoardString());
      assertFalse(game.getChap().isAlive()); 
      
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * tests chap dying.
   */
  @Test
  public void test_valid_14() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level2.xml");
    Domain game = loader.getDomain();
    try {
      
      game.move(new Move(2, 0));
      game.update();
      game.update();
      assertFalse(game.getChap().isAlive()); 
      
      
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * collects enough to open the exit.
   */
  @Test
  public void test_valid_15() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level1.xml");
    Domain game = loader.getDomain();
    try {
      game.move(new Move(0, 3));
      game.move(new Move(0, 1));
      game.move(new Move(1, 0));
      game.getChap().getHand();
      game.getChap().getLocation();
      assertTrue(game.getChap().getTreasuresCollected() == 3);
      
    } catch (ChapOnInvalidTileException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * other tests.
   */
  @Test
  public void test_valid_16() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level1.xml");
    Domain game = loader.getDomain();
    assertTrue(game.getBoard().getHeight() == 13);
    assertEquals(new ChapOnInvalidTileException().toString(),
    	"nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException");
    assertEquals(new ChapOnInvalidTileException("").toString(),
    	"nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException: ");
    assertTrue(game.getEnemies().isEmpty());
    assertEquals(game.getTreasuresToCollect(), 2);
    try {
		game.getBoard().connectBoardChangedEvent(this, 
		        getClass().getDeclaredMethod("onBoardChanged", Location.class));
		game.getBoard().disconnectBoardChangedEvent(this, 
		        getClass().getDeclaredMethod("onBoardChanged", Location.class));
	} catch (NoSuchMethodException | IllegalArgumentException | SecurityException e ) {
	}
    
  }
  public void onBoardChanged(Location l) {
	  
  }

  // ================================================
  // Invalid Tests
  // ================================================

  /**
   * tests moving into a wall.
   */
  @Test
  public void test_invalid_01() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(1, 2));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d_c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving into a lock.
   */
  @Test
  public void test_invalid_02() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, -4));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d_c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests moving into a door with no key.
   */
  @Test
  public void test_invalid_03() {
    List<Move> moves = new ArrayList<Move>();
    moves.add(new Move(0, 2));
    check("1", moves, "wwwwwwwwwww\n"
        + "wwwwwewwwww\n"
        + "wwwwwlwwwww\n"
        + "wwwwwdwwwww\n"
        + "wwwwi__wwww\n"
        + "w__w_k_w__w\n"
        + "wk_d_c_d_kw\n"
        + "w__w___w__w\n"
        + "wwwwwdwwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtttwwww\n"
        + "wwwwtktwwww\n"
        + "wwwwwwwwwww\n");
  }

  /**
   * tests not winning.
   */
  @Test
  public void test_invalid_04() {
    Persistency loader = new Persistency();
    loader.loadLevel("levels/level1.xml");
    Domain game = loader.getDomain();
    assertFalse(game.isFinished());
  }

  /** The following provides a simple helper method for all tests.

   * @param level - start level
   * @param moves - list of moves to be made
   * @param expectedOutput - expected output
   */
  public static void check(String level, List<Move> moves, String expectedOutput) {
    Domain game = null;
    try {
      Persistency loader = new Persistency();
      loader.loadLevel("levels/level1.xml");
      game = loader.getDomain();
      for (Move m : moves) {
        game.move(m);
      }
      String output = game.getBoardString();
      assertEquals(expectedOutput, output);
    } catch (Exception e) {
      fail("Failing gamestate: " + game.getBoardString());
    }
  }
}
