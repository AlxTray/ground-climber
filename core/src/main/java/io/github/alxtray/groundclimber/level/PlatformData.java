package io.github.alxtray.groundclimber.level;

public class PlatformData {
    private final String type;
    private final String orientation;
    private final float x;
    private final float y;
    private final float height;
    private final float width;

    public PlatformData(String type, String orientation, float x, float y, float height, float width) {
        this.type = type;
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public String getOrientation() {
        return orientation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

}
