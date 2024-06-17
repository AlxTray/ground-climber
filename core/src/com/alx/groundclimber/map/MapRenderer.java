package com.alx.groundclimber.map;

import com.alx.groundclimber.enums.DebugRenderMode;
import com.alx.groundclimber.enums.GameMode;
import com.alx.groundclimber.bodies.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class MapRenderer {

    Map map;
    GameMode gameMode;
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture playerImage;
    Texture backgroundImage;

    // Debug rendering
    Box2DDebugRenderer debugRenderer;
    DebugRenderMode debugMode;

    public MapRenderer(Map map, GameMode gameMode, DebugRenderMode debugMode) {
        this.map = map;
        this.gameMode = gameMode;
        this.debugMode = debugMode;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        playerImage = new Texture(Gdx.files.internal("ball.png"));
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

        debugRenderer = new Box2DDebugRenderer();
    }

    public void render() {
        if (gameMode.equals(GameMode.ENDLESS) && map.player.body.getPosition().x > 100) {
            camera.translate(0.6f, 0);
        }
        if (gameMode.equals(GameMode.NORMAL)) {
            camera.position.set(map.player.body.getPosition().x, map.player.body.getPosition().y, 0);
        }
        camera.update();

        if (!debugMode.equals(DebugRenderMode.ONLY)) {
            batch.begin();
            batch.disableBlending();
            batch.draw(
                    backgroundImage,
                    camera.position.x - (camera.viewportWidth / 2),
                    camera.position.y - (camera.viewportHeight / 2),
                    camera.viewportWidth,
                    camera.viewportHeight
            );
            batch.enableBlending();
            batch.setProjectionMatrix(camera.combined);
            batch.draw(
                    playerImage,
                    map.player.body.getPosition().x - (playerImage.getWidth() / 2f),
                    map.player.body.getPosition().y - (playerImage.getHeight() / 2f)
            );

            for (Platform platform : map.platforms) {
                platform.draw(batch, 1);
            }
            batch.end();
        }
        if (!debugMode.equals(DebugRenderMode.NORMAL)) {
            debugRenderer.render(map.world, camera.combined);
        }
    }

    public void dispose() {
        playerImage.dispose();
        batch.dispose();
        Gdx.app.debug("MapRenderer - DEBUG", "Disposed objects");
    }

}
