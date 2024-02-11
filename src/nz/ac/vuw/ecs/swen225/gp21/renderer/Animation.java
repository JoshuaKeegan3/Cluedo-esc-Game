package nz.ac.vuw.ecs.swen225.gp21.renderer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Animation {
	private String name;
	private List<AnimationFrame> frames;
	private boolean loop = false;

	private AnimationFrame currentFrame;
	private int currentFrameIndex;
	private final Timer frameTimer = new Timer(0, e -> nextFrame());
	private boolean playing;

	public Animation(String name) {
		this.name = name;
		frameTimer.setRepeats(loop);
	}

	private void nextFrame() {
		++currentFrameIndex;
		if (currentFrameIndex == frames.size()) {
			if (loop) {
				currentFrameIndex = 0;
			} else {
				reset();
				return;
			}
		}

		currentFrame = frames.get(currentFrameIndex);
		frameTimer.setInitialDelay(currentFrame.duration);
		frameTimer.start();
	}

	public void start() {
		currentFrameIndex = 0;
		currentFrame = frames.get(currentFrameIndex);
		frameTimer.setInitialDelay(currentFrame.duration);
		frameTimer.start();
		playing = true;
	}

	public void reset() {
		frameTimer.stop();
		currentFrameIndex = 0;
		currentFrame = frames.get(currentFrameIndex);
		playing = false;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void addFrame(AnimationFrame frame) {
		frames.add(frame);
	}

	public void insertFrame(int index, AnimationFrame frame) {
		frames.add(index, frame);
	}

	public void removeFrame(AnimationFrame frame) {
		frames.remove(frame);
	}

	public void doLoop(boolean loop) {
		this.loop = loop;
	}

	public String getName() {
		return name;
	}

	public List<AnimationFrame> getFrames() {
		return Collections.unmodifiableList(frames);
	}

	public AnimationFrame getCurrentFrame() {
		return currentFrame;
	}

	public static class Builder {
		private Animation animation;
		private boolean loop = false;
		private List<AnimationFrame> frames = new ArrayList<>();

		public Builder(String name) {
			this.animation = new Animation(name);
		}

		/**
		 * Constructs the built Animation.
		 * @return The built Animation.
		 */
		public Animation build() {
			animation.frames = frames;
			animation.doLoop(loop);
			return animation;
		}

		/**
		 * Adds a frame to the constructed Animation.
		 * @param image The BufferedImage to use for the frame.
		 * @param duration The duration of the frame in milliseconds.
		 * @return The same Builder.
		 */
		public Animation.Builder addFrame(BufferedImage image, int duration) {
			AnimationFrame frame = new AnimationFrame(image, duration);
			this.frames.add(frame);
			return this;
		}

		/**
		 * Makes the Animation loop.
		 * @return The same Builder.
		 */
		public Animation.Builder doLoop() {
			this.loop = true;
			return this;
		}
	}
}
