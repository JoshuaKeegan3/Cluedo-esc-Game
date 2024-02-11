package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * Tile is never accessible by the player.

 * @author Joshua Keegan 30052483
 */
public class Wall extends Tile {

  /**
   * Wall constructor.
   */
  public Wall() {
    super("wall");
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
    return 'w';
  }
}
