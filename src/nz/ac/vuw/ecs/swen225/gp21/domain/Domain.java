package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.*;

/**
 * Domain class Contains the model for the game.
 * Represents and maintains the state of the game.
 * This module is not concerned with how the game is displayed or stored.

 * @author Joshua Keegan 30052483
 */

public class Domain {

  private Board board;

  private int treasuresToCollect;

  private Chap chap;
  private String displayedInformation;

  private List<Enemy> enemies = new ArrayList<>();

  /**
   * Domain constructor.

   * @param width              - width of the board
   * @param height             - height of the board
   * @param treasuresToCollect - number of treasures to collect
   * @param board              - tile map
   * @param chap               - player
   */

  public Domain(int width, int height, int treasuresToCollect, Tile[] board,
                Chap chap, List<Enemy> enemies) {
    this.chap = chap;
    this.enemies = enemies;

    int chapIndex = chap.getLocation().getY() * width + chap.getLocation().getX();
    chap.setStandingOn(board[chapIndex]);
    board[chapIndex] = chap;

    this.board = new Board(board, width, height);

    this.treasuresToCollect = treasuresToCollect;
    this.enemies = enemies;
    for (Enemy e : enemies) {
      int enemyIndex = e.getLocation().getY() * width + e.getLocation().getX();
      e.setStandingOn(board[enemyIndex]);
      board[enemyIndex] = e;
    }
  }

  /**
   * Applies a move to the domain model.

   * @param m - move to be applied
   * @return whether the move was valid or not
   * @throws ChapOnInvalidTileException thrown if chap ends up on an unexpected tile
   */
  public boolean move(Move m) throws ChapOnInvalidTileException {
    Location newLoc = chap.getLocation().apply(m);
    Tile moveTo = board.getTile(newLoc);

    if (!moveTo.accessibleBy(chap)) {
      return false;
    }

    board.setTile(chap.getLocation(), chap.isStandingOn());

    moveTo = moveTo.stoodOnBy(chap);
    chap.setStandingOn(moveTo);

    board.setTile(newLoc, chap);
    if (chap.getTreasuresCollected() >= treasuresToCollect) {
      for (int i = 0; i < board.getSize(); i++) {
        if (board.getBoard()[i] instanceof Lock) {
          board.getBoard()[i] = new EmptyTile();
        }
      }
    }

    displayedInformation = chap.getInformationShown();

    chap.setLocation(newLoc);
    return true;
  }

  /**
   * updates any moving objects that arn't controlled by the user.
   */
  public void update() {
    for (Enemy e : enemies) {
      board.setTile(e.getLocation(), e.isStandingOn());

      Location newLoc = e.getLocation().apply(e.getNextMove());
      e.setStandingOn(board.getTile(newLoc));
      e.setLocation(newLoc);
      if (board.getTile(newLoc) instanceof Chap) {
    	chap.killed();
      }
      board.setTile(newLoc, e);

    }
  }

  /**
   * Returns the player object.

   * @return chap
   */
  public Chap getChap() {
    return chap;
  }

  /**
   * Gets the board.

   * @return board board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns the information of the tile below chap.

   * @return information displayed to the user.
   */
  public String getDisplayedInformation() {
    return displayedInformation;
  }

  /**
   * Gets whether or not the game is finished.

   * @return if chap has reached the exit
   */
  public boolean isFinished() {
    return chap.isStandingOn() != null && chap.isStandingOn() instanceof Exit;
  }

  /**
   * Calculates a text representation of the board.

   * @return a text representation of the board
   */
  public String getBoardString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0, j = 1; i < board.getSize(); i++, j = (i + 1) % board.getWidth()) {
      sb.append(board.getBoard()[i].getTextRepresentation());
      if (j == 0) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
  /**
   * get the treasures remaining to open the gate.

   * @return treasuresToCollect
   */
  public int getTreasuresToCollect() {
    return treasuresToCollect;

  }

  public List<Enemy> getEnemies() {
    return enemies;
  }
}
