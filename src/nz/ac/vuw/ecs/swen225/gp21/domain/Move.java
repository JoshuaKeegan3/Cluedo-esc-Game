package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * A move made on the board.

 * @author Joshua Keegan 30052483
 */
public class Move {
  /**
   * Change in x coordinate.
   */
  public final int dx;

  /**
   * change in y coordinate.
   */
  public final int dy;

  /**
   * Move Constructor.

   * @param dx - change in x
   * @param dy - change in y
   */
  public Move(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }
}
