package nz.ac.vuw.ecs.swen225.gp21.renderer;

/**
 * Used within Renderer to render a portion of the scene.
 */
class Camera extends SceneItem {
	private int width = 128;
	private int height = 128;
	private double scale = 1;
	private SceneItem following;

	@Override
	public void update(long deltaTime) {
		super.update(deltaTime);

		// If following a SceneItem, set it's position as target.
		if (following != null) {
			setTargetPosition(following);
		}
	}

	/**
	 * Makes the Camera follow the given SceneItem.
	 * @param sceneItem The SceneItem to follow.
	 */
	public void follow(SceneItem sceneItem) {
		following = sceneItem;
	}

	/**
	 * Sets the view size of the camera. Useful for zooming in.
	 * E.g. if given 2 width and 2 height, the camera will only display 4 pixels at it's location, but if given
	 * 100 width and 100 height, the camera will display a lot more.
	 * @param width Width of the camera size.
	 * @param height Height of the camera size.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getScale() {
		return scale;
	}
}
