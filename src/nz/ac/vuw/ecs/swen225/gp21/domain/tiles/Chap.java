package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import java.util.ArrayList;
import java.util.List;
import nz.ac.vuw.ecs.swen225.gp21.domain.Location;
import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;
import nz.ac.vuw.ecs.swen225.gp21.domain.TileColor;

/**
 * The player in the game.
 * Interacts with tiles.

 * @author Joshua Keegan 30052483
 *
 */
public class Chap extends Tile {

  private final List<TileColor> keys;
  private Location loc;
  private Tile standingOn;

  private String informationShown = null;
  private int treasuresCollected;
  private boolean alive = true;

  /**
   * Player constructor.
   *
   * @param keys               - currently collected keys
   * @param x                  - x position
   * @param y                  - y position
   * @param standingOn         - the tile chap is covering
   * @param treasuresCollected - how many treasures chap has collected
   */
  public Chap(List<TileColor> keys, int x, int y, Tile standingOn, int treasuresCollected) {
    super("chap_down");
    this.keys = new ArrayList<>(keys);
    this.loc = new Location(x, y);
    this.standingOn = standingOn;
    this.treasuresCollected = treasuresCollected;
  }

  /**
   * Returns whether or not a player has picked up a key that can open a door.
   *
   * @param door - the door to check if it can be opened
   * @return whether the door can be opened
   */
  public boolean hasKey(Door door) {
    return keys.contains(door.getColor());

  }

  /**
   * uses a key to open a door.
   *
   * @param door - door to be opened
   */
  public void removeKey(Door door) {
    keys.remove(door.getColor());
  }

  /**
   * player picks up a key.
   *
   * @param key - key picked up
   */
  public void addKey(Key key) {
    keys.add(key.getColor());
  }

  /**
   * Gets the location of the player.
   *
   * @return loc - location of player
   */
  public Location getLocation() {
    return loc;
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }

  /**
   * Gets the tile the player is standing on.
   *
   * @return tile player is standing on
   */
  public Tile isStandingOn() {
    return standingOn;
  }

  /**
   * sets the tile the player is standing on.
   *
   * @param t - tile under the player
   */
  public void setStandingOn(Tile t) {
    this.standingOn = t;
  }

  /**
   * Gets the number of treasures the player has collected.
   *
   * @return number of treasures collected
   */
  public int getTreasuresCollected() {
    return treasuresCollected;
  }

  /**
   * Gets the information show by the tile underneath it.
   *
   * @return information shown
   */
  public String getInformationShown() {
    return informationShown;
  }

  /**
   * Sets the information shown by the tile below.
   *
   * @param info - information shown buy and info card
   */
  public void setInformationShown(String info) {
    informationShown = info;

  }

  /**
   * increments treasures collected by 1.
   */
  public void collectedTreasure() {
    treasuresCollected += 1;
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    //no move, return tile chap is on
    chap.setInformationShown(null);
    return chap.isStandingOn();
  }

  @Override
  public char getTextRepresentation() {
    return 'c';
  }

  /**
   * changes chaps location to a new location.
   *
   * @param newLoc - new location
   */
  public void setLocation(Location newLoc) {
    loc = newLoc;
  }

  /**
   * Kills chap.
   */
  public void killed() {
    alive = false;
  }

  /**
   * returns whether chap is alive or dead.
   *
   * @return alive
   */
  public boolean isAlive() {
    return alive;
  }

  public List<TileColor> getHand() {
    return keys;
  }

}