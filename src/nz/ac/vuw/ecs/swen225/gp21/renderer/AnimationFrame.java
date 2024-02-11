package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.image.BufferedImage;

/**
 * An AnimationFrame simply contains an image for a frame, and the length of time of that frame in milliseconds.
 * TODO: If this project is moved to JDK 14 or above, turn this class into a record.
 */
class AnimationFrame {
	public final BufferedImage image;
	public final int duration;

	/**
	 * @param image The BufferedImage to use for the frame.
	 * @param duration The duration of the frame in milliseconds.
	 */
	public AnimationFrame(BufferedImage image, int duration) {
		this.image = image;
		this.duration = duration;
	}
}
