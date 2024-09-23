package io.github.alxtray.groundclimber.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.GameMode;

public class CameraController {
    private static final int CAMERA_MOVEMENT_THRESHOLD = 300;
    private static final float CAMERA_TRANSLATION_STEP = 170f;
    private static final float AUTOSCROLL_CAMERA_TRANSLATION_STEP = 100f;
    private final ObjectIntMap<String> bounds;
    private final OrthographicCamera camera;

    public CameraController(ObjectFloatMap<String> cameraPosition, ObjectIntMap<String> bounds) {
        this.bounds = bounds;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.position.set(
            cameraPosition.get("x", 0),
            cameraPosition.get("y", 0),
            0);
    }

    public void update(float delta, Player player, GameMode gameMode) {
        if (gameMode == GameMode.ENDLESS && player.getBody().getPosition().x > 100) {
            camera.translate(AUTOSCROLL_CAMERA_TRANSLATION_STEP * delta, 0);
        } else if (gameMode == GameMode.NORMAL) {
            repositionCamera(delta, player);
        }
        checkCameraInBounds();
        camera.update();
    }

    private void repositionCamera(float delta, Player player) {
        ObjectFloatMap<String> cameraScreenBounds = getCameraScreenBounds();
        Vector2 playerPos = player.getPosition();
        // Have to add/subtract threshold back so the camera stop bound is absolute to
        // the position defined
        float cameraLeft = cameraScreenBounds.get("left", 0);
        float cameraRight = cameraScreenBounds.get("right", 0);
        float cameraBottom = cameraScreenBounds.get("bottom", 0);
        float cameraTop = cameraScreenBounds.get("top", 0);
        if (playerPos.x < (cameraLeft + CAMERA_MOVEMENT_THRESHOLD) && cameraLeft > bounds.get("left", 0)) {
            camera.translate(-CAMERA_TRANSLATION_STEP * delta, 0);
        }
        if (playerPos.x > (cameraRight - CAMERA_MOVEMENT_THRESHOLD) && cameraRight < bounds.get("right", 0)) {
            camera.translate(CAMERA_TRANSLATION_STEP * delta, 0);
        }
        if (playerPos.y < (cameraBottom + CAMERA_MOVEMENT_THRESHOLD) && cameraBottom > bounds.get("bottom", 0)) {
            camera.translate(0, -CAMERA_TRANSLATION_STEP * delta);
        }
        if (playerPos.y > (cameraTop - CAMERA_MOVEMENT_THRESHOLD) && cameraTop < bounds.get("top", 0)) {
            camera.translate(0, CAMERA_TRANSLATION_STEP * delta);
        }
    }

    private void checkCameraInBounds() {
        ObjectFloatMap<String> cameraScreenBounds = getCameraScreenBounds();
        int boundsLeft = bounds.get("left", 0);
        int boundsRight = bounds.get("right", 0);
        int boundsBottom = bounds.get("bottom", 0);
        int boundsTop = bounds.get("top", 0);
        // Move camera back within bounds if it has left
        if (cameraScreenBounds.get("left", 0) < boundsLeft) {
            camera.position.set(boundsLeft + camera.viewportWidth / 2, camera.position.y, camera.position.z);
        }
        if (cameraScreenBounds.get("right", 0) > boundsRight) {
            camera.position.set(boundsRight - camera.viewportWidth / 2, camera.position.y, camera.position.z);
        }
        if (cameraScreenBounds.get("bottom", 0) < boundsBottom) {
            camera.position.set(camera.position.x, boundsBottom + camera.viewportHeight / 2, camera.position.z);
        }
        if (cameraScreenBounds.get("top", 0) > boundsTop) {
            camera.position.set(camera.position.x, boundsTop - camera.viewportHeight / 2, camera.position.z);
        }
    }

    private ObjectFloatMap<String> getCameraScreenBounds() {
        ObjectFloatMap<String> cameraPosition = new ObjectFloatMap<>();
        cameraPosition.put("left", camera.position.x - camera.viewportWidth / 2);
        cameraPosition.put("right", camera.position.x + camera.viewportWidth / 2);
        cameraPosition.put("bottom", camera.position.y - camera.viewportHeight / 2);
        cameraPosition.put("top", camera.position.y + camera.viewportHeight / 2);
        return cameraPosition;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public ObjectIntMap<String> getBounds() {
        return bounds;
    }

}
