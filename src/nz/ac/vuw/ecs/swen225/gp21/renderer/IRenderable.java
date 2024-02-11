package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.*;

interface IRenderable {
	/**
	 * Rendering logic for this renderable.
	 * @param g Graphics object to draw to.
	 * @param deltaTime The time since the last render.
	 */
	void render(Graphics g, int deltaTime);

	/**
	 * Determines the draw order of this renderable. A high depth will make this renderable draw above others with
	 * lower depths, and vice-versa.
	 */
	int getDrawDepth();
}
