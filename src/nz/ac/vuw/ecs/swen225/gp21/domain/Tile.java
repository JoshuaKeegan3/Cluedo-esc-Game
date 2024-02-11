package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Chap;

/**
 * Abstract Tile.
 * Used to fill a board with images and actions
 * A player can move onto tile

 * @author Joshua Keegan 30052483
 */
public abstract class Tile {

  protected BufferedImage img;
  protected Domain controller;

  /**
   * Tile Constructor.

   * @param type - type of tile, used to open image of tile
   */
  public Tile(String type) {
    try {
      img = ImageIO.read(new File("images/" + type + ".png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Checks if Chap can stand on this tile.

   * @param chap - player trying to stand on this
   * @return if the tile can be touched by chap
   */
  public abstract boolean accessibleBy(Chap chap);

  /**
   * applies the tiles@Override
<<<<<<< HEAD
<<<<<<< HEAD
   action if stood on by chap.
=======
 action if stood on by chap.
>>>>>>> fdf14aa76a9d90f5b5232f1ef3134c66069d693b
=======
 action if stood on by chap.
>>>>>>> 01f51878895251dd2b97d1f430a78363e32535c9

   * @param chap - player standing on this
   * @return The tile chap is moving to
   * @throws ChapOnInvalidTileException - if chap is on an invalid tile
   */
  public abstract Tile stoodOnBy(Chap chap) throws ChapOnInvalidTileException;

  /**
   * gets text representation of the tile.

   * @return text representation of the tile
   */
  public abstract char getTextRepresentation();

}
