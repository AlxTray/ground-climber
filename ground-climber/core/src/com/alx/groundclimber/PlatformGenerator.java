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
        Array<Platform> initialPlatforms = new Array<Platform>();;
        initialPlatforms.add(new Platform(world,0, 0, 40f, 200f));
        initialPlatforms.add(new Platform(world,260f, 0, 70f, 120f));

        return initialPlatforms;
    }

    public Array<Platform> generatePlatformBatch(World world) {
        return new Array<Platform>();
    }

}
