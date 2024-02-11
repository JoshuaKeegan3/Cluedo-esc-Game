package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

class AnimatedSprite extends SceneItem implements IRenderable {
	private Set<Animation> animations = new HashSet<>();
	private Animation currentAnimation;
	private BufferedImage defaultImage;		// The image to use when currentAnimation isn't playing.
	private int depth;

	@Override
	public void render(Graphics g, int deltaTime) {
		if (currentAnimation == null || !currentAnimation.isPlaying()) {
			if (defaultImage != null) {
				g.drawImage(defaultImage, xPos, yPos, null);
			}
		} else {
			g.drawImage(currentAnimation.getCurrentFrame().image, xPos, yPos, null);
		}
	}

	@Override
	public int getDrawDepth() {
		return depth;
	}

	public void setDrawDepth(int depth) {
		this.depth = depth;
	}

	public void addAnimation(Animation animation) {
		animations.add(animation);
	}

	public void removeAnimation(Animation animation) {
		animations.remove(animation);
	}

	public void playAnimation(String name) {
		if (currentAnimation != null) {
			currentAnimation.reset();
		}
		currentAnimation = animations.stream()
				.filter(animation -> animation.getName().equals(name))
				.findFirst()
				.orElseThrow();
		currentAnimation.start();
	}

	public void setDefaultImage(BufferedImage image) {
		defaultImage = image;
	}
}
