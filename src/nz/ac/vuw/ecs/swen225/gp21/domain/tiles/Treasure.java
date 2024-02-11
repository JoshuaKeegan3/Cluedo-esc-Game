package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * Treasure tile.
 * Can be collected by chap to unlock an exit.

 * @author Joshua Keegan 30052483
 */
public class Treasure extends Tile {

  /**
   * Treasure constructor.
   */
  public Treasure() {
    super("treasure");
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.collectedTreasure();
    return new EmptyTile();
  }

  @Override
  public char getTextRepresentation() {
    return 't';
  }
}
