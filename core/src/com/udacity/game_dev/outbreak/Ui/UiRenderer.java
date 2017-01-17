package com.udacity.game_dev.outbreak.Ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Class for creating, managing, and rendering a collection of UI labels and associated styles.
 */
public class UiRenderer {

    HashMap<String, Label.LabelStyle> styles;
    Array<Label> labels;

    public UiRenderer() {
        labels = new Array<Label>();
        styles = new HashMap<String, Label.LabelStyle>();
    }

    /**
     * Renders all labels.
     * @param  batch  SpriteBatch to draw label text with.
     * @param  renderer  ShapeRenderer to draw label boxes with.
     */
    public void render(SpriteBatch batch, ShapeRenderer renderer) {
        //The ShapeRenderer batch must be ended before the SpriteBatch begins in order for the text
        //to appear above the shapes.
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Label label : labels) {
            label.renderLabel(renderer);
        }
        renderer.end();
        batch.begin();
        for (Label label : labels) {
            label.renderText(batch);
        }
        batch.end();
    }

    /**
     * Gets the first label in the label array that contains the specified point, or null if there
     * is not one.
     * @param  x  x-coordinate in world coordinates.
     * @param  y  y-coordinate in world coordinates.
     * @return   Label containing the given point, null if none exits.
     */
    public Label getLabelAtPoint(float x, float y) {
        for (Label label : labels) {
            if (label.rect.contains(x, y)) {
                return label;
            }
        }
        return null;
    }

    /**
     * Add a Label to this UiRenderer.  Label will be rendered when render() is called.
     * @param  label  Label to add.
     */
    public void addLabel(Label label) {
        labels.add(label);
    }

    /**
     * Removes a label by index.
     * @param  index  Index of label to be removed.
     * @return  Removed label, if there is one.
     */
    public Label removeLabel(int index) {
        if (index >= 0 && index < labels.size) {
            return labels.removeIndex(index);
        }
        return null;
    }

    /**
     * Gets the text of a label identified by index.
     * @param  index  Index of label.
     * @return   Text of label at given index, if one exists.
     */
    public String getLabelText(int index) {
        if (index >= 0 && index < labels.size) {
            return labels.get(index).text;
        }
        return null;
    }

    /**
     * Sets the text of a label identified by index.
     * @param  index  Index of label to change.
     * @param  text   Desired text for label.
     */
    public void setLabelText(int index, String text) {
        if (index >= 0 && index < labels.size) {
            labels.get(index).text = text;
        }
    }

    /**
     * Adds a LabelStyle to this UiRenderer.  UiStyles are accessed by string names.
     * @param  key  String to use as label key.
     * @param  style  Style to add.
     */
    public void putLabelStyle(String key, Label.LabelStyle style) {
        styles.put(key, style);
    }

    /**
     * Gets a label by name.
     * @param  key  Name of desired label.
     * @return  Label with given name, if one exists.
     */
    public Label.LabelStyle getLabelStyle(String key) {
        return styles.get(key);
    }

    /**
     * Removes all labels.
     */
    public void clear() {
        labels.clear();
    }
}
