/**
 * 
 */
package nz.ac.vuw.ecs.swen225.gp21.app;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

/**
 * @author Joel 300524008
 *	
 *	MainWindow is the main window that stores Board and side
 *	Updated every time the screen is resized or game is updated
 *	Extends Pop-up abstract class
 */
public class MainWindow extends Popup{
	JFrame frame;
	/**
	 * @param frame - frame for whole application
	 * @param window - Dimension to set original window size
	 */
	public MainWindow(JFrame frame, Dimension window) {
		this.frame=frame;
		this.setLayout(new FlowLayout());
		this.setPreferredSize(window);
		update();
	}
	

	/**
	 * Update method is called every time panel needs to be updated
	 * Sets the size and border
	 * Goes through children and calls update method on them
	 */
    @Override
	public void update(){
    	// Set dimension according to frame size
    	int currHeight=frame.getHeight();
        int currWidth=frame.getHeight();
        this.setLayout(new FlowLayout());
        this.setBorder(new EmptyBorder((int) (currHeight/13.46)/2, (int) (currWidth*0.037), (int) (currHeight/30), (int) (currWidth*0.037)));
    	
        // Update children
        for(Component child: this.getComponents()){	//Update children
        	if(child instanceof Popup) {
        		((Popup)child).update();
        	}
        	
        }
        this.repaint();
        this.revalidate();
    }
}
