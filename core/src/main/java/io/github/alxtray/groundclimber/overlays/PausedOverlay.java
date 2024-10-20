package io.github.alxtray.groundclimber.overlays;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PausedOverlay extends Overlay {
    public PausedOverlay(ClickListener resetListener, ClickListener quitListener) {
        super("title_text", resetListener, quitListener);
    }

}
