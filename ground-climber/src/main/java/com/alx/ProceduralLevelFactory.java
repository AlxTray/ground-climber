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
            set("currentPlatformWidth", coords[1]);
            set("currentPlatformHeight", coords[2]);
            getGameWorld().spawn("platform", coords[0],
                    getAppHeight() - geti("currentPlatformHeight"));
        }
    }

    public void spawnPlatformSet() {
        double[] lastPlatformCoords = new double[]{random(lastPlatformX + 50, lastPlatformX + 150),
                getAppHeight() - (geti("currentPlatformHeight") + platformRandomSeed)};
        for (int i = 0; i < 10; i++) {
            set("currentPlatformWidth", random(55, 200) - platformRandomSeed);
            set("currentPlatformHeight", random(55, geti("currentPlatformHeight") + 55) - platformRandomSeed);
            Entity spawnedPlatform = getGameWorld().spawn("platform",
                    lastPlatformCoords[0] + random(15, 175),
                    getAppHeight() - geti("currentPlatformHeight"));
            lastPlatformCoords = new double[]{spawnedPlatform.getX() + geti("currentPlatformWidth"),
                    getAppHeight() - geti("currentPlatformHeight")};
        }
        lastPlatformX = lastPlatformCoords[0];
        setRandomSeed();
    }

    public void setRandomSeed() {
        platformRandomSeed = generateRandomSeed();
    }

    public int generateRandomSeed() {
        return random(1, 50);
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PLATFORM)
                .viewWithBBox(new Rectangle(geti("currentPlatformWidth"),
                        geti("currentPlatformHeight")))
                .with(new PhysicsComponent())
                .build();
    }
}
