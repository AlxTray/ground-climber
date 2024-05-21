package com.alx.groundclimber;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Platform {

    public final Vector2 position = new Vector2();
    public final Rectangle bounds = new Rectangle();

    public Platform(int x, int y, int height, int width) {
        position.x = x;
        position.y = y;
        bounds.height = height;
        bounds.width = width;
        bounds.x = position.x;
        bounds.y = position.y;
    }

}
