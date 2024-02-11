package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * Ends the game when touched by Chap.

 * @author Joshua Keegan 30052483
 */
public class Exit extends Tile {

  /**
   * Exit Tile Constructor.
   */
  public Exit() {
    super("exit");
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
    return 'e';
  }

}
