package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Thrown if chap is on a tile he shouldn't be on.

 * @author Joshua Keegan 30052483 
 */
public class ChapOnInvalidTileException extends Exception {

  private static final long serialVersionUID = 1L;
  /**
   * Constructor.

   * @param s - exception message
   */
  public ChapOnInvalidTileException(String s) {
    super(s);
  }
  
  /**
   * Constructor.
   */
  public ChapOnInvalidTileException() {
    super();
  }
}
