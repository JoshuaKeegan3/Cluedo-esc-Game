package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * Empty tile.
 * Replaces some tiles, anything can move onto it.

 * @author Joshua Keegan 30052483
 */
public class EmptyTile extends Tile {

  /**
   * Empty tile Constructor.
   */
  public EmptyTile() {
    super("floor");
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.setInformationShown(null);
    return this;
  }

  @Override
  public char getTextRepresentation() {
    return '_';
  }
}
