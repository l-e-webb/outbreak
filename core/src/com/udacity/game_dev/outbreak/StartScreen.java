package com.udacity.game_dev.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.udacity.game_dev.outbreak.Game.GameManager;
import com.udacity.game_dev.outbreak.Ui.Label;
import com.udacity.game_dev.outbreak.Ui.UiRenderer;
import com.udacity.game_dev.outbreak.Utilities.Constants;

/**
 * Screen for displaying game title and difficulty selection.
 */
public class StartScreen extends InputAdapter implements Screen {

    OutbreakGame game;

    private FitViewport viewport;
    private UiRenderer uiRenderer;
    private ShapeRenderer renderer;
    private SpriteBatch batch;
    //GameManager instance variable exists only to display background.
    private GameManager manager;

    public StartScreen(OutbreakGame game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        uiRenderer = new UiRenderer();
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        initBackground();
        initUi();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        manager.render(renderer);
        renderer.end();

        uiRenderer.render(batch, renderer);
    }

    /**
     * Initializes the title and difficulty-select UI.
     */
    public void initUi() {
        uiRenderer.clear();

        initUiStyles();

        //Add all necessary labels to the UI renderer.
        Label.LabelStyle buttonStyle = uiRenderer.getLabelStyle(Constants.BUTTON_UI_STYLE);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        //Title label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.LARGE_LABEL_WIDTH / 2,
                worldHeight / 2,
                Constants.LARGE_LABEL_WIDTH,
                Constants.LARGE_LABEL_HEIGHT,
                Constants.TITLE_LABEL_TEXT,
                uiRenderer.getLabelStyle(Constants.TITLE_UI_STYLE)
        ));
        //Select prompt label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.LARGE_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT,
                Constants.LARGE_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.DIFFICULTY_SELECT_LABEL_TEXT,
                uiRenderer.getLabelStyle(Constants.SIMPLE_UI_STYLE)
        ));
        //Easy button label:
        uiRenderer.addLabel(new Label(
                worldWidth / 4 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.EASY_LABEL_TEXT,
                buttonStyle
        ));
        //Normal button label:
        uiRenderer.addLabel(new Label(
                worldWidth / 2 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.NORMAL_LABEL_TEXT,
                buttonStyle
        ));
        //Hard button label:
        uiRenderer.addLabel(new Label(
                3 * worldWidth / 4 - Constants.NORMAL_LABEL_WIDTH / 2,
                worldHeight / 2 - Constants.NORMAL_LABEL_HEIGHT * 2,
                Constants.NORMAL_LABEL_WIDTH,
                Constants.NORMAL_LABEL_HEIGHT,
                Constants.HARD_LABEL_TEXT,
                buttonStyle
        ));
    }

    /**
     * Initializes styles for Labels and puts them in UiRenderer.
     */
    public void initUiStyles() {
        BitmapFont titleFont = new BitmapFont();
        titleFont.setColor(Constants.LABEL_TEXT_COLOR);
        titleFont.getData().setScale(Constants.LARGE_TEXT_FONT_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Label.LabelStyle titleStyle = new Label.LabelStyle(Constants.LABEL_BACKGROUND_COLOR, titleFont);
        titleStyle.setBorder(Constants.LABEL_BORDER_WIDTH, Constants.LABEL_BORDER_COLOR);
        uiRenderer.putLabelStyle(Constants.TITLE_UI_STYLE, titleStyle);

        BitmapFont uiFont = new BitmapFont();
        uiFont.setColor(Constants.LABEL_TEXT_COLOR);
        uiFont.getData().setScale(Constants.NORMAL_TEXT_FONT_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Label.LabelStyle simpleLabelStyle = new Label.LabelStyle(Constants.LABEL_BACKGROUND_COLOR, uiFont);
        //Uncomment the line below for red debug lines.
        //simpleLabelStyle.setBorder(1, Color.RED);
        uiRenderer.putLabelStyle(Constants.SIMPLE_UI_STYLE, simpleLabelStyle);
        Label.LabelStyle buttonLabelStyle = new Label.LabelStyle(Constants.LABEL_BACKGROUND_COLOR, uiFont);
        buttonLabelStyle.setBorder(Constants.LABEL_BORDER_WIDTH, Constants.LABEL_BORDER_COLOR);
        uiRenderer.putLabelStyle(Constants.BUTTON_UI_STYLE, buttonLabelStyle);
    }

    /**
     * Initializes the background for the title screen, which looks like an opening outbreak game.
     */
    public void initBackground() {
        manager = new GameManager(
                GameManager.Difficulty.EASY,
                viewport.getWorldWidth(),
                viewport.getWorldHeight() - Constants.TOP_UI_HEIGHT
        );
        manager.init();
    }

    /**
     * InputProcessor method responding to clicks.  Tests whether a difficulty option has been
     * clicked and initializes the game as appropriate.
     * @param screenX  Screen x-coordinate of click.
     * @param screenY  Screen y-coordinate of click.
     * @param pointer  Pointer number (irrelevant).
     * @param button   Mouse button (irrelevant).
     * @return True if a Label has been clicked, consuming the touch event, and false otherwise.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        Label clickedLabel = uiRenderer.getLabelAtPoint(worldClick.x, worldClick.y);
        if (clickedLabel != null) {
            String text = clickedLabel.getText();
            GameManager.Difficulty difficulty = null;
            if (text.equals(Constants.EASY_LABEL_TEXT)) {
                difficulty = GameManager.Difficulty.EASY;
            } else if (text.equals(Constants.NORMAL_LABEL_TEXT)) {
                difficulty = GameManager.Difficulty.MEDIUM;
            } else if (text.equals(Constants.HARD_LABEL_TEXT)) {
                difficulty = GameManager.Difficulty.HARD;
            }
            if (difficulty != null) {
                game.setGameScreen(difficulty);
                return true;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        renderer.dispose();
        batch.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {}

}
