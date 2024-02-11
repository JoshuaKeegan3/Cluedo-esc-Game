package nz.ac.vuw.ecs.swen225.gp21.app;

import javax.swing.*;

/**
 * Abstract class for all popup windows
 * @author joelt
 *
 */
public abstract class Popup extends JPanel {
    protected JPanel parent;
    /**
     * Abstract method for drawing
     * Might be deleted later
     */
    public abstract void update();
}
