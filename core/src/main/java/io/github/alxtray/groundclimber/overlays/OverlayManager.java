package io.github.alxtray.groundclimber.overlays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.alxtray.groundclimber.enums.GameStatus;

public class OverlayManager {
    private final PausedOverlay pausedOverlay;
    private final DeadOverlay deadOverlay;
    private final CompletedOverlay completedOverlay;

    public OverlayManager(ClickListener resetListener, ClickListener quitListener) {
        pausedOverlay = new PausedOverlay(resetListener, quitListener);
        deadOverlay = new DeadOverlay(resetListener, quitListener);
        completedOverlay = new CompletedOverlay(resetListener, quitListener);
    }

    public void checkAndRender(GameStatus status, float delta, OrthographicCamera camera, SpriteBatch batch) {
        switch (status) {
            case PLAYING:
                return;
            case PAUSED:
                pausedOverlay.render(delta, camera, batch);
                break;
            case DEAD:
                deadOverlay.render(delta, camera, batch);
                break;
            case COMPLETED:
                completedOverlay.render(delta, camera, batch);
                break;
            default:
                break;
        }
    }

}
