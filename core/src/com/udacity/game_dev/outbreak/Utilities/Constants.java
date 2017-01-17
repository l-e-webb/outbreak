package com.udacity.game_dev.outbreak.Utilities;

import com.badlogic.gdx.graphics.Color;

/**
 * Class with only static final members to store all game constants.
 */
public class Constants {

    public static final float WORLD_WIDTH = 640;
    public static final float WORLD_HEIGHT = 450;
    public static final float TOP_UI_HEIGHT = 50;

    public static final int STARTING_LIVES = 4;

    public static final int GAME_OVER_SCORE_DECREMENT = 500;
    public static final float BLOCK_DESTROY_SCORE_INCREASE = 100;
    public static final float BLOCK_HIT_SCORE_INCREASE = 50;
    public static final float BALL_DIE_SCORE_DECREASE = 100;

    public static final float TARGET_GAME_TIME = 400;
    public static final float TIME_SCORE_BONUS_FACTOR = 1000;
    public static final float REMAINING_LIVES_BONUS = 200;
    public static final float EASY_WIN_BONUS = 500;
    public static final float MEDIUM_WIN_BONUS = 1000;
    public static final float HARD_WIN_BONUS = 2000;

    public static final float BLOCK_AREA_SCREEN_RATIO = 0.4f;
    public static final float BLOCK_SPAWN_RATE_EASY = 25;
    public static final float BLOCK_SPAWN_RATE_MEDIUM = 20;
    public static final float BLOCK_SPAWN_RATE_HARD = 15;

    public static final int MAX_BLOCK_HEALTH = 3;
    public static final float BLOCK_WIDTH = 64;
    public static final float BLOCK_HEIGHT = 25;
    public static final float BLOCK_BORDER_WIDTH = 2;
    public static final Color[] BLOCK_HEALTH_COLORS = new Color[] {
            Color.RED,
            Color.PURPLE,
            Color.BLUE
    };

    public static final float BALL_RADIUS = 9;
    public static final float BALL_SPEED_EASY = 180;
    public static final float BALL_SPEED_MEDIUM = 210;
    public static final float BALL_SPEED_HARD = 240;
    public static final Color BALL_COLOR = Color.WHITE;

    public static final float PADDLE_SPEED = 150;
    public static final float PADDLE_WIDTH = 80;
    public static final float PADDLE_HEIGHT = 15;
    public static final Color PADDLE_COLOR = Color.LIGHT_GRAY;
    public static final float PADDLE_TRAJECTORY_ORIGIN_DEPTH = 20;

    public static final float NORMAL_TEXT_FONT_SCALE = 1f;
    public static final float NORMAL_LABEL_WIDTH = 100;
    public static final float NORMAL_LABEL_HEIGHT = 25;
    public static final float LARGE_TEXT_FONT_SCALE = 2;
    public static final float LARGE_LABEL_WIDTH = 250;
    public static final float LARGE_LABEL_HEIGHT = 75;
    public static final float LABEL_BORDER_WIDTH = 2;
    public static final Color LABEL_BACKGROUND_COLOR = Color.BLACK;
    public static final Color LABEL_TEXT_COLOR = Color.WHITE;
    public static final Color LABEL_BORDER_COLOR = Color.WHITE;

    public static final String TITLE_LABEL_TEXT = "OUTBREAK";
    public static final String DIFFICULTY_SELECT_LABEL_TEXT = "Choose a difficulty level: ";
    public static final String EASY_LABEL_TEXT = "Easy";
    public static final String NORMAL_LABEL_TEXT = "Normal";
    public static final String HARD_LABEL_TEXT = "Hard";
    public static final String SCORE_LABEL_TEXT = "Score:";
    public static final String LIVES_LABEL_TEXT = "Lives:";
    public static final String TIME_LABEL_TEXT = "Time:";
    public static final String WIN_LABEL_TEXT = "You Win!";
    public static final String GAME_OVER_LABEL_TEXT = "Game Over!";
    public static final String PLAY_AGAIN_TEXT = "Press spacebar to play again";
    public static final String RETURN_TO_TITLE_TEXT = "Press escape to return to title";

    public static final String SIMPLE_UI_STYLE = "simpleUiStyle";
    public static final String END_GAME_UI_STYLE = "endGameUiStyle";
    public static final String TITLE_UI_STYLE = "titleUiStyle";
    public static final String BUTTON_UI_STYLE = "buttonUiStyle";

}
