package com.udacity.game_dev.outbreak.Game.GameEntities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class used as parent of all game objects.
 */
public abstract class GameObject {

    protected Vector2 position;
    protected float width;
    protected float height;
    protected Color color;
    protected boolean visible;

    public GameObject() {
        position = new Vector2();
        visible = true;
    }

    public GameObject(float x, float y, float width, float height) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        visible = true;
    }

    /**
     * Draws object.
     * @param renderer  ShapeRenderer to draw with.
     */
    public abstract void render(ShapeRenderer renderer);

    /**
     * Gets x-coordinate.
     * @return x-coordinate.
     */
    public float getX() { return position.x; }

    /**
     * Gets y-coordinate.
     * @return y-coordinate.
     */
    public float getY() { return position.y; }

    /**
     * Gets width.
     * @return  Width.
     */
    public float getWidth() { return width; }

    /**
     * Gets height.
     * @return  Height.
     */
    public float getHeight() { return height; }

    /**
     * Returns whether object is visible.
     * @return  True if visible, false otherwise.
     */
    public boolean getIsVisible() { return visible; }

    /**
     * Tests whether the obejct is on screen given screen width and screen height.
     * @param screenWidth  Width of screen.
     * @param screenHeight Height of screen.
     * @return  Whether objects is within given screen bounds.
     */
    public abstract boolean onScreen(float screenWidth, float screenHeight);

    /**
     * Sets the objects position.
     * @param x  Desired x-coordinate.
     * @param y  Desired y-coordinate.
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    /**
     * Moves the object by a vector.
     * @param motion  Motion vector.
     */
    public void move(Vector2 motion) {
        position.add(motion);
    }
}
