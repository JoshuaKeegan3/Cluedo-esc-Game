package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;

/**
 * shows chap information.

 * @author Joshua Keegan 30052483
 */
public class Info extends Tile {
  private final String text;

  /**
   * Info tile Constructor.

   * @param text - information contained
   */
  public Info(String text) {
    super("info");
    this.text = text;
  }


  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.setInformationShown(text);
    return this;
  }


  @Override
  public char getTextRepresentation() {
    return 'i';
  }

}
