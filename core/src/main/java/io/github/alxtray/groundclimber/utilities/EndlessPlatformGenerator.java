package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.LogLevel;

public class EndlessPlatformGenerator {
    private static final int PLATFORM_BATCH_SIZE = 10;
    private static final String ENDLESS_PLATFORM_TYPE = "normal";
    private final int generationThreshold;
    private final PlatformFactory platformFactory;
    // Values set to values from last platform in the initial batch
    private float lastPlatformX = 0f;
    private float lastPlatformWidth = 216f;
    private float lastPlatformHeight = 54f;

    public EndlessPlatformGenerator() {
        platformFactory = new PlatformFactory();
        generationThreshold = Gdx.graphics.getWidth() / 2;
    }

    public void checkAndGenerateBatch(World world, Player player, Array<EnvironmentObject> environmentObjects) {
        if (player.getPosition().x > lastPlatformX - generationThreshold) {
            Array<Platform> platformBatch = generatePlatformBatch(world);
            environmentObjects.addAll(platformBatch);
            Logger.log(
                "Map",
                "Successfully generated new endless platform batch",
                LogLevel.INFO);
        }
    }

    private Array<Platform> generatePlatformBatch(World world) {
        Array<Platform> platformBatch = new Array<>();
        for (int i = 0; i < PLATFORM_BATCH_SIZE; i++) {
            float currentPlatformWidth = (float) MathUtils.random(3, 10) * 18;
            float currentPlatformHeight = MathUtils.random(1, 5 + ((int) -lastPlatformHeight / 18)) * 18f;
            float currentPlatformX = (lastPlatformX + lastPlatformWidth) + MathUtils.random(15, 80);
            Platform newPlatform = platformFactory.createPlatform(
                world,
                ENDLESS_PLATFORM_TYPE,
                currentPlatformX,
                0,
                currentPlatformHeight,
                currentPlatformWidth);
            platformBatch.add(newPlatform);

            lastPlatformWidth = currentPlatformWidth;
            lastPlatformHeight = currentPlatformHeight;
            lastPlatformX = currentPlatformX;
        }

        return platformBatch;
    }

}
