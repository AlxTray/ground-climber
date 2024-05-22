package com.alx.groundclimber;

import com.badlogic.gdx.utils.Array;

public class Map {

    public Player player;
    Array<Platform> platforms = new Array<Platform>();

    public Map() {
        int PLAYER_INITIAL_WIDTH = 64;
        int PLAYER_INITIAL_HEIGHT = 64;
        int PLAYER_INITIAL_Y = 20;
        int PLAYER_INITIAL_X = 50;

        spawnNewPlayer(
                PLAYER_INITIAL_X,
                PLAYER_INITIAL_Y,
                PLAYER_INITIAL_HEIGHT,
                PLAYER_INITIAL_WIDTH
        );
    }

    public void spawnNewPlayer(int x, int y, int height, int width) {
        player = new Player(x, y, height, width);
    }
}
