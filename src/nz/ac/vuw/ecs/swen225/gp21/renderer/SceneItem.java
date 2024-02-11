package nz.ac.vuw.ecs.swen225.gp21.renderer;

import javax.swing.*;

abstract class SceneItem {
	protected String name = "";
	protected int xPos;
	protected int yPos;
	protected int lastXPos;
	protected int lastYPos;
	protected int speed;	// The real speed. Should not be set.
	protected boolean lerpPosition = false;		// When true, linearly interpolates to it's target position.
	protected double lerpSpeed = .1;
	protected int targetXPos;
	protected int targetYPos;

	private long lastUpdateTime;
	private int updateDeltaTime;		// The time since the last update, in milliseconds.

	private Timer updateTimer = new Timer(1000 / 60, e -> {
		long time = e.getWhen();
		updateDeltaTime = (int) (time - lastUpdateTime);
		lastUpdateTime = time;
		update(updateDeltaTime);
	});

	public SceneItem() {
		this.xPos = 0;
		this.yPos = 0;
		updateTimer.start();
	}

	public SceneItem(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		updateTimer.start();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setTargetPosition(int xPos, int yPos) {
		this.targetXPos = xPos;
		this.targetYPos = yPos;
	}

	public void setTargetPosition(SceneItem other) {
		this.targetXPos = other.xPos;
		this.targetYPos = other.yPos;
	}

	public void setLerpPosition(boolean lerpPosition) {
		this.lerpPosition = lerpPosition;
	}

	public void setLerpSpeed(float lerpSpeed) {
		this.lerpSpeed = lerpSpeed;
	}

	public double getSpeed() {
		return speed;
	}

	/**
	 * Updates the position of the SceneItem based on how much time has passed since the last update.
	 * @param deltaTime The time in milliseconds since this SceneItem was last updated.
	 */
	public void update(long deltaTime) {
		if (lerpPosition) {
			double lerpProgress = 1 - (1 / Math.max(lerpSpeed * deltaTime, 1.05));
			xPos += (targetXPos - xPos) * lerpProgress;
			yPos += (targetYPos - yPos) * lerpProgress;

			// Stick to target when close enough.
			if (Math.abs(xPos - targetXPos) < 10 * lerpSpeed) {
				xPos = targetXPos;
			}
			if (Math.abs(yPos - targetYPos) < 10 * lerpSpeed) {
				yPos = targetYPos;
			}
		} else {
			xPos = targetXPos;
			yPos = targetYPos;
		}

		speed = Math.abs(xPos - lastXPos) + Math.abs(yPos - lastYPos);

		lastXPos = xPos;
		lastYPos = yPos;
	}
}
