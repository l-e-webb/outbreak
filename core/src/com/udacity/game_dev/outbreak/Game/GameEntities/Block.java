package com.udacity.game_dev.outbreak.Game.GameEntities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.udacity.game_dev.outbreak.Utilities.Constants;

/**
 * Class representing blocks to be broken.
 */
public class Block extends GameObject {

    private int health;

    public Block(float x, float y, int startingHealth) {
        super(x, y, Constants.BLOCK_WIDTH, Constants.BLOCK_HEIGHT);
        init(startingHealth);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.rect(position.x, position.y, width, height);
        renderer.setColor(color);
        float offset = Constants.BLOCK_BORDER_WIDTH;
        renderer.rect(position.x + offset, position.y + offset, width - 2 * offset, height - 2 * offset);
    }

    /**
     * Updates the blocks color based on remaining health.
     */
    public void updateColor() {
        color = Constants.BLOCK_HEALTH_COLORS[health - 1];
    }

    /**
     * Respond to collision with ball.
     * @return  Whether block has been destroyed.
     */
    public boolean takeHit() {
        health--;
        if (health <= 0) {
            visible = false;
            return true;
        }
        else updateColor();
        return false;
    }

    @Override
    public boolean onScreen(float screenWidth, float screenHeight) {
        return (position.x < screenWidth &&
            position.x + width > 0 &&
            position.y < screenHeight &&
            position.y + height > 0
        );
    }

    /**
     * Initializes block with given health.
     * @param health  Desired initial health.
     */
    public void init(int health) {
        this.health = health;
        visible = true;
        updateColor();
    }
}
