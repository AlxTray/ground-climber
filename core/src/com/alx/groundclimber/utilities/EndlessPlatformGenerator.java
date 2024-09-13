package com.alx.groundclimber.utilities;

import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndlessPlatformGenerator {

  final PlatformFactory platformFactory;

  // Values set to values from last platform in the initial batch
  float lastPlatformX = 520f;
  float currentPlatformWidth = 60f;
  float currentPlatformHeight = 20f;

  public EndlessPlatformGenerator(World world) {
    platformFactory = new PlatformFactory(world);
  }

  public Array<Platform> generateInitialBatch() {
    Array<Platform> initialPlatforms = new Array<>();
    initialPlatforms.add(platformFactory.createPlatform("normal", 0, 0, 40f, 200f));
    initialPlatforms.add(platformFactory.createPlatform("normal", 260f, 0, 70f, 120f));
    initialPlatforms.add(platformFactory.createPlatform("normal", 520f, 0, 20f, 60f));

    return initialPlatforms;
  }

  public Array<Platform> generatePlatformBatch() {
    int randomSeed = MathUtils.random(1, 50);
    Gdx.app.debug(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
            + " DEBUG PlatformGenerator",
        String.format("Batch seed: %s", randomSeed));
    Array<Platform> platformBatch = new Array<>();
    double[] lastPlatformCoords = new double[] {
        MathUtils.random((int) (lastPlatformX + 50), (int) lastPlatformX + 150),
        currentPlatformHeight + randomSeed
    };
    for (int i = 0; i < 10; i++) {
      currentPlatformWidth = MathUtils.random(55, 250) - randomSeed;
      currentPlatformHeight = MathUtils.random(55, (int) currentPlatformHeight + 55) - randomSeed;
      Platform newPlatform = platformFactory.createPlatform(
          "normal",
          (float) lastPlatformCoords[0] + MathUtils.random(15, 175),
          0,
          currentPlatformHeight,
          currentPlatformWidth);
      platformBatch.add(newPlatform);
      lastPlatformCoords = new double[] {
          newPlatform.body.getPosition().x + currentPlatformWidth,
          currentPlatformHeight
      };
    }
    lastPlatformX = (float) lastPlatformCoords[0];

    return platformBatch;
  }

}
