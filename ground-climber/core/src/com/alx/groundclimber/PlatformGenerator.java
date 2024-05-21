package com.alx.groundclimber;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PlatformGenerator {

    private static Vector2 lastPlatformBatchPos = new Vector2();

    public static Array<Platform> generateInitialBatch() {
        Array<Platform> initialPlatforms = new Array<Platform>();
        initialPlatforms.add(new Platform(20, 0, 20, 50));
        initialPlatforms.add(new Platform(120, 0, 40, 30));

        return initialPlatforms;
    }

    public static Array<Platform> generatePlatformBatch() {
        return new Array<Platform>();
    }

}
