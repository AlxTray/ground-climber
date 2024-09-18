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
    private String actorName = null;
    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;
    private ClickListener clickListener = null;

    public ButtonBuilder(String buttonText, Skin skin, Stage stage) {
        this.buttonText = buttonText;
        this.skin = skin;
        this.stage = stage;
    }

    public ButtonBuilder setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public ButtonBuilder setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ButtonBuilder setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ButtonBuilder setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public void build() {
        TextButton button = new TextButton(buttonText, skin);
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
