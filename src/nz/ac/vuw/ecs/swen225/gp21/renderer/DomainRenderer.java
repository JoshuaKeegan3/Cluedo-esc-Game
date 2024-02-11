package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Board;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.domain.Location;
import nz.ac.vuw.ecs.swen225.gp21.domain.Tile;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Chap;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Door;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Exit;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Info;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Key;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Lock;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Treasure;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.Wall;


/**
 * Uses Renderer to render a given Domain. Must supply a Domain by calling setDomain() 
 * for this to render anything.
 */
public class DomainRenderer {
  private BufferedImage chapUp;
  private BufferedImage chapUpWalk1;
  private BufferedImage chapUpWalk2;
  private BufferedImage chapDown;
  private BufferedImage chapDownWalk1;
  private BufferedImage chapDownWalk2;
  private BufferedImage chapLeft;
  private BufferedImage chapLeftWalk1;
  private BufferedImage chapLeftWalk2;
  private BufferedImage chapRight;
  private BufferedImage chapRightWalk1;
  private BufferedImage chapRightWalk2;
  private BufferedImage exit;
  private BufferedImage exitLock;
  private BufferedImage floor;
  private BufferedImage info;
  private BufferedImage keyRed;
  private BufferedImage keyRedSpin1;
  private BufferedImage keyRedSpin2;
  private BufferedImage keyRedSpin3;
  private BufferedImage keyRedSpin4;
  private BufferedImage keyRedSpin5;
  private BufferedImage keyGreen;
  private BufferedImage keyGreenSpin1;
  private BufferedImage keyGreenSpin2;
  private BufferedImage keyGreenSpin3;
  private BufferedImage keyGreenSpin4;
  private BufferedImage keyGreenSpin5;
  private BufferedImage keyBlue;
  private BufferedImage keyBlueSpin1;
  private BufferedImage keyBlueSpin2;
  private BufferedImage keyBlueSpin3;
  private BufferedImage keyBlueSpin4;
  private BufferedImage keyBlueSpin5;
  private BufferedImage lockedDoorRed;
  private BufferedImage lockedDoorGreen;
  private BufferedImage lockedDoorBlue;
  private BufferedImage treasure;
  private BufferedImage wall;

  private Clip walkSound;
  private Clip keyPickupSound;
  private Clip unlockDoorSound;
  private Clip treasurePickupSound;
  private Clip exitLockPickupSound;

  private Domain domain;
  private final Renderer renderer;

  private Location previousChapLocation;

  private final AnimatedSprite chapSprite = new AnimatedSprite();

  // Special renderables are made from tiles that do something, like keys or doors (excludes Chap).
  private final Map<Location, IRenderable> specialRenderables = new HashMap<>();

  /**
   * Domain Renderer.
   *
   * @param renderer The Renderer that DomainRender will use to draw the Domain.
   */
  public DomainRenderer(Renderer renderer) {
    this.renderer = renderer;

    loadImages();
    loadAudio();

    // Camera setup.
    renderer.getCamera().follow(chapSprite);
    renderer.getCamera().setScale(2);
    renderer.getCamera().setLerpPosition(true);
    renderer.getCamera().setLerpSpeed(.045f);

    // Chap setup.
    chapSprite.setDefaultImage(chapDown);
    chapSprite.setDrawDepth(1);  // So chap gets drawn above all other sprites.
    chapSprite.setLerpPosition(true);
    chapSprite.addAnimation(new Animation.Builder("walk_up")
        .addFrame(chapUpWalk1, 50)
        .addFrame(chapUp, 50)
        .addFrame(chapUpWalk2, 50)
        .build()
    );
    chapSprite.addAnimation(new Animation.Builder("walk_down")
        .addFrame(chapDownWalk1, 50)
        .addFrame(chapDown, 50)
        .addFrame(chapDownWalk2, 50)
        .build()
    );
    chapSprite.addAnimation(new Animation.Builder("walk_left")
        .addFrame(chapLeftWalk1, 50)
        .addFrame(chapLeft, 50)
        .addFrame(chapLeftWalk2, 50)
        .build()
    );
    chapSprite.addAnimation(new Animation.Builder("walk_right")
        .addFrame(chapRightWalk1, 50)
        .addFrame(chapRight, 50)
        .addFrame(chapRightWalk2, 50)
        .build()
    );
  }

  /**
   * Sets the Domain to render. 
   *
   * @param domain The Domain to render.
   */
  public void setDomain(Domain domain) {
    try {
      // Disconnect the previous board's changed event from onBoardChanged.
      if (this.domain != null) {
        this.domain.getBoard().disconnectBoardChangedEvent(this, 
            getClass().getDeclaredMethod("onBoardChanged", Location.class));
      }
      // Connect the new board's changed event to onBoardChanged.
      domain.getBoard().connectBoardChangedEvent(this, 
          getClass().getDeclaredMethod("onBoardChanged", Location.class));
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    this.domain = domain;

    renderer.unregisterAllRenderables();

    // Make Sprites from all the Tiles in the Domain's Board.
    Board board = domain.getBoard();
    Sprite boardSprite = new Sprite(board.getWidth() * 32, board.getHeight() * 32, false);
    boardSprite.setDrawDepth(-1);  // So the board is always drawn behind other sprites.
    renderer.registerRenderable(boardSprite);
    for (int y = 0; y < board.getHeight(); y++) {
      for (int x = 0; x < board.getWidth(); x++) {
        // Draw floor onto boardSprite first.
        Graphics boardSpriteGraphics = boardSprite.getImage().getGraphics();
        boardSpriteGraphics.drawImage(floor, x * 32, y * 32, null);

        // Make sprite.
        Tile tile = board.getTile(x, y);
        if (tile instanceof Chap) {
          previousChapLocation = ((Chap) tile).getLocation();
          chapSprite.setTargetPosition(x * 32, y * 32);
          renderer.registerRenderable(chapSprite);
        } else if (tile instanceof Lock) {
          Sprite tileSprite = new Sprite(exitLock);
          tileSprite.setName("exitLock");
          tileSprite.setTargetPosition(x * 32, y * 32);
          specialRenderables.put(new Location(x, y), tileSprite);
          renderer.registerRenderable(tileSprite);
        } else if (tile instanceof Key) {
          switch (((Key) tile).getColor()) {
            case RED:
              AnimatedSprite redKeySprite = new AnimatedSprite();
              redKeySprite.setName("keyRed");
              redKeySprite.addAnimation(new Animation.Builder("spin")
                  .addFrame(keyRed, 400)
                  .addFrame(keyRedSpin1, 175)
                  .addFrame(keyRedSpin2, 140)
                  .addFrame(keyRedSpin3, 100)
                  .addFrame(keyRedSpin4, 75)
                  .addFrame(keyRedSpin3, 100)
                  .addFrame(keyRedSpin2, 125)
                  .addFrame(keyRedSpin5, 150)
                  .doLoop()
                  .build()
              );
              specialRenderables.put(new Location(x, y), redKeySprite);
              renderer.registerRenderable(redKeySprite);
              redKeySprite.setTargetPosition(x * 32, y * 32);
              redKeySprite.playAnimation("spin");
              break;
            case GREEN:
              AnimatedSprite greenKeySprite = new AnimatedSprite();
              greenKeySprite.setName("keyGreen");
              greenKeySprite.addAnimation(new Animation.Builder("spin")
                  .addFrame(keyGreen, 400)
                  .addFrame(keyGreenSpin1, 175)
                  .addFrame(keyGreenSpin2, 140)
                  .addFrame(keyGreenSpin3, 100)
                  .addFrame(keyGreenSpin4, 75)
                  .addFrame(keyGreenSpin3, 100)
                  .addFrame(keyGreenSpin2, 125)
                  .addFrame(keyGreenSpin5, 150)
                  .doLoop()
                  .build()
              );
              specialRenderables.put(new Location(x, y), greenKeySprite);
              renderer.registerRenderable(greenKeySprite);
              greenKeySprite.setTargetPosition(x * 32, y * 32);
              greenKeySprite.playAnimation("spin");
              break;
            case BLUE:
              AnimatedSprite blueKeySprite = new AnimatedSprite();
              blueKeySprite.setName("keyBlue");
              blueKeySprite.addAnimation(new Animation.Builder("spin")
                  .addFrame(keyBlue, 400)
                  .addFrame(keyBlueSpin1, 175)
                  .addFrame(keyBlueSpin2, 140)
                  .addFrame(keyBlueSpin3, 100)
                  .addFrame(keyBlueSpin4, 75)
                  .addFrame(keyBlueSpin3, 100)
                  .addFrame(keyBlueSpin2, 125)
                  .addFrame(keyBlueSpin5, 150)
                  .doLoop()
                  .build()
              );
              specialRenderables.put(new Location(x, y), blueKeySprite);
              renderer.registerRenderable(blueKeySprite);
              blueKeySprite.setTargetPosition(x * 32, y * 32);
              blueKeySprite.playAnimation("spin");
              break;
            default:
              throw new IllegalStateException("Unexpected value: " + ((Key) tile).getColor());
          }
        } else if (tile instanceof Door) {
          Sprite tileSprite;
          switch (((Door) tile).getColor()) {
            case RED:
              tileSprite = new Sprite(lockedDoorRed);
              tileSprite.setName("lockedDoorRed");
              break;
            case GREEN:
              tileSprite = new Sprite(lockedDoorGreen);
              tileSprite.setName("lockedDoorGreen");
              break;
            case BLUE:
              tileSprite = new Sprite(lockedDoorBlue);
              tileSprite.setName("lockedDoorBlue");
              break;
            default:
              throw new IllegalStateException("Unexpected value: " + ((Door) tile).getColor());
          }
          tileSprite.setTargetPosition(x * 32, y * 32);
          specialRenderables.put(new Location(x, y), tileSprite);
          renderer.registerRenderable(tileSprite);
        } else if (tile instanceof Treasure) {
          Sprite tileSprite = new Sprite(treasure);
          tileSprite.setName("treasure");
          tileSprite.setTargetPosition(x * 32, y * 32);
          specialRenderables.put(new Location(x, y), tileSprite);
          renderer.registerRenderable(tileSprite);
        } else {
          // The tile is static, so can be drawn onto the static board sprite.
          if (tile instanceof Exit) {
            boardSpriteGraphics.drawImage(exit, x * 32, y * 32, null);
          } else if (tile instanceof Info) {
            boardSpriteGraphics.drawImage(info, x * 32, y * 32, null);
          } else if (tile instanceof Wall) {
            boardSpriteGraphics.drawImage(wall, x * 32, y * 32, null);
          }
        }
      }
    }
  }

  /**
   * Loads images required for rendering.
   */
  private void loadImages() {
    try {
      chapUp = ImageIO.read(new File("images/chap_up.png"));
      chapUpWalk1 = ImageIO.read(new File("images/chap_up_walk_1.png"));
      chapUpWalk2 = ImageIO.read(new File("images/chap_up_walk_2.png"));
      chapDown = ImageIO.read(new File("images/chap_down.png"));
      chapDownWalk1 = ImageIO.read(new File("images/chap_down_walk_1.png"));
      chapDownWalk2 = ImageIO.read(new File("images/chap_down_walk_2.png"));
      chapLeft = ImageIO.read(new File("images/chap_left.png"));
      chapLeftWalk1 = ImageIO.read(new File("images/chap_left_walk_1.png"));
      chapLeftWalk2 = ImageIO.read(new File("images/chap_left_walk_2.png"));
      chapRight = ImageIO.read(new File("images/chap_right.png"));
      chapRightWalk1 = ImageIO.read(new File("images/chap_right_walk_1.png"));
      chapRightWalk2 = ImageIO.read(new File("images/chap_right_walk_2.png"));
      exit = ImageIO.read(new File("images/exit.png"));
      exitLock = ImageIO.read(new File("images/exit_lock.png"));
      floor = ImageIO.read(new File("images/floor.png"));
      info = ImageIO.read(new File("images/info.png"));
      keyRed = ImageIO.read(new File("images/key_red.png"));
      keyRedSpin1 = ImageIO.read(new File("images/key_red_spin_1.png"));
      keyRedSpin2 = ImageIO.read(new File("images/key_red_spin_2.png"));
      keyRedSpin3 = ImageIO.read(new File("images/key_red_spin_3.png"));
      keyRedSpin4 = ImageIO.read(new File("images/key_red_spin_4.png"));
      keyRedSpin5 = ImageIO.read(new File("images/key_red_spin_5.png"));
      keyGreen = ImageIO.read(new File("images/key_green.png"));
      keyGreenSpin1 = ImageIO.read(new File("images/key_green_spin_1.png"));
      keyGreenSpin2 = ImageIO.read(new File("images/key_green_spin_2.png"));
      keyGreenSpin3 = ImageIO.read(new File("images/key_green_spin_3.png"));
      keyGreenSpin4 = ImageIO.read(new File("images/key_green_spin_4.png"));
      keyGreenSpin5 = ImageIO.read(new File("images/key_green_spin_5.png"));
      keyBlue = ImageIO.read(new File("images/key_blue.png"));
      keyBlueSpin1 = ImageIO.read(new File("images/key_blue_spin_1.png"));
      keyBlueSpin2 = ImageIO.read(new File("images/key_blue_spin_2.png"));
      keyBlueSpin3 = ImageIO.read(new File("images/key_blue_spin_3.png"));
      keyBlueSpin4 = ImageIO.read(new File("images/key_blue_spin_4.png"));
      keyBlueSpin5 = ImageIO.read(new File("images/key_blue_spin_5.png"));
      lockedDoorRed = ImageIO.read(new File("images/locked_door_red.png"));
      lockedDoorGreen = ImageIO.read(new File("images/locked_door_green.png"));
      lockedDoorBlue = ImageIO.read(new File("images/locked_door_blue.png"));
      treasure = ImageIO.read(new File("images/treasure.png"));
      wall = ImageIO.read(new File("images/wall.png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Loads audio used during rendering.
   */
  private void loadAudio() {
    try {
      AudioInputStream walkSoundStream = 
          AudioSystem.getAudioInputStream(new File("audio/walk.wav"));
      walkSound = AudioSystem.getClip();
      walkSound.open(walkSoundStream);
      AudioInputStream keyPickupSoundStream = 
          AudioSystem.getAudioInputStream(new File("audio/key_pickup.wav"));
      keyPickupSound = AudioSystem.getClip();
      keyPickupSound.open(keyPickupSoundStream);
      AudioInputStream unlockDoorSoundStream = 
          AudioSystem.getAudioInputStream(new File("audio/door_unlock.wav"));
      unlockDoorSound = AudioSystem.getClip();
      unlockDoorSound.open(unlockDoorSoundStream);
      AudioInputStream treasurePickupSoundStream = 
          AudioSystem.getAudioInputStream(new File("audio/treasure_pickup.wav"));
      treasurePickupSound = AudioSystem.getClip();
      treasurePickupSound.open(treasurePickupSoundStream);
      AudioInputStream exitLockPickupSoundStream = 
          AudioSystem.getAudioInputStream(new File("audio/exit_lock_pickup.wav"));
      exitLockPickupSound = AudioSystem.getClip();
      exitLockPickupSound.open(exitLockPickupSoundStream);
    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * DO NOT CALL THIS.
   * Called by the domain's board when it fires a changed event.
   *
   * @param location The location on the board at which the change occurred.
   */
  private void onBoardChanged(Location location) {
    Board board = domain.getBoard();
    Tile newTile = board.getTile(location);
    if (newTile instanceof Chap) {
      // Test for which direction chap moved, and play an animation based on that direction.
      int prevX = previousChapLocation.getX();
      int prevY = previousChapLocation.getY();
      int newX = location.getX();
      int newY = location.getY();
      if (prevX - newX == 1) {  // Moved left.
        chapSprite.playAnimation("walk_left");
        chapSprite.setDefaultImage(chapLeft);
        chapSprite.setTargetPosition(newX * 32, newY * 32);
        walkSound.setMicrosecondPosition(0);
        walkSound.start();
      } else if (prevX - newX == -1) {  // Moved right.
        chapSprite.playAnimation("walk_right");
        chapSprite.setDefaultImage(chapRight);
        chapSprite.setTargetPosition(newX * 32, newY * 32);
        walkSound.setMicrosecondPosition(0);
        walkSound.start();
      } else if (prevY - newY == 1) {  // Moved up.
        chapSprite.playAnimation("walk_up");
        chapSprite.setDefaultImage(chapUp);
        chapSprite.setTargetPosition(newX * 32, newY * 32);
        walkSound.setMicrosecondPosition(0);
        walkSound.start();
      } else if (prevY - newY == -1) {  // Moved down.
        chapSprite.playAnimation("walk_down");
        chapSprite.setDefaultImage(chapDown);
        chapSprite.setTargetPosition(newX * 32, newY * 32);
        walkSound.setMicrosecondPosition(0);
        walkSound.start();
      }
      previousChapLocation = location;

      // Destroy any renderable at Chaps location, because it must have been
      // interacted with by Chap.
      if (specialRenderables.containsKey(location)) {
        IRenderable renderable = specialRenderables.get(location);

        // Play audio.
        if (renderable instanceof AnimatedSprite) {
          String name = ((AnimatedSprite) renderable).getName();
          if (name.equals("keyRed") || name.equals("keyGreen") || name.equals("keyBlue")) {
            keyPickupSound.setMicrosecondPosition(0);
            keyPickupSound.start();
          }
        } else if (renderable instanceof Sprite) {
          String name = ((Sprite) renderable).getName();
          switch (name) {
            case "lockedDoorRed":
            case "lockedDoorGreen":
            case "lockedDoorBlue":
              unlockDoorSound.setMicrosecondPosition(0);
              unlockDoorSound.start();
              break;
            case "treasure":
              treasurePickupSound.setMicrosecondPosition(0);
              treasurePickupSound.start();
              break;
            case "exitLock":
              exitLockPickupSound.setMicrosecondPosition(0);
              exitLockPickupSound.start();
              break;
            default:
              break;
          }
        }

        // Destroy.
        renderer.unregisterRenderable(renderable);
        specialRenderables.remove(location);
      }
    }
  }
}
