package com.alx;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * JavaFX App
 */
public class App extends GameApplication {

    private Entity player;

    private UserAction moveLeft = new UserAction("Move Left") {
        @Override
        protected void onAction() {
            PropertyMap gameVarsMap = getWorldProperties();
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(-2 * xAccelerator);
            if (xAccelerator < 2.2) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + 0.008);
            }
        }

        @Override
        protected void onActionEnd() {
            getWorldProperties().setValue("xAccelerator", 1.0);
        }
    };

    private UserAction moveRight = new UserAction("Move Right") {
        @Override
        protected void onAction() {
            PropertyMap gameVarsMap = getWorldProperties();
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(2 * xAccelerator);
            if (xAccelerator < 2.2) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + 0.008);
            }
        }

        @Override
        protected void onActionEnd() {
            getWorldProperties().setValue("xAccelerator", 1.0);
        }
    };

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
        Input input = getInput();

        input.addAction(moveLeft, KeyCode.A);
        input.addAction(moveRight, KeyCode.D);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("xAccelerator", 1.0);
        vars.put("yAccelerator", 1.0);
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