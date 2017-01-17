package com.udacity.game_dev.outbreak.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.game_dev.outbreak.Utilities.Constants;
import com.udacity.game_dev.outbreak.Game.GameEntities.Ball;
import com.udacity.game_dev.outbreak.Game.GameEntities.Block;
import com.udacity.game_dev.outbreak.Game.GameEntities.GameObject;
import com.udacity.game_dev.outbreak.Game.GameEntities.Paddle;
import com.udacity.game_dev.outbreak.Utilities.Util;

/**
 * Manages all entities in the game, such as blocks, ball, and paddle.  Handles score & lives.
 */
public class GameManager {

    public int lives;
    public int score;
    public long startTime;
    public GameState state;
    public final Difficulty difficulty;

    private float stageWidth;
    private float stageHeight;
    private int numRows;
    private int numColumns;
    private int numBlocks;
    private Array<GameObject> gameObjects;
    private Array<Block[]> blocks;
    private long lastBlockSpawnTime;
    private float spawnRate;
    private Ball ball;
    private Paddle paddle;

    public GameManager(Difficulty difficulty, float width, float height) {
        this.difficulty = difficulty;
        stageWidth = width;
        stageHeight = height;
        //The number of rows is the number of blocks we can fit on the screen side by side.
        numColumns = (int) (stageWidth / Constants.BLOCK_WIDTH);
        //The number of columns is the number of blocks we can fit on the screen top to bottom
        //filling part of the screen.
        numRows = (int) (Constants.BLOCK_AREA_SCREEN_RATIO * stageHeight / Constants.BLOCK_HEIGHT);
        numBlocks = numRows * numColumns;
        //The maximum number of GameObjects in the scene is the number of blocks plus 2 for the
        //paddle and ball.
        gameObjects = new Array<GameObject>(false, numRows * numColumns + 2);
        blocks = new Array<Block[]>(true, numRows);
    }

    /**
     * Draws all objects in scene.
     * @param renderer  ShapeRenderer used to draw objects.
     */
    public void render(ShapeRenderer renderer) {
        for (GameObject object : gameObjects) {
            if (object.getIsVisible()) object.render(renderer);
        }
    }

    /**
     * Update state of all objects based on time since prior frame.
     * @param delta  Time since prior frame.
     */
    public void update(float delta) {
        paddle.update(delta, stageWidth);

        if (state == GameState.PLAYING) {
            ball.updateMotion(delta);
            //Check for collisions between the ball and other objects.
            for (GameObject object : gameObjects) {
                if (object.getIsVisible() &&
                        !(object instanceof Ball) &&
                        ball.detectCollision(object, true)
                ) {
                    //The collide method returns true if a block was destroyed.
                    if (ball.collide(object)) {
                        score += Constants.BLOCK_DESTROY_SCORE_INCREASE;
                        if (isGameWon()) {
                            win();
                            return;
                        }
                    } else if (!(object instanceof Paddle)) {
                        score += Constants.BLOCK_HIT_SCORE_INCREASE;
                    }
                }
            }
            ball.move(stageWidth, stageHeight);
            if (!ball.onScreen(stageWidth, stageHeight)) {
                state = GameState.READY;
                lives--;
                score -= Constants.BALL_DIE_SCORE_DECREASE;
                if (lives <= 0) {
                    gameOver();
                    return;
                }
            }
        } else if (state == GameState.READY) {
            //In the ready state, the ball is manually positioned on top of the paddle.
            ball.setPosition(
                    paddle.getX() + paddle.getWidth() / 2,
                    paddle.getY() + paddle.getHeight() + ball.getHeight() / 2
            );
            //If the user presses space while the game is in the READY state, it will bounce upward.
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                state = GameState.PLAYING;
                ball.initHeading(new Vector2(0,1));
            }
        }
        //If sufficient time has passed, spawn a new block and reset the timer.
        if (Util.secondsSince(lastBlockSpawnTime) > spawnRate) {
            generateRandomBlock();
            lastBlockSpawnTime = TimeUtils.nanoTime();
        }
    }

    /**
     * Initialize the block array.
     */
    private void generateBlocks() {
        for (int i = 0; i < numRows; i ++) {
            Block[] row = new Block[numColumns];
            for (int j = 0; j < numColumns; j++) {
                row[j] = generateBlock(i, j, getStartingHealthByRow(i));
                gameObjects.add(row[j]);
            }
            blocks.add(row);
        }
    }

    /**
     * Creates a block at a specified position.
     * @param row    Row in block grid.
     * @param column Column in block grid
     * @param startingHealth  Initial health for block.
     * @return new Block at position given by row & column and with specified health.
     */
    private Block generateBlock(int row, int column, int startingHealth) {
        float x = column * Constants.BLOCK_WIDTH;
        float y = stageHeight - (row + 1) * Constants.BLOCK_HEIGHT;
        return new Block(x, y, startingHealth);
    }

    /**
     * Regenerates a random destroyed block.
     */
    private void generateRandomBlock() {
        //Create an array of row/column coordinates of destroyed blocks.
        Array<int[]> openPositions = new Array<int[]>(numBlocks);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (!blocks.get(i)[j].getIsVisible()) {
                    openPositions.add(new int[]{i, j});
                }
            }
        }
        openPositions.shrink();
        if (openPositions.size == 0) return;
        //Choose random destroyed block coordinates.
        int[] newBlockPosition = openPositions.get(MathUtils.random(openPositions.size-1));
        //Re-initialize the block at those coordinates.
        blocks.get(newBlockPosition[0])[newBlockPosition[1]].init(1);
    }

    /**
     * Gets appropriate starting health for a block by its row.
     * @param  row  Row index.
     * @return  block health for blocks in given row.
     */
    private int getStartingHealthByRow(int row) {
        int rowsPerValue = Math.round(numRows * 1.0f / Constants.MAX_BLOCK_HEALTH);
        for (int i = 1;  i <= Constants.MAX_BLOCK_HEALTH; i++) {
            if (row < rowsPerValue * i) {
                return Constants.MAX_BLOCK_HEALTH - i + 1;
            }
        }
        return Constants.MAX_BLOCK_HEALTH;
    }

    /**
     * Checks to see if all blocks have been destroyed.
     * @return True if all blocks are destroyed, false otherwise.
     */
    public boolean isGameWon() {
        for (Block[] row : blocks) {
            for (Block block : row) {
                if (block.getIsVisible()) return false;
            }
        }
        return true;
    }

    /**
     * Method called at game victory.  Totals score and sets game state.
     */
    private void win() {
        score += calculateWinBonus();
        state = GameState.WIN;
    }

    /**
     * Method called at game over.
     */
    private void gameOver() {
        score -= Constants.GAME_OVER_SCORE_DECREMENT;
        state = GameState.GAME_OVER;
    }

    /**
     * Calculates score bonus for victory based on remaining lives, time, and difficulty.
     * @return score bonus.
     */
    private int calculateWinBonus() {
        float bonus = 0;
        bonus += (Constants.TARGET_GAME_TIME / Util.secondsSince(startTime)) * Constants.TIME_SCORE_BONUS_FACTOR;
        switch (difficulty) {
            case EASY:
                bonus += Constants.EASY_WIN_BONUS;
                break;
            case MEDIUM:
                bonus += Constants.MEDIUM_WIN_BONUS;
                break;
            case HARD:
                bonus += Constants.HARD_WIN_BONUS;
        }
        bonus += lives * Constants.REMAINING_LIVES_BONUS;
        return (int) bonus;
    }

    /**
     * Initialize the game.  Called when the screen loads and to reset game to play again.
     */
    public void init() {
        startTime = TimeUtils.nanoTime();
        score = 0;
        lives = Constants.STARTING_LIVES;
        generateBlocks();
        float speed;
        switch (difficulty) {
            case EASY:
                speed = Constants.BALL_SPEED_EASY;
                spawnRate = Constants.BLOCK_SPAWN_RATE_EASY;
                break;
            case MEDIUM: default:
                speed = Constants.BALL_SPEED_MEDIUM;
                spawnRate = Constants.BLOCK_SPAWN_RATE_MEDIUM;
                break;
            case HARD:
                speed = Constants.BALL_SPEED_HARD;
                spawnRate = Constants.BLOCK_SPAWN_RATE_HARD;
                break;
        }
        ball = new Ball(speed);
        gameObjects.add(ball);
        paddle = new Paddle(stageWidth / 2 - Constants.PADDLE_WIDTH / 2, Constants.PADDLE_HEIGHT);
        gameObjects.add(paddle);
        ball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() + paddle.getHeight() + ball.getHeight() / 2);
        state = GameState.READY;
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }

    public enum GameState {
        PLAYING,
        READY,
        GAME_OVER,
        WIN
    }

}
