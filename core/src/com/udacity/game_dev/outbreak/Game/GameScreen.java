package com.udacity.game_dev.outbreak.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.udacity.game_dev.outbreak.OutbreakGame;
import com.udacity.game_dev.outbreak.Ui.Label;
import com.udacity.game_dev.outbreak.Ui.UiRenderer;
import com.udacity.game_dev.outbreak.Utilities.Constants;
import com.udacity.game_dev.outbreak.Utilities.Util;

/**
 * Screen for gameplay.
 */
public class GameScreen extends ScreenAdapter {

    OutbreakGame game;
    GameManager.Difficulty difficulty;

    private FitViewport viewport;
    private GameManager manager;
    private UiRenderer uiRenderer;
    private ShapeRenderer renderer;
    private SpriteBatch batch;

    private int scoreLabelIndex;
    private int livesLabelIndex;
    private int timeLabelIndex;

    public GameScreen(OutbreakGame game, GameManager.Difficulty difficulty) {
        super();
        this.game = game;
        this.difficulty = difficulty;
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        uiRenderer = new UiRenderer();
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        initGame(difficulty);
        initUi();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (manager.state != GameManager.GameState.WIN && manager.state != GameManager.GameState.GAME_OVER) {
            manager.update(delta);
            updateUi(manager.score, manager.lives, (int) Util.secondsSince(manager.startTime));
            checkForGameEnd();
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                initGame(manager.difficulty);
                initGameUi();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setStartScreen();
                return;
            }
        }

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        manager.render(renderer);
        renderer.end();

        uiRenderer.render(batch, renderer);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawBorders(renderer);
        renderer.end();

    }

    /**
     * Draws white lines to indicate play area.
     * @param renderer  ShapeRenderer used to draw lines.
     */
    public void drawBorders(ShapeRenderer renderer) {
        renderer.setColor(Color.WHITE);
        float stageWidth = viewport.getWorldWidth();
        float stageHeight = viewport.getWorldHeight() - Constants.TOP_UI_HEIGHT;
        renderer.line(1, 0, 1, stageHeight);
        renderer.line(0, 1, stageWidth, 1);
        renderer.line(stageWidth, 0, stageWidth, stageHeight);
        renderer.line(0, stageHeight, stageWidth, stageHeight);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        renderer.dispose();
        batch.dispose();
    }

    /**
     * Initializes the game with a given difficulty by creating and initializing the
     * GameManager.
     * @param  difficulty  Desired difficulty level.
     */
    public void initGame(GameManager.Difficulty difficulty) {
        manager = new GameManager(
                difficulty,
                viewport.getWorldWidth(),
                viewport.getWorldHeight() - Constants.TOP_UI_HEIGHT
        );
        manager.init();
    }

    /**
     * Initializes the uiRenderer and creates in-game HUD.
     */
    public void initUi() {
        initStyles();
        initGameUi();
    }

    /**
     * Creates in-game HUD.  Initialize iuRenderer through initStyles() first.
     */
    public void initGameUi() {
        uiRenderer.clear();
        Label.LabelStyle labelStyle = uiRenderer.getLabelStyle(Constants.SIMPLE_UI_STYLE);
        //Score text label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() - Constants.NORMAL_LABEL_WIDTH,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.SCORE_LABEL_TEXT,
                labelStyle
        ));
        //Score number label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() - Constants.NORMAL_LABEL_WIDTH,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                "",
                labelStyle
        ));
        scoreLabelIndex = 1;
        //Lives count text label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() - Constants.NORMAL_LABEL_WIDTH * 2,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.LIVES_LABEL_TEXT,
                labelStyle
        ));
        //Lives number label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() - Constants.NORMAL_LABEL_WIDTH * 2,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                "",
                labelStyle
        ));
        livesLabelIndex = 3;
        //Time text label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.TIME_LABEL_TEXT,
                labelStyle
        ));
        //Time number label:
        uiRenderer.addLabel(new Label(
                viewport.getWorldWidth() / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                viewport.getWorldHeight() - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                "",
                labelStyle
        ));
        timeLabelIndex = 5;
    }

    /**
     * Displays Game Over! or You Win! & other end-game labels.
     */
    public void initEndUi() {
        Label.LabelStyle labelStyle = uiRenderer.getLabelStyle(Constants.SIMPLE_UI_STYLE);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        //Game Over / You Win! text label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.LARGE_LABEL_WIDTH / 2,
                worldHeight / 2,
                Constants.LARGE_LABEL_WIDTH,
                Constants.LARGE_LABEL_HEIGHT,
                ((manager.state == GameManager.GameState.WIN) ? Constants.WIN_LABEL_TEXT : Constants.GAME_OVER_LABEL_TEXT),
                uiRenderer.getLabelStyle(Constants.END_GAME_UI_STYLE)
        ));
        //Score display label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.SCORE_LABEL_TEXT + " " + manager.score,
                labelStyle
        ));
        //Play again label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.PLAY_AGAIN_TEXT,
                labelStyle
        ));
        //Return to title label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT * 3,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.RETURN_TO_TITLE_TEXT,
                labelStyle
        ));
    }

    /**
     * Initializes the LabelStyles for UI labels and puts them into the UiRenderer.
     */
    public void initStyles() {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(Constants.NORMAL_TEXT_FONT_SCALE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font);
        //Uncomment the line below for red debug lines on labels.
        //labelStyle.setBorder(1, Color.RED);
        uiRenderer.putLabelStyle(Constants.SIMPLE_UI_STYLE, labelStyle);
        font = new BitmapFont();
        font.getData().setScale(Constants.LARGE_TEXT_FONT_SCALE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        labelStyle = new Label.LabelStyle(font);
        labelStyle.setBorder(2, Color.WHITE);
        uiRenderer.putLabelStyle(Constants.END_GAME_UI_STYLE, labelStyle);
    }

    /**
     * Updates the in-game HUD labels for score, lives, and time.  Called every frame.
     * @param  score  Score to be displayed.
     * @param  lives  Life count to be displayed.
     * @param  time   Time value to be displayed.
     */
    public void updateUi(int score, int lives, int time) {
        uiRenderer.setLabelText(scoreLabelIndex, score + "");
        uiRenderer.setLabelText(livesLabelIndex, lives + "");
        uiRenderer.setLabelText(timeLabelIndex, time + "");
    }

    /**
     * Checks to see if the game is over and, if so, initializes the end-game UI.
     */
    public void checkForGameEnd() {
        if (manager.state == GameManager.GameState.WIN || manager.state == GameManager.GameState.GAME_OVER) {
            initEndUi();
        }
    }
}
