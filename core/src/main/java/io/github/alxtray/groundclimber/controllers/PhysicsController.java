package io.github.alxtray.groundclimber.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import elemental2.dom.FilePropertyBag;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.level.PlatformData;
import io.github.alxtray.groundclimber.listeners.CrackedPlatformContactListener;
import io.github.alxtray.groundclimber.listeners.DebugContactListener;
import io.github.alxtray.groundclimber.utilities.Logger;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.listeners.ContactListenerImpl;
import io.github.alxtray.groundclimber.utilities.PlatformFactory;
import text.formic.Stringf;

public class PhysicsController {
    private static final float TIME_STEP = 1 / 120f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float X_GRAVITY = 0;
    private static final float Y_GRAVITY = -425f;
    private final ContactListenerImpl contactListener;
    private final World world;
    private float deltaAccumulator;
    private Array<Platform> platforms = new Array<>();
    private Array<Body> objectsToDestroy = new Array<>();

    public PhysicsController(Array<PlatformData> platformsData) {
        world = new World(new Vector2(X_GRAVITY, Y_GRAVITY), true);
        contactListener = new ContactListenerImpl();
        world.setContactListener(contactListener);
        contactListener.addContactListener(new CrackedPlatformContactListener());
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            contactListener.addContactListener(new DebugContactListener());
        }

        PlatformFactory platformFactory = new PlatformFactory(world);
        for (PlatformData data : platformsData) {
            platforms.add(platformFactory.createPlatform(
                data.getType(),
                data.getX(),
                data.getY(),
                data.getHeight(),
                data.getWidth()));
        }
    }

    public void step(float delta) {
        doPhysicsStep(delta);
        queueObjectsToDestroy();
        destroyQueuedObjects();
    }

    private void doPhysicsStep(float delta) {
        deltaAccumulator += delta;
        while (deltaAccumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            deltaAccumulator -= TIME_STEP;
        }
    }

    private void queueObjectsToDestroy() {
        objectsToDestroy.addAll(contactListener.getBodiesToDestroy());
    }

    private void destroyQueuedObjects() {
        for (Body objectToDestroy : objectsToDestroy) {
            Object objectData = objectToDestroy.getUserData();
            platforms.removeValue((Platform) objectData, false);
            world.destroyBody(objectToDestroy);
            Logger.log(
                "Map",
                Stringf.format(
                    "The object %s has been destroyed from world",
                    objectData.getClass().getSimpleName()),
                LogLevel.DEBUG);
        }
        objectsToDestroy.clear();
    }

    public World getWorld() {
        return world;
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

}
