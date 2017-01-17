package com.udacity.game_dev.outbreak.Game.GameEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.udacity.game_dev.outbreak.Utilities.Constants;

/**
 * Class representing the player's movable paddle.
 */
public class Paddle extends GameObject {

    public Paddle(float x, float y) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
    }

    /**
     * Update the position fo the paddle based on user input and time since prior frame.
     * @param delta  Time since prior frame.
     * @param screenWidth  Width of screen, used to stop paddle from leaving screen.
     */
    public void update(float delta, float screenWidth) {
        Vector2 motion = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) motion.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) motion.x += 1;
        motion.setLength(Constants.PADDLE_SPEED * delta);
        move(motion);
        if (!onScreen(screenWidth, 0)) move(motion.scl(-1));
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.setColor(Constants.PADDLE_COLOR);
        renderer.rect(position.x, position.y, width, height);
    }

    @Override
    public boolean onScreen(float screenWidth, float screenHeight) {
        return position.x > 0 && position.x + width < screenWidth;
    }

}
