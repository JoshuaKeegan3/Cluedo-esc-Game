package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * Exit Lock.
 * Opens when player has enough keys.

 * @author Joshua Keegan 30052483
 */
public class Lock extends Tile {

  /**
   * Lock Constructor.
   */
  public Lock() {
    super("locked_door_green");
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return false;
  }

  @Override
  public Tile stoodOnBy(Chap chap) throws ChapOnInvalidTileException {
    throw new ChapOnInvalidTileException();
  }

  @Override
  public char getTextRepresentation() {
    return 'l';
  }
}
