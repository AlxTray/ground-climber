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
    private final int yAcceleratorMax = 3;
    private final double yAcceleratorModifier = 0.008;
    private final double yAcceleratorDefault = 1.0;

    private Entity player;

    private UserAction moveLeft = new UserAction("Move Left") {
        @Override
        protected void onAction() {
            PropertyMap gameVarsMap = getWorldProperties();
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(-basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            getWorldProperties().setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    private UserAction moveRight = new UserAction("Move Right") {
        @Override
        protected void onAction() {
            PropertyMap gameVarsMap = getWorldProperties();
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.translateX(basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            getWorldProperties().setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    private UserAction jump = new UserAction("Jump") {
        @Override
        protected void onAction() {
            PropertyMap gameVarsMap = getWorldProperties();
            double yAccelerator = gameVarsMap.getDouble("yAccelerator");
            if (yAccelerator < yAcceleratorMax) {
                gameVarsMap.setValue("yAccelerator", yAccelerator + yAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            PropertyMap gameVarsMap = getWorldProperties();
            gameVarsMap.setValue("jumping", true);
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
        input.addAction(jump, KeyCode.SPACE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("xAccelerator", xAcceleratorDefault);
        vars.put("yAccelerator", yAcceleratorDefault);
        vars.put("jumping", false);
        vars.put("distanceJumped", distanceJumpedDefault);
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
        PropertyMap gameVarsMap = getWorldProperties();
        System.out.println(gameVarsMap.getBoolean("jumping"));

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
            player.translateY(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}