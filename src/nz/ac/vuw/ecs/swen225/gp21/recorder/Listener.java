package nz.ac.vuw.ecs.swen225.gp21.recorder;
import nz.ac.vuw.ecs.swen225.gp21.app.App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Listens to key actions made by user to record their moves for xml file generation.
 *
 * Author: Johanah Gloria
 * ID: 300466914
 */
public class Listener {

    protected Recorder recorder;

    // encoded key numbers for user control
    private final int LEFT_KEY = 37;
    private final int RIGHT_KEY = 39;
    private final int UP_KEY = 38;
    private final int DOWN_KEY = 40;

    /**
     * Constructor for Listener class
     * @param r
     *      takes the current recorder object
     */
    public Listener(Recorder r) {
        recorder = r;
    }

    /**
     * Adds action and window listeners to GUI components in the App package.
     */
    public void addListeners(App app) {
        app.frame.addKeyListener(new KeyListener() {
            /**
             * Listens for exit or save game key actions, stops recording if following keys are pressed.
             * Listens for move key actions: a (left), d (right), w (up) and s (down) and adds them to the current game
             * document.
             * @param e
             *      takes key event object
             */
            @Override
            public void keyPressed(KeyEvent e) {
                // user moves
                if (e.getExtendedKeyCode() == UP_KEY) {
                    recorder.getChapRoot().addElement("move").addAttribute("dir", "up");
                }
                else if (e.getExtendedKeyCode() == LEFT_KEY) {
                    recorder.getChapRoot().addElement("move").addAttribute("dir", "left");
                }
                else if (e.getExtendedKeyCode() == DOWN_KEY) {
                    recorder.getChapRoot().addElement("move").addAttribute("dir", "down");
                }
                else if (e.getExtendedKeyCode() == RIGHT_KEY) {
                    recorder.getChapRoot().addElement("move").addAttribute("dir", "right");
                }
            }

            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
        });
    }
}
