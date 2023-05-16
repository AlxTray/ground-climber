package com.alx;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ProceduralLevelFactory implements EntityFactory {

    private int platformRandomSeed;
    private double lastPlatformX = 700;
    private int currentPlatformWidth = 50;
    private int currentPlatformHeight = 50;

    private enum EntityType {
        PLAYER, PLATFORM, COIN, ENEMY, DEATH
    }


    public ProceduralLevelFactory() {
        setRandomSeed();
    }

    public void spawnStartingPlatforms() {
        final int[][] startingPlatformCoords
                = {{0, 50, 150}, {100, 100, 75}, {230, 50, 50}, {300, 50, 50}, {400, 150, 30}, {650, 110, 80}};
        for (int[] coords : startingPlatformCoords) {
            currentPlatformWidth = coords[1];
            currentPlatformHeight = coords[2];
            getGameWorld().spawn("platform", coords[0],
                    getAppHeight() - currentPlatformHeight);
        }
    }

    public void spawnPlatformSet() {
        double[] lastPlatformCoords = new double[]{random(lastPlatformX + 50, lastPlatformX + 150),
                getAppHeight() - (currentPlatformHeight + platformRandomSeed)};
        for (int i = 0; i < 10; i++) {
            currentPlatformWidth = random(55, 250) - platformRandomSeed;
            currentPlatformHeight = random(55, currentPlatformHeight + 55) - platformRandomSeed;
            Entity spawnedPlatform = getGameWorld().spawn("platform",
                    lastPlatformCoords[0] + random(15, 175),
                    getAppHeight() - currentPlatformHeight);
            lastPlatformCoords = new double[]{spawnedPlatform.getX() + currentPlatformWidth,
                    getAppHeight() - currentPlatformHeight};
        }
        lastPlatformX = lastPlatformCoords[0];
        setRandomSeed();
    }

    private void setRandomSeed() {
        platformRandomSeed = generateRandomSeed();
    }

    private int generateRandomSeed() {
        return random(1, 50);
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PLATFORM)
                .viewWithBBox(new Rectangle(currentPlatformWidth,
                        currentPlatformHeight))
                .with(new PhysicsComponent())
                .build();
    }
}
