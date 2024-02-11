package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

class Sprite extends SceneItem implements IRenderable {
	private BufferedImage image;
	private int depth;

	public Sprite(int width, int height, boolean transparency) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();
		if (transparency) {
			this.image = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		} else {
			this.image = config.createCompatibleImage(width, height, Transparency.OPAQUE);
		}
	}

	public Sprite(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void render(Graphics g, int deltaTime) {
		g.drawImage(image, xPos, yPos, null);
	}

	@Override
	public int getDrawDepth() {
		return depth;
	}

	public void setDrawDepth(int depth) {
		this.depth = depth;
	}

	public BufferedImage getImage() {
		return image;
	}
}
