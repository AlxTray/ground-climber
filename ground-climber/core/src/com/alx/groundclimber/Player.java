package com.alx.groundclimber;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    public Rectangle bounds = new Rectangle();

    public Player(int x, int y, int height, int width) {
        bounds.x = x;
        bounds.y = y;
        bounds.height = height;
        bounds.width = width;
    }
}
