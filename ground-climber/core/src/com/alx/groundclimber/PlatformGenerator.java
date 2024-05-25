package com.alx.groundclimber;

import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class PlatformGenerator {

    World world;

    // Values set to values from last platform in the initial batch
    private float lastPlatformX = 520f;
    private float currentPlatformWidth = 60f;
    private float currentPlatformHeight = 20f;

    public PlatformGenerator(World world) {
        this.world = world;
    }

    public Array<Platform> generateInitialBatch() {
        Array<Platform> initialPlatforms = new Array<Platform>();;
        initialPlatforms.add(new Platform(world,0, 0, 40f, 200f));
        initialPlatforms.add(new Platform(world,260f, 0, 70f, 120f));
        initialPlatforms.add(new Platform(world,520f, 0, 20f, 60f));

        return initialPlatforms;
    }

    public Array<Platform> generatePlatformBatch(World world, OrthographicCamera camera) {
        int randomSeed = MathUtils.random(1, 50);
        Array<Platform> platformBatch = new Array<>();
        double[] lastPlatformCoords = new double[]{
                MathUtils.random((int) (lastPlatformX + 50), (int) lastPlatformX + 150),
                currentPlatformHeight + randomSeed
        };
        for (int i = 0; i < 10; i++)  {
            currentPlatformWidth = MathUtils.random(55, 250) - randomSeed;
            currentPlatformHeight = MathUtils.random(55, (int) currentPlatformHeight + 55) - randomSeed;
            Platform newPlatform = new Platform(
                    world,
                    (float) lastPlatformCoords[0] + MathUtils.random(15, 175),
                    0,
                    currentPlatformHeight,
                    currentPlatformWidth
            );
            platformBatch.add(newPlatform);
            lastPlatformCoords = new double[]{
                    newPlatform.getX() + currentPlatformWidth,
                    currentPlatformHeight
            };
        }
        lastPlatformX = (float) lastPlatformCoords[0];

        return platformBatch;
    }
;
}
