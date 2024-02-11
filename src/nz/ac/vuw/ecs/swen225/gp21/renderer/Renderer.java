package nz.ac.vuw.ecs.swen225.gp21.renderer;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders registered IRenderables periodically.
 * TODO:
 * - Display treasures collected
 */
public class Renderer extends JPanel {

  private final Timer frameTimer;
  private final Set<IRenderable> renderables = new HashSet<>();

  private long lastFrameTime;
  private int frameDeltaTime;    // The time since the last render, in milliseconds.

  private int fps = 60;
  private Camera camera = new Camera();

  public Renderer() {
    frameTimer = new Timer(1000 / fps, e -> {
      long time = e.getWhen();
      frameDeltaTime = (int) (time - lastFrameTime);
      lastFrameTime = time;
      render();
    });
    frameTimer.setRepeats(true);
    start();
  }

  /**
   * Repaints.
   */

  private void render() {
    repaint();
  }

  /**
   * Paints all IRenderables.
   *
   * @param g Graphics object.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Translate scene to the Camera.
    Graphics2D g2d = (Graphics2D) g;
    g2d.scale(camera.getScale(), camera.getScale());
    g2d.translate(-camera.getXPos() + (double) getWidth() / 2 / camera.getScale() - 16,
        -camera.getYPos() + (double) getHeight() / 2 / camera.getScale() - 16);

    // Make sure they are drawn according to their draw depths.
    List<IRenderable> drawOrder = renderables.stream()
        .sorted(Comparator.comparingInt(IRenderable::getDrawDepth))
        .collect(Collectors.toList());
    for (IRenderable renderable : drawOrder) {
      renderable.render(g, frameDeltaTime);
    }

    // Translate back to the origin.
    g2d.translate(camera.getXPos(), camera.getYPos());
    //g2d.translate(camera.getXPos() + camera.getWidth() / 2, camera.getYPos() + camera.getHeight() / 2);
  }


  public void registerRenderable(IRenderable renderable) {
    renderables.add(renderable);
  }

  public void unregisterRenderable(IRenderable renderable) {
    renderables.remove(renderable);
  }

  public void unregisterAllRenderables() {
    renderables.clear();
  }

  /**
   * Sets the frames-per-second. This determines how often the registered IRenderables are rendered.
   *
   * @param fps Frames-per-second to render the registered IRenderables.
   */
  public void setFps(int fps) {
    this.fps = fps;
    frameTimer.setDelay(1000 / fps);
  }

  /**
   * Sets the Camera that the Renderer will use.
   *
   * @param camera The Camera that the Renderer will use.
   */
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  /**
   * Returns the Renderer's Camera.
   *
   * @return The Renderer's Camera.
   */
  public Camera getCamera() {
    return camera;
  }

  /**
   * Starts rendering. Can be paused by calling stop().
   */
  public void start() {
    frameTimer.start();
  }

  /**
   * Stops rendering. Can be resumed by calling start().
   */
  public void stop() {
    frameTimer.stop();
  }

}
