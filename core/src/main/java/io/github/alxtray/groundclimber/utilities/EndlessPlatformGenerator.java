package io.github.alxtray.groundclimber.utilities;

import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import text.formic.Stringf;

public class EndlessPlatformGenerator {

    private static final String ENDLESS_PLATFORM_TYPE = "normal";
    private final PlatformFactory platformFactory;
    // Values set to values from last platform in the initial batch
    private float lastPlatformX = 0f;
    private float lastPlatformWidth = 216f;
    private float lastPlatformHeight = 54f;

    public EndlessPlatformGenerator(World world) {
        platformFactory = new PlatformFactory(world);
    }

    public Array<Platform> generatePlatformBatch() {
        Array<Platform> platformBatch = new Array<>();
        for (int i = 0; i < 10; i++) {
            float currentPlatformWidth = (float) MathUtils.random(3, 10) * 18;
            float currentPlatformHeight = MathUtils.random(1, 5 + ((int) -lastPlatformHeight / 18)) * 18f;
            float currentPlatformX = (lastPlatformX + lastPlatformWidth) + MathUtils.random(15, 80);
            Platform newPlatform = platformFactory.createPlatform(
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
