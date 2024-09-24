package io.github.alxtray.groundclimber.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.level.PlatformData;
import io.github.alxtray.groundclimber.listeners.BouncyPlatformContactListener;
import io.github.alxtray.groundclimber.listeners.ContactListenerImpl;
import io.github.alxtray.groundclimber.listeners.CrackedPlatformContactListener;
import io.github.alxtray.groundclimber.listeners.DebugContactListener;
import io.github.alxtray.groundclimber.utilities.Logger;
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
    private final Array<EnvironmentObject> environmentObjects = new Array<>();
    private final Array<Body> objectsToDestroy = new Array<>();

    public PhysicsController(Array<PlatformData> platformsData) {
        world = new World(new Vector2(X_GRAVITY, Y_GRAVITY), true);
        contactListener = new ContactListenerImpl();
        world.setContactListener(contactListener);
        contactListener.addContactListener(new CrackedPlatformContactListener());
        contactListener.addContactListener(new BouncyPlatformContactListener());
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            contactListener.addContactListener(new DebugContactListener());
        }

        PlatformFactory platformFactory = new PlatformFactory();
        for (PlatformData data : platformsData) {
            environmentObjects.add(platformFactory.createPlatform(
                world,
                data.getType(),
                data.getOrientation(),
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

    public void addObjectToDestroy(Body object) {
        objectsToDestroy.add(object);
    }

    private void queueObjectsToDestroy() {
        objectsToDestroy.addAll(contactListener.getBodiesToDestroy());
    }

    private void destroyQueuedObjects() {
        for (Body objectToDestroy : objectsToDestroy) {
            EnvironmentObject objectData = (EnvironmentObject) objectToDestroy.getUserData();
            environmentObjects.removeValue(objectData, false);
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

    public Array<EnvironmentObject> getEnvironmentObjects() {
        return environmentObjects;
    }

}
