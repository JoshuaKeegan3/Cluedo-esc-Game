package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.util.Objects;

/**
 * Location class.
 * Stores an x and y coordinate.
 * A move can be applied to produce a new location.
 * Can be converted into a 1d array coordinates.

 * @author Joshua Keegan 30052483
 */
public class Location {
  private final int x;
  private final int y;
  /**
   * Location constructor.

   * @param x - x coordinate
   * @param y - y coordinate
   */
  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }
  /**
   * Adds a moves direction and magnitude to the player.

   * @param m - move to apply
   * @return new location
   */
  public Location apply(Move m) {
    return new Location(x + m.dx, y + m.dy);
  }

  /**
   * Get x coordinate.

   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * Get the y coordinate.

   * @return y
   */
  public int getY() {
    return y;
  }
}
