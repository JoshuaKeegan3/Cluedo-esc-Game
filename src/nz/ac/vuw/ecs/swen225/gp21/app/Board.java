package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.renderer.DomainRenderer;
import nz.ac.vuw.ecs.swen225.gp21.renderer.Renderer;


/**
 * Board class.
 * Board class represents the board representing the game-play
 * Has reference to renderer for drawing
 * Changes size based on size of parent
 *
 * @author Joel 300524008
 */
public class Board extends Popup {
  private final Popup parent;
  private int height;
  private Renderer renderer = null;
  private Domain domain;
  private Dimension size;
  private int width;
  private DomainRenderer domainRenderer;

  /**
   * Constructor instantiate local copy of parent and renderer.
   *
   * @param parent - Parent is MainWindow
   * @param domain - Domain passed in to set renderer
   */
  Board(Popup parent, Domain domain) {
    this.domain = domain;
    this.parent = parent;
    update();

  }

  /**
   * Update method.
   * called to update contents and size of the board
   * Updates the height according to size of parent
   * Calls draw method in renderer to update board status
   */
  @Override
  public void update() {
    //height=(int) (parent.getHeight()*0.857);
    height = 450;
    width = height;
    size = new Dimension(width, height);
    this.setPreferredSize(size);
    this.setBorder(BorderFactory.createRaisedBevelBorder());

    if (renderer == null) {
      this.renderer = new Renderer();
      renderer.setFps(60);
      renderer.setPreferredSize(size);
      domainRenderer = new DomainRenderer(renderer);
      this.add(renderer);
      domainRenderer.setDomain(domain);
    }


  }


  /**
   * setDomain method.
   * Updates domain field and renderers domain
   *
   * @param domain - new Domain
   */
  protected void setDomain(Domain domain) {
    this.domain = domain;
    if (domainRenderer != null) {
      domainRenderer.setDomain(domain);
    }
  }


}
