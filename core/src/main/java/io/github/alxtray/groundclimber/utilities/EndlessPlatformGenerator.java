package io.github.alxtray.groundclimber.utilities;

import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class EndlessPlatformGenerator {

    private static final String ENDLESS_PLATFORM_TYPE = "normal";
    private final PlatformFactory platformFactory;
    // Values set to values from last platform in the initial batch
    private float lastPlatformX = 520f;
    private float currentPlatformWidth = 60f;
    private float currentPlatformHeight = 20f;

    public EndlessPlatformGenerator(World world) {
        platformFactory = new PlatformFactory(world);
    }

    public Array<Platform> generateInitialBatch() {
        Array<Platform> initialPlatforms = new Array<>();
        initialPlatforms.add(platformFactory.createPlatform(ENDLESS_PLATFORM_TYPE, 0, 0, 40f, 200f));
        initialPlatforms.add(platformFactory.createPlatform(ENDLESS_PLATFORM_TYPE, 260f, 0, 70f, 120f));
        initialPlatforms.add(platformFactory.createPlatform(ENDLESS_PLATFORM_TYPE, 520f, 0, 20f, 60f));

        return initialPlatforms;
    }

    public Array<Platform> generatePlatformBatch() {
        int randomSeed = MathUtils.random(1, 50);
        Logger.log(
                "PlatformGenerator",
                String.format("Batch seed: %s", randomSeed),
                LogLevel.DEBUG);
        Array<Platform> platformBatch = new Array<>();
        double[] lastPlatformCoords = new double[]{
                MathUtils.random((int) (lastPlatformX + 50), (int) lastPlatformX + 150),
                currentPlatformHeight + randomSeed
        };
        for (int i = 0; i < 10; i++) {
            currentPlatformWidth = (float) MathUtils.random(55, 250) - randomSeed;
            currentPlatformHeight = (float) MathUtils.random(55, (int) currentPlatformHeight + 55) - randomSeed;
            Platform newPlatform = platformFactory.createPlatform(
                    ENDLESS_PLATFORM_TYPE,
                    (float) lastPlatformCoords[0] + MathUtils.random(15, 175),
                    0,
                    currentPlatformHeight,
                    currentPlatformWidth);
            platformBatch.add(newPlatform);
            lastPlatformCoords = new double[]{
                    newPlatform.getPosition().x + currentPlatformWidth,
                    currentPlatformHeight
            };
        }
        lastPlatformX = (float) lastPlatformCoords[0];

        return platformBatch;
    }

}
