package io.github.alxtray.groundclimber.overlays;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CompletedOverlay extends Overlay {
    public CompletedOverlay(ClickListener resetListener, ClickListener quitListener) {
        super("title_text", resetListener, quitListener);
    }

}
