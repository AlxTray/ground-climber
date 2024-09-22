package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.Logger;
import text.formic.Stringf;

public class RenderManager {
    private final BitmapFont font;
    private final PlayerRenderer playerRenderer;
    private final BackgroundObjectRenderer backgroundObjectRenderer;
    private final EnvironmentObjectVisitor environmentObjectRenderer;
    private boolean displayDebugInfo;

    public RenderManager() {
        font = new BitmapFont();
        playerRenderer = new PlayerRenderer();
        backgroundObjectRenderer = new BackgroundObjectRenderer();
        environmentObjectRenderer = new EnvironmentObjectRenderer();
        displayDebugInfo = false;
    }

    public void render(OrthographicCamera camera, Player player, Array<EnvironmentObject> environmentObjects, SpriteBatch batch) {
        backgroundObjectRenderer.render(batch, camera);
        playerRenderer.render(batch, player);
        for (EnvironmentObject environmentObject : environmentObjects) {
            environmentObject.acceptRender(environmentObjectRenderer, batch);
        }
        if (displayDebugInfo) {
            drawDebugInfo(camera, player, batch);
        }
    }

    private void drawDebugInfo(OrthographicCamera camera, Player player, SpriteBatch batch) {
        float cornerX = (camera.position.x - camera.viewportWidth / 2) + 10;
        float cornerY = camera.position.y + camera.viewportHeight / 2;
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), cornerX, cornerY - 10);
        font.draw(
            batch,
            Stringf.format(
                "Player Pos: (%.2f, %.2f)",
                player.getBody().getPosition().x,
                player.getBody().getPosition().y),
            cornerX,
            cornerY - 30);
        font.draw(batch,
            Stringf.format(
                "Player Lin Vec: (%.2f, %.2f)",
                player.getBody().getLinearVelocity().x,
                player.getBody().getLinearVelocity().y),
            cornerX,
            cornerY - 50);
        font.draw(batch,
            Stringf.format(
                "Player Ang Vec: %.2f",
                player.getBody().getAngularVelocity()),
            cornerX,
            cornerY - 70);
    }

    public void toggleDebugInfo() {
        displayDebugInfo = !displayDebugInfo;
    }

    public void dispose() {
        font.dispose();
        Logger.log("RenderManager", "Disposed objects", LogLevel.DEBUG);
    }

}
