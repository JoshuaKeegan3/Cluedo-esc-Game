package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import nz.ac.vuw.ecs.swen225.gp21.domain.Location;
import nz.ac.vuw.ecs.swen225.gp21.domain.Move;
import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * The enemy in the game.
 * Interacts with chap.
 *

 * @author Joshua Keegan 30052483
 **/
public class Enemy extends Tile {

  private Location loc;
  private Tile standingOn;
  private Queue<Move> moves = new LinkedList<>();
  private String path;

  /**
   * Player constructor.

   * @param x - x position
   * @param y - y position
   */
  public Enemy(int x, int y, Tile standingOn, String path) {
    super("Enemy");
    this.loc = new Location(x, y);
    this.standingOn = standingOn;
    this.path = path;
    

    Scanner sc = new Scanner(path);
    while (sc.hasNext()) {
      Move m = new Move(0, 0);
      switch (sc.next()) {

        case "N": 
    	  m = new Move(0,-1);
    	  break;
        case "E": 
    	  m = new Move(1,0);
    	  break;
        case "S": 
    	  m = new Move(0,1);
    	  break;
        case "W": 
    	  m = new Move(-1,0);
    	  break;
        default:
    	  break;
      }
      int a = sc.nextInt();
      for(int i = 0; i < a; i++) {
    	  moves.add(m);
      }
    }
  }

  /**
   * Gets the location of the enemy.

   * @return loc - location of enemy
   */
  public Location getLocation() {
    return loc;
  }

  /**
   * changes enemy location to a new location.

   * @param newLoc - new location
   */
  public void setLocation(Location newLoc) {
	  loc = newLoc;
  }

  /**
   * Gets the tile the enemy is standing on.

   * @return tile enemy is standing on
   */
  public Tile isStandingOn() {
    return standingOn;
  }

  /**
   * sets the tile the enemy is standing on.

   * @param t - tile under the enemy
   */
  public void setStandingOn(Tile t) {
    this.standingOn = t;
  }


  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.setInformationShown(null);
    chap.killed();
    return new EmptyTile();
  }

  @Override
  public char getTextRepresentation() {
    return 'b';
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }
  /**
   * gets the next move for the thing to make.

   * @return next move
   */
  public Move getNextMove() {
	Move m = moves.poll();
	moves.add(m);
	return m;
  }
  /**
   * return the path.

   * @return path
   */
  public String getPath() {
	return path;
  }
}
