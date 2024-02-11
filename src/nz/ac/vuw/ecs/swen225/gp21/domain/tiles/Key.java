package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;
import nz.ac.vuw.ecs.swen225.gp21.domain.TileColor;

/**
 * Key tile.
 * When stood on by chap it is collected.

 * @author Joshua Keegan 30052483
 */


public class Key extends Tile {
  private TileColor color;

  /**
   * Key constructor.

   * @param color - color of the door the key can open
   */
  public Key(TileColor color) {
    super("key_green");
    this.color = color;
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return true;
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.setInformationShown(null);
    chap.addKey(this);
    return new EmptyTile();
  }

  /**
   * Gets the color of the key.

   * @return color
   */
  public TileColor getColor() {
    return color;
  }

  @Override
  public char getTextRepresentation() {
    return 'k';
  }

}
