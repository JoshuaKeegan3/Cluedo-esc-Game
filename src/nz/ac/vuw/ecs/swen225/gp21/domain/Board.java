package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Board class.

 * @author keeganjosh 30023483
 *
 */
public class Board {
  private Tile[] board;
  private int height;
  private int width;
  private int size;
  private Set<Listener> boardChangedEventListeners = new HashSet<>();
  

  Board(Tile[] board, int width, int height) {
    this.board = board;
    this.height = height;
    this.width = width;
    this.size = width * height;
  }
  
  /**
   * Returns a tile at an x,y.

   * @param x - x coordinate of the tile
   * @param y - y coordinate of the tile
   * @return tile on the board at that position
   */
  public Tile getTile(int x, int y) {
    return board[y * width + x];
  }
  
  /**
   * Gets a tile at a location.

   * @param newLoc - location
   * @return tile at location 
   */
  public Tile getTile(Location newLoc) {
    return getTile(newLoc.getX(), newLoc.getY());
  }
  
  /**
   * Gets the height of the board.

   * @return height of the board
   */
  public int getHeight() {
    return height;
  }
  
  /**
   * Gets the width of the board.

   * @return width of the board
   */
  public int getWidth() {
    return width;
  }


  /**
   * Changes a tile at a location to another tile.

   * @param location - tile to change
   * @param standingOn - tile to change to
   */
  public void setTile(Location location, Tile standingOn) {
    boolean doBoardChangedEvent = getTile(location) != standingOn;

    board[location.getY() * width + location.getX()] = standingOn;

    if (doBoardChangedEvent) {
      for (Listener listener : boardChangedEventListeners) {
        listener.invoke(location);
      }
    }
  }

  public void connectBoardChangedEvent(Object object, Method method) {
    Listener listener = new Listener(object, method);
    boardChangedEventListeners.add(listener);
  }

  public void disconnectBoardChangedEvent(Object object, Method method) {
    Listener listener = new Listener(object, method);
    boardChangedEventListeners.remove(listener);
  }
  
  /**
   * Gets the size of the board.

   * @return the length of the board array
   */
  public int getSize() {
    return size;
  }

  /**
   * Gets the board.

   * @return board array
   */
  public Tile[] getBoard() {
    return board;
  }

  /**
   * Maps a listener object to one of it's methods.
   * It can invoke the method on that object by calling invoke().
   */
  private static class Listener {
    public final Object object;
    public final Method method;

    public Listener(Object object, Method method) {
      if (object == null) {
    	  throw new IllegalArgumentException("'object' argument cannot be null");
      }
      if (method == null) {
    	  throw new IllegalArgumentException("'method' argument cannot be null");
      }
      this.object = object;
      this.method = method;
      method.setAccessible(true);
    }

    /**
     * Invokes the method on the listener object with a given argument.
     */
    public void invoke(Object arg) {
      try {
        method.invoke(object, arg);
      } catch (InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
