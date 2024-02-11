package nz.ac.vuw.ecs.swen225.gp21.domain.tiles;

import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;
import nz.ac.vuw.ecs.swen225.gp21.domain.TileColor;

/**
 * A locked door.
 * Can only be walked on/through with a key of the same color.

 * @author Joshua Keegan 30052483
 */
public class Door extends Tile {

  private TileColor color;

  /**
   * Door Constructor.
   */

  public Door(TileColor color) {
    super("locked_door_green");
    this.color = color;
  }

  /**
   * Gets the color of the door.

   * @return color
   */
  public TileColor getColor() {
    return color;
  }

  @Override
  public boolean accessibleBy(Chap chap) {
    return chap.hasKey(this);
  }

  @Override
  public Tile stoodOnBy(Chap chap) {
    chap.removeKey(this);
    chap.setInformationShown(null);
    return new EmptyTile();
  }

  @Override
  public char getTextRepresentation() {
    return 'd';
  }

}
