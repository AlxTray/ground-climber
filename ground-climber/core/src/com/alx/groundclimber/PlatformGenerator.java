package com.alx.groundclimber;

import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class PlatformGenerator {

    World world;
    private Vector2 lastPlatformBatchPos = new Vector2();

    public PlatformGenerator(World world) {
        this.world = world;
    }

    public Array<Platform> generateInitialBatch() {
        Array<Platform> initialPlatforms = new Array<Platform>();
        initialPlatforms.add(new Platform(world,20, 0, 20, 50));
        initialPlatforms.add(new Platform(world,120, 0, 40, 30));

        return initialPlatforms;
    }

    public Array<Platform> generatePlatformBatch(World world) {
        return new Array<Platform>();
    }

}
