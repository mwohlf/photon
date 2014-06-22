package net.wohlfart.photon.tools;

import java.awt.Color;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;


public class ColorGradient implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double LEFT = -1d;
    private static final double RIGHT = +1d;

    private final Set<GradientPoint> gradientPoints = new TreeSet<GradientPoint>();


    public ColorGradient(Color... colors) {
        if (colors.length == 1) {
            gradientPoints.add(new GradientPoint(LEFT, colors[0]));
            gradientPoints.add(new GradientPoint(RIGHT, colors[0]));
        } else {
            for (int i = 0; i < colors.length; i++) {
                final double delta = (RIGHT - LEFT) * i / (colors.length - 1d);
                gradientPoints.add(new GradientPoint(LEFT + delta, colors[i]));
            }
        }
    }

    public ColorGradient(GradientPoint... points) {
        for (final GradientPoint gradientPoint : points) {
            gradientPoints.add(gradientPoint);
        }
    }

    // for testing
    Set<GradientPoint> getGradientPoints() {
        return gradientPoints;
    }

    public Color getColor(double value) {

        final int size = gradientPoints.size();
        final GradientPoint pointArray[] = new GradientPoint[size];
        gradientPoints.toArray(pointArray);

        GradientPoint left = pointArray[0];
        for (int i = 0; i < size; i++) {
            final GradientPoint next = pointArray[i];
            if (next.point > value) {
                break; // we need to stay below the next value
            }
            left = next;
        }

        GradientPoint right = pointArray[size - 1];
        for (int i = size - 1; i >= 0; i--) {
            final GradientPoint next = pointArray[i];
            if (next.point < value) {
                break; // we need to stay below the next value
            }
            right = next;
        }

        if (left == right) {
            return right.color;
        }

        // now calculate gradient between left and right
        double delta = 1d;
        double distanceLeft = 0.5d;
        double distanceRight = 0.5d;
        if (right.point > left.point) {
            delta = right.point - left.point;
            distanceLeft = (value - left.point) / delta;
            distanceRight = (right.point - value) / delta;
        }

        return calculateRGBColor(left, right, distanceLeft, distanceRight);
    }

    private Color calculateRGBColor(GradientPoint left2, GradientPoint right2, double distanceLeft, double distanceRight) {
        final float red = (left2.color.getRed() * (float) distanceRight + right2.color.getRed() * (float) distanceLeft) / 256f;
        final float green = (left2.color.getGreen() * (float) distanceRight + right2.color.getGreen() * (float) distanceLeft) / 256f;
        final float blue = (left2.color.getBlue() * (float) distanceRight + right2.color.getBlue() * (float) distanceLeft) / 256f;
        return new Color(red, green, blue);
    }

    public static class GradientPoint implements Comparable<GradientPoint>, Serializable {
        private static final long serialVersionUID = 1L;
        final double point;
        final Color color;

        public GradientPoint(final double point, final Color color) {
            this.point = point;
            this.color = color;
        }

        @Override
        public int compareTo(final GradientPoint that) {
            return Double.compare(this.point, that.point);
        }

        @Override
        public boolean equals(Object that) {
            if (that instanceof GradientPoint) {
                return Double.compare(this.point, ((GradientPoint) that).point) == 0;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Double.valueOf(point).hashCode();
        }

    }

    public static Color cosGradient(final Color top, final Color low, final float v) {
        // v [-1 .. 1]
        float value = v;

        value = (float) Math.sin(value * (float) Math.PI / 2f);
        value = (float) Math.sin(value * (float) Math.PI / 2);
        value = (float) Math.sin(value * (float) Math.PI / 2);

        final float red = (top.getRed() * value + low.getRed() * (1f - value)) / 256f;
        final float green = (top.getGreen() * value + low.getGreen() * (1f - value)) / 256f;
        final float blue = (top.getBlue() * value + low.getBlue() * (1f - value)) / 256f;
        return new Color(red, green, blue);
    }


    public static Color linearGradient(final Color top, final Color low, final double skyNoise) {
        // v [-1 .. 1]
        final float value = (float) (skyNoise + 1f) / 2f;

        final float red = (top.getRed() * value + low.getRed() * (1f - value)) / 256f;
        final float green = (top.getGreen() * value + low.getGreen() * (1f - value)) / 256f;
        final float blue = (top.getBlue() * value + low.getBlue() * (1f - value)) / 256f;
        return new Color(red, green, blue);
    }

}
