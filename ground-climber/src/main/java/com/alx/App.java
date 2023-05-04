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

    private final int basePlayerSpeed = 2;
    private final double xAcceleratorMax = 2.2;
    private final double xAcceleratorModifier = 0.008;
    private final double xAcceleratorDefault = 1.0;

    private final int jumpHeightInterval = 2;
    private final int baseJumpHeight = 75;
    private final double distanceJumpedDefault = 0.0;
    private final double yAcceleratorMax = 2.5;
    private final double yAcceleratorModifier = 0.02;
    private final double yAcceleratorDefault = 1.0;

    private final double gravityDefault = 1.0;
    private final double gravityModifier = 0.1;
    private final int gravityMax = 3;

    private final int appSizeHeight = 600;
    private final int appSizeWidth = 800;

    private int playerSpawnX = 500;
    private int playerSpawnY = 500;
    private final int playerWidth = 25;
    private final int playerHeight = 25;

    private PropertyMap gameVarsMap;


    private Entity player;

    private UserAction moveLeft = new UserAction("Move Left") {
        @Override
        protected void onAction() {
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(-basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            gameVarsMap.setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    private UserAction moveRight = new UserAction("Move Right") {
        @Override
        protected void onAction() {
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            gameVarsMap.setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    // TODO: #4 Add a way to stop the player from jumping when the player is falling/already jumping
    private UserAction jump = new UserAction("Jump") {
        @Override
        protected void onAction() {
            double yAccelerator = gameVarsMap.getDouble("yAccelerator");
            if (yAccelerator < yAcceleratorMax) {
                gameVarsMap.setValue("yAccelerator", yAccelerator + yAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            gameVarsMap.setValue("jumping", true);
        }
    };

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(appSizeWidth);
        settings.setHeight(appSizeHeight);
        settings.setTitle("Ground Climber");
    }

    @Override
    protected void initGame() {
        player = entityBuilder()
                .at(playerSpawnX, playerSpawnY)
                .view(new Rectangle(playerWidth, playerHeight, Color.BLUE))
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(moveLeft, KeyCode.A);
        input.addAction(moveRight, KeyCode.D);
        input.addAction(jump, KeyCode.SPACE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("xAccelerator", xAcceleratorDefault);
        vars.put("yAccelerator", yAcceleratorDefault);
        vars.put("gravity", gravityDefault);
        vars.put("jumping", false);
        vars.put("distanceJumped", distanceJumpedDefault);

        gameVarsMap = getWorldProperties();
    }

    @Override
    protected void initUI() {
        Text text = new Text();
        text.setText("Ground Climber");
        text.setTranslateX(50);
        text.setTranslateY(100);
    
        getGameScene().addUINode(text);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (gameVarsMap.getBoolean("jumping")) {
            final double yAccelerator = gameVarsMap.getDouble("yAccelerator");
            final double amountToJump = -jumpHeightInterval * yAccelerator;
            player.translateY(amountToJump);
            inc("distanceJumped", amountToJump);
            if (gameVarsMap.getDouble("distanceJumped") <= -baseJumpHeight * yAccelerator) {
                gameVarsMap.setValue("jumping", false);
                gameVarsMap.setValue("yAccelerator", yAcceleratorDefault);
                gameVarsMap.setValue("distanceJumped", distanceJumpedDefault);
            }
        }
        // TODO: #3 Change current gravity stop collision with BLOCK entity, not bottom of the screen
        if (player.getY() <= getAppHeight() - 25) {
            double gravity = gameVarsMap.getDouble("gravity");
            player.translateY(gravity);
            if (gravity < gravityMax) {
                gameVarsMap.setValue("gravity", gravity + gravityModifier);
            }
        }
        else {
            gameVarsMap.setValue("gravity", gravityDefault);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}