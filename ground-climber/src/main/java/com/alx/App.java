package com.alx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.entity.Entity;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * JavaFX App
 */
public class App extends GameApplication {

    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Ground Climber");
    }

    @Override
    protected void initGame() {
        player = entityBuilder()
                .at(500, 500)
                .view(new Rectangle(25, 25, Color.BLUE))
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.D, () -> {
            player.translateX(5);
        });

        onKey(KeyCode.A, () -> {
            player.translateX(-5);
        });
    }

    @Override
    protected void initUI() {
        Text text = new Text();
        text.setText("Ground Climber");
        text.setTranslateX(50);
        text.setTranslateY(100);
    
        getGameScene().addUINode(text);
    }

    public static void main(String[] args) {
        launch(args);
    }

}