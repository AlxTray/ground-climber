package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonBuilder {
    private final String buttonText;
    private final Skin skin;
    private final Stage stage;
    private String actorName;
    private float x;
    private float y;
    private float width;
    private float height;
    private ClickListener clickListener;

    public ButtonBuilder(final String buttonText, final Skin skin, final Stage stage) {
        this.buttonText = buttonText;
        this.skin = skin;
        this.stage = stage;
    }

    public ButtonBuilder setActorName(final String actorName) {
        this.actorName = actorName;
        return this;
    }

    public ButtonBuilder setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ButtonBuilder setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ButtonBuilder setClickListener(final ClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public void build() {
        final TextButton button = new TextButton(buttonText, skin);
        button.getLabel().setFontScale(stage.getWidth() / stage.getHeight());
        button.setColor(Color.valueOf("636363"));
        button.getLabel().setColor(Color.BLACK);
        button.setPosition(x, y);
        if (actorName != null) {
            button.setName(actorName);
        }
        if (width != 0 && height != 0) {
            button.setSize(width, height);
        }
        if (clickListener != null) {
            button.addListener(clickListener);
        }
        stage.addActor(button);
    }

}
