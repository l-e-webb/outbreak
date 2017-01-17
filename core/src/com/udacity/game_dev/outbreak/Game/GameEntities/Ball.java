package com.udacity.game_dev.outbreak.Game.GameEntities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.udacity.game_dev.outbreak.Utilities.Constants;
import com.udacity.game_dev.outbreak.Utilities.Util;

/**
 * Class representing the ball.
 */
public class Ball extends GameObject {

    float radius;
    Vector2 motion;
    final float speed;

    public Ball(float speed) {
        super();
        radius = Constants.BALL_RADIUS;
        width = radius * 2;
        height = radius * 2;
        color = Constants.BALL_COLOR;
        this.speed = speed;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.setColor(color);
        renderer.circle(position.x, position.y, radius);
    }

    /**
     * Move ball based on its motion vector, bouncing of walls or ceiling if appropriate.
     * @param stageWidth  Width of play area.
     * @param stageHeight Height of play area.
     */
    public void move(float stageWidth, float stageHeight) {
        if (position.x + motion.x + radius > stageWidth ||
                position.x + motion.x - radius < 0) {
            motion.x = -motion.x;
        }
        if (position.y + motion.y + radius > stageHeight) {
            motion.y = - motion.y;
        }
        position.add(motion);
    }

    /**
     * Update the motion vector to have appropriate length based on ball's speed and time since
     * prior frame.
     * @param delta  Time since prior frame.
     */
    public void updateMotion(float delta) {
        motion.setLength(speed * delta);
    }

    /**
     * Detects whether the ball is colliding with the given GameObject.  Collision can be assessed
     * before or after motion by current motion vector.
     * @param object  GameObject to collide with.
     * @param afterMotion  True to test for collision after the ball has moved.
     * @return
     */
    public boolean detectCollision(GameObject object, boolean afterMotion) {
        Vector2 pos = new Vector2(position);
        if (afterMotion) pos.add(motion);
        return detectCollision(object, pos, radius);
    }

    /**
     * Respond to collision with GameObject.
     * @param object  Object being collided with.
     * @return True if a block is destroyed by this collision.
     */
    public boolean collide(GameObject object) {
        if (object instanceof Paddle) kickOffPaddle((Paddle) object);
        else if (object instanceof Block) return collideWithBlock((Block) object);
        return false;
    }

    /**
     * Respond to collision with player paddle.
     * @param paddle  Paddle object.
     */
    public void kickOffPaddle(Paddle paddle) {
        /*
         * To get the kick trajectory, subtract the position of the trajectory origin (a point
         * below the center of the paddle) from the current position to get a vector representing
         * radial motion away from the trajectory origin.  Since the directory origin is directly
         * below the paddle, impacts at the center of the paddle will kick the ball up and impacts
         * closer to the edges will kick the ball in that direction.  Since the trajectory origin
         * is below the screen, there is a limit to how close to a horizontal bounce the ball can
         * take, even if it hits close to the edge of the paddle.
         */
        Vector2 kickTrajectory = new Vector2(position);
        kickTrajectory.sub(paddle.position.x + paddle.width / 2, paddle.position.y - Constants.PADDLE_TRAJECTORY_ORIGIN_DEPTH);
        float distanceThisFrame = motion.len();
        motion.set(kickTrajectory).setLength(distanceThisFrame);
    }

    /**
     * Respond to collision with block.
     * @param block  Block being collided with.
     * @return  True if block is destroyed.
     */
    public boolean collideWithBlock(Block block) {
        boolean changed = false;
        if (Util.rectIntersectsCircle(position.x + motion.x, position.y, radius,
                block.position.x, block.position.y, block.width, block.height)) {
            motion.x = -motion.x;
            changed = true;
        }
        if (Util.rectIntersectsCircle(position.x, position.y + motion.y, radius,
                block.position.x, block.position.y, block.width, block.height)) {
            motion.y = -motion.y;
            changed = true;
        }
        if (!changed) motion.scl(-1);
        return block.takeHit();
    }

    @Override
    public boolean onScreen(float screenWidth, float screenHeight) {
        return (position.x + radius > 0 &&
            position.x - radius < screenWidth &&
            position.y + radius > 0 &&
            position.y - radius < screenHeight
        );
    }

    /**
     * Initialize the ball's motion, used when launching ball at the beginning of game.
     * @param heading  Desired heading.
     */
    public void initHeading(Vector2 heading) {
        motion = new Vector2(heading);
    }

    /**
     * Static method for detecting collision between a rectangle and a circle, using corresponding
     * utility method.  Included for convenience here.
     * @param object Collision object.
     * @param position Coordinates of collision circle center.
     * @param radius Collision circle radius.
     * @return True if the given circle and GameObject rectangle intersect.
     */
    public static boolean detectCollision(GameObject object, Vector2 position, float radius) {
        return Util.rectIntersectsCircle(position.x, position.y, radius,
                object.position.x, object.position.y, object.width, object.height);
    }

}
