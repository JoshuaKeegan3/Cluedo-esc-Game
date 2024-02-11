package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import nz.ac.vuw.ecs.swen225.gp21.domain.TileColor;

/**
 * Side class shows player side statistics.
 * - keys left
 * - time
 * - inventory
 * - level
 * Creates Three Stats for each stat drawn.
 * Inventory is used to display keys collected
 *

 * @author Joel 300524008
 */
public class Side extends Popup {
  private final Game game;

  private final int level = 0;
  private List<TileColor> hand;

  private Stat levelDisplay = null;
  private Stat timeDisplay = null;
  private Stat chipsLeftStat = null;

  private JLabel messageLabel = new JLabel();
  private JPanel messagePanel = new JPanel();

  private Inventory inventory = null;
  protected String message = "No message";

  /**
   * Side constructor.
   * Sets up the layout and creates border around JPanel.
   *
   * @param parent - Parent of Side which is the mainPanel.
   * @param game - instance of game class to access setters and getters
   *
   */
  Side(JPanel parent, Game game) {
    messagePanel.add(messageLabel);
    messageLabel.setForeground(Color.yellow);
    messagePanel.setBackground(new Color(30, 30, 30));
    super.parent = parent;
    this.game = game;
    this.setLayout(new GridLayout(5, 0));
    update();
  }


  /**
   * Update method:
   * - Updates the size of the panel according to height and width or parent
   * - Updates the values being displayed in the side bar.
   */
  @Override
  public void update() {
    messageLabel.setText(game.message);
    setBackground(new Color(20, 20, 20));
    for (Component child : this.getComponents()) {
      if (child instanceof JPanel) {
        ((JPanel) child).setBorder(new LineBorder(new Color(20, 20, 20)));
      }
    }
    int height = (int) (parent.getHeight() * 0.868);
    int width = (int) (parent.getWidth() * 0.3);
    Dimension size = new Dimension(width, height);
    this.setPreferredSize(size);
    drawStats();

  }

  /**
   * drawStats Method.
   *
   */
  private void drawStats() {
    if (levelDisplay == null) {    //Initialize statistic fields when required.
      levelDisplay = new Stat("LEVEL", game);
      timeDisplay = new Stat("TIME", game);
      chipsLeftStat = new Stat("CHIPS LEFT", game);
      inventory = new Inventory();
      this.add(levelDisplay);
      this.add(timeDisplay);
      this.add(chipsLeftStat);
      this.add(messagePanel);
      this.add(inventory);

    }

    //Set amount in data fields
    levelDisplay.setAmount(game.getLevel());
    timeDisplay.setAmount(game.getTime());
    chipsLeftStat.setAmount(game.getRemainingKeys());
    hand = new ArrayList<>(game.getHand());
    for (Component component : this.getComponents()) {
      if (component instanceof JPanel) {
        if (component instanceof Stat) {
          ((Stat) component).update();
        } else if (component instanceof Inventory) {
          ((Inventory) component).update();
        }
      }
    }
  }

  /**
   * Inventory class extending JPanel.
   * Update method to redraw inventory each time it is required
   */
  public class Inventory extends JPanel {
    int keys = 0;

    Inventory() {
      keys = -1;
    }
    /**
     * Update method.
     * Clears the inventory and creates the grids for each key slot.
     * Draws keys as required.
     */

    private void update() {
      hand = new ArrayList<>(game.getHand());
      if (keys == hand.size()) {
        return;
      }
      setBackground(new Color(20, 20, 20));
      removeAll();
      Graphics g = getGraphics();
      setLayout(new GridLayout(2, 4));
      for (TileColor colour : hand) {
        try {
          BufferedImage image = null;
          image = ImageIO.read(new File("images/key_"
              + (colour.name()).toLowerCase() + ".png"));
          BufferedImage finalImage = image;
          JPanel keys = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              g.drawImage(finalImage, 0, 0, inventory.getWidth() / 4,
                  inventory.getHeight() / 2, null);

            }
          };
          inventory.add(keys);
        } catch (IOException e) {
          e.printStackTrace();
        }


      }
      drawFillers();
      keys = hand.size();
    }
  }

  /**
   * Draw Fillers method.
   * Draws a filler square for each square that isn't taken up.
   */
  protected void drawFillers() {
    for (int i = hand.size(); i < 8; i++) {
      JPanel placeholder = new JPanel();
      placeholder.setPreferredSize(new Dimension(inventory.getWidth() / 4, inventory.getHeight()));
      placeholder.setBorder(new LineBorder(new Color(20, 20, 20)));
      placeholder.setBackground(new Color(30, 30, 30));
      inventory.add(placeholder);
    }
    repaint();
    revalidate();
  }


  /**
   * Stat class.
   * Displays time, keys left and level
   * Each Stat has a name(label) and an amount.
   */
  public static class Stat extends JPanel {
    private final String name;
    private int amount;
    private JLabel title;
    JLabel amountLabel;
    private final Game game;

    /**
     * Stat constructor.
     *
     * @param name - name of the Stat
     */
    public Stat(String name, Game game) {
      this.name = name;
      this.game = game;
      initialise();

    }

    protected void setAmount(int amount) {

      this.amount = amount;
    }

    private void initialise() {
      setLayout(new GridLayout(2, 0));
      setBackground(new Color(30, 30, 30));
      title = new JLabel(this.name);
      this.add(title);
      amountLabel = new JLabel(String.valueOf(this.amount));
      title.setHorizontalAlignment((int) CENTER_ALIGNMENT);
      amountLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
      this.add(amountLabel);
      amountLabel.setVerticalAlignment(SwingConstants.TOP);
      title.setForeground(new Color(200, 200, 200));
    }

    /**
     * Update method.
     * Sets the amount and label if changed
     */
    private void update() {
      title.setText(name);
      amountLabel.setText(String.valueOf(this.amount));
      amountLabel.setForeground(getColour());
    }

    private Color getColour() {
      if (name.equals("TIME")) {
        double ratio = ((double) game.getTime() / (double) game.end);
        return new Color((int) (255 * ratio), (int) (255. * (1.0 - ratio)), 0);
      }
      return new Color(200, 200, 200);
    }

  }
}
