package com.udacity.game_dev.outbreak.Utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Utility methods.
 */
public class Util {

    /**
     * Tests whether a rectangle and a circle intersect.  The logic is as follows: if the center of
     * the circle has y coordinate between the top and bottom of the rectangle or x coordinate
     * between the left and right edges of the rectangle, then we can test whether the two
     * intersect just as if the circle were a rectangle: by testing to see whether the horizontal
     * and vertical intervals spanned by the circle both  intersect with the intervals spanned by
     * the rectangle.
     * If the center of the circle does not satisfy the above condition, then the two intersect
     * if and only if one of the corners of the rectangle is within the circle.  Therefore, we test
     * whether the distance between the center of the circle and each rectangle corner is less
     * than the radius of the circle.
     * @param  circX       The x coordinate of the circle's center.
     * @param  circY       The y coordinate of the circles' center.
     * @param  radius      The circle's radius.
     * @param  rectX       The x coordinate of the rectangle's bottom left corner.
     * @param  rectY       The y coordinate of the rectangle's bottom left corner.
     * @param  rectWidth   The rectangle's width.
     * @param  rectHeight  The rectangle's height.
     * @return  True if the shapes intersect, false otherwise.
     */
    public static boolean rectIntersectsCircle(float circX, float circY, float radius, float rectX, float rectY, float rectWidth, float rectHeight) {
        if (isInInterval(rectX, rectX + rectWidth, circX) || isInInterval(rectY, rectY + rectHeight, circY)) {
            return intervalsOverlap(circX - radius, circX + radius, rectX, rectX + rectWidth) &&
                    intervalsOverlap(circY - radius, circY + radius, rectY, rectY + rectHeight);
        } else {
            return (Vector2.dst(circX, circY, rectX, rectY) <= radius ||
                    Vector2.dst(circX, circY, rectX, rectY + rectHeight) <= radius ||
                    Vector2.dst(circX, circY, rectX + rectWidth, rectY) <= radius ||
                    Vector2.dst(circX, circY, rectX + rectWidth, rectY + rectHeight) <= radius
            );
        }
    }

    /**
     * Tests whether a pair of intervals on the real line overlap, eg whether [a, b] and [c, d]
     * intersect.
     * @param  a  Left endpoint of first interval.
     * @param  b  Right endpoint of first interval.
     * @param  c  Left endpoint of second interval.
     * @param  d  Right endpoint of second interval.
     * @return  true if [a,b] intersects with [c,d], false otherwise.
     */
    public static boolean intervalsOverlap(float a, float b, float c, float d) {
        return b >= c && a <= d;
    }

    /**
     * Tests whether a given point on the real number line is between two other points.
     * @param a  Start of interval.
     * @param b  End of interval.
     * @param x  Point of interest.
     * @return  Whether point is within interval.
     */
    public static boolean isInInterval(float a, float b, float x) {
        return x >= a && x <= b;
    }

    /**
     * Returns the time in seconds since a long timestamp given by TimeUtils.nanoTime().
     * @param time Nanosecond timestamp.
     * @return  Seconds since parameter time.
     */
    public static float secondsSince(long time) {
        return (TimeUtils.nanoTime() - time) * MathUtils.nanoToSec;
    }
}
