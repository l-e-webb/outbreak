package com.udacity.game_dev.outbreak.Ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

/**
 * Rectangular label for displaying text with basic color and border features.
 */
public class Label {

    Rectangle rect;
    String text;
    LabelStyle style;

    public Label(float x, float y, float width, float height, String text) {
        rect = new Rectangle(x, y, width, height);
        this.text = text;
        style = new LabelStyle();
    }

    public Label(float x, float y, float width, float height, String text, LabelStyle style) {
        this(x, y, width, height, text);
        this.style = style;
    }

    /**
     * Draws the label itself.  This does not include label text.
     * @param renderer  ShapeRenderer used to draw label.
     */
    public void renderLabel(ShapeRenderer renderer) {
        if (style.border) {
            renderer.setColor(style.borderColor);
            renderer.rect(rect.x, rect.y, rect.width, rect.height);
            renderer.setColor(style.backgroundColor);
            renderer.rect(
                    rect.x + style.borderWidth,
                    rect.y + style.borderWidth,
                    rect.width - style.borderWidth * 2,
                    rect.height - style.borderWidth * 2
            );
        } else {
            renderer.setColor(style.backgroundColor);
            renderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    /**
     * Get the text of the Label.
     * @return  Label text.
     */
    public String getText() {
        return text;
    }

    /**
     * Render this Label's text.  To be called after the ShapeRenderer batch in which renderLabel()
     * is called has been ended.
     * @param batch  SpriteBatch to draw text with.
     */
    public void renderText(SpriteBatch batch) {
        if (text.equals("")) return;
        //This call to .draw() renders the text centered within the rectangle of the Label.  There
        //if no guarantee, however, that the text will fit in the label.  If the font size is too
        //large or the string is too long, there will be overflow.
        style.font.draw(
                batch,
                text,
                rect.x,
                rect.y + rect.height / 2 + style.font.getXHeight() / 2,
                rect.width,
                Align.center,
                false
        );
    }

    /**
     * Contains style information for label, including font, text color, background color, and
     * border information.
     */
    public static class LabelStyle {

        Color backgroundColor;
        boolean border;
        float borderWidth;
        Color borderColor;
        final BitmapFont font;

        public LabelStyle(Color color, BitmapFont font) {
            backgroundColor = color;
            this.font = font;
        }

        public LabelStyle(Color color) {
            this(color, new BitmapFont());
        }

        public LabelStyle(BitmapFont font) {
            this(Color.BLACK, font);
        }

        public LabelStyle() {
            this(Color.BLACK, new BitmapFont());
        }

        public void setColor(Color color) {
            backgroundColor = color;
        }

        public void setBorder(float width, Color color) {
            setBorder(width);
            setBorderColor(color);
        }

        public void setBorder(float width) {
            border = width > 0;
            borderWidth = width;
        }

        public void setBorderColor(Color color) {
            borderColor = color;
        }

        public void setFontColor(Color color) {
            font.setColor(color);
        }

    }

}
