package io.github.alxtray.groundclimber.enums;

public enum PlatformOrientation {
    NORTH(0), SOUTH(180);

    private final int angle;

    PlatformOrientation(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

}
